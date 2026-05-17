package net.vampirestudios.arrp.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.vampirestudios.arrp.ARRPException;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.data.advancement.Advancement;
import net.vampirestudios.arrp.assets.animation.Animation;
import net.vampirestudios.arrp.assets.blockstates.BlockState;
import net.vampirestudios.arrp.data.entity.*;
import net.vampirestudios.arrp.assets.equipment.EquipmentModel;
import net.vampirestudios.arrp.assets.equipment.TrimMaterial;
import net.vampirestudios.arrp.assets.equipment.TrimPattern;
import net.vampirestudios.arrp.assets.item.ItemInfo;
import net.vampirestudios.arrp.assets.lang.Lang;
import net.vampirestudios.arrp.data.loot.LootTable;
import net.vampirestudios.arrp.assets.models.Model;
import net.vampirestudios.arrp.assets.models.Textures;
import net.vampirestudios.arrp.data.recipe.Recipe;
import net.vampirestudios.arrp.data.registry.*;
import net.vampirestudios.arrp.data.tags.Tag;
import net.vampirestudios.arrp.assets.timeline.Timeline;
import net.vampirestudios.arrp.data.worldgen.*;
import net.vampirestudios.arrp.data.worldgen.biome.Biome;
import net.vampirestudios.arrp.data.worldgen.dimension.Dimension;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;
import net.vampirestudios.arrp.data.worldgen.feature.ConfiguredFeature;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.arrp.data.worldgen.structure.Structure;
import net.vampirestudios.arrp.data.worldgen.structure.StructureSet;
import net.vampirestudios.arrp.mixin.ShapedRecipeBuilderAccessor;
import net.vampirestudios.arrp.util.CallableFunction;
import net.vampirestudios.arrp.util.CountingInputStream;
import net.vampirestudios.arrp.util.UnsafeByteArrayOutputStream;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.ApiStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.lang.String.valueOf;

@ApiStatus.Internal
public class RuntimeResourcePackImpl extends AbstractPackResources implements RuntimeResourcePack {
	public static final ExecutorService EXECUTOR_SERVICE;
	public static final boolean DUMP;
	public static final boolean DEBUG_PERFORMANCE;

	// @formatter:off
	public static final Gson GSON = new GsonBuilder()
									.setPrettyPrinting()
									.disableHtmlEscaping()
									.registerTypeAdapter(Textures.class, new Textures.Serializer())
									.registerTypeAdapter(Animation.class, new Animation.Serializer())
									.create();
	// if it works, don't touch it
	static final Set<String> KEY_WARNINGS = Collections.newSetFromMap(new ConcurrentHashMap<>());
	// @formatter:on
	public static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("RRP");

	static {
		Properties properties = new Properties();
		int processors = Math.max(Runtime.getRuntime().availableProcessors() / 2 - 1, 1);
		boolean dump = false;
		boolean performance = false;
		properties.setProperty("threads", valueOf(processors));
		properties.setProperty("dump assets", "false");
		properties.setProperty("debug performance", "false");

		File file = new File("config/rrp.properties");
		try (FileReader reader = new FileReader(file)) {
			properties.load(reader);
			processors = Integer.parseInt(properties.getProperty("threads"));
			dump = Boolean.parseBoolean(properties.getProperty("dump assets"));
			performance = Boolean.parseBoolean(properties.getProperty("debug performance"));
		} catch (Throwable t) {
			LOGGER.warn("Invalid config, creating new one!");
			file.getParentFile().mkdirs();
			try (FileWriter writer = new FileWriter(file)) {
				properties.store(writer, "number of threads RRP should use for generating resources");
			} catch (IOException ex) {
				LOGGER.error("Unable to write to RRP config!");
				ex.printStackTrace();
			}
		}
		EXECUTOR_SERVICE = Executors.newFixedThreadPool(
				processors,
				new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ARRP-Workers-%s").build()
		);
		DUMP = dump;
		DEBUG_PERFORMANCE = performance;
		KEY_WARNINGS.add("filter");
		KEY_WARNINGS.add("language");
	}

	private final Identifier id;
	private final Lock waiting = new ReentrantLock();
	private final Map<Identifier, Supplier<byte[]>> data = new ConcurrentHashMap<>();
	private final Map<Identifier, Supplier<byte[]>> assets = new ConcurrentHashMap<>();
	private final Map<List<String>, Supplier<byte[]>> root = new ConcurrentHashMap<>();
	private final Map<Identifier, Lang> langMergable = new ConcurrentHashMap<>();

	public RuntimeResourcePackImpl(Identifier id) {
		super(new PackLocationInfo(id.getNamespace() + ";" + id.getPath(), Component.nullToEmpty("Runtime Resource Pack " + id), PackSource.DEFAULT, Optional.empty()));
		this.id = id;
	}

	public RuntimeResourcePackImpl(Identifier id, int packFormat) {
		this(id);
		this.addPackMcmeta("Runtime Resource Pack " + id, packFormat);
	}

	@Override
	public void addRecoloredImage(Identifier identifier, InputStream target, IntUnaryOperator operator) {
		this.addLazyResource(PackType.CLIENT_RESOURCES, fix(identifier, "textures", "png"), (i, r) -> {
			try {

				// optimize buffer allocation, input and output image after recoloring should be roughly the same size
				CountingInputStream is = new CountingInputStream(target);
				// repaint image
				BufferedImage base = ImageIO.read(is);
				BufferedImage recolored = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < base.getHeight(); y++) {
					for (int x = 0; x < base.getWidth(); x++) {
						recolored.setRGB(x, y, operator.applyAsInt(base.getRGB(x, y)));
					}
				}
				// write image
				UnsafeByteArrayOutputStream baos = new UnsafeByteArrayOutputStream(is.bytes());
				ImageIO.write(recolored, "png", baos);
				return baos.getBytes();
			} catch (Throwable e) {
				e.printStackTrace();
				throw new ARRPException("Failed to recolor texture", e);
			}
		});
	}

	@Override
	public byte[] addLang(Identifier identifier, Lang lang) {
		return this.addJsonAsset(identifier, "lang", lang.getLang());
	}

	@Override
	public void mergeLang(Identifier identifier, Lang lang) {
		this.langMergable.compute(identifier, (identifier1, lang1) -> {
			if (lang1 == null) {
				lang1 = new Lang();
				Lang finalLang = lang1;
				this.addLazyResource(PackType.CLIENT_RESOURCES, identifier, (pack, identifier2) -> {
					return pack.addLang(identifier, finalLang);
				});
			}
			lang1.merge(lang);
			return lang1;
		});
	}

	@Override
	public byte[] addLootTable(Identifier identifier, LootTable table) {
		return this.addCodecData(identifier, "loot_table", LootTable.CODEC, table);
	}

	@Override
	public byte[] addWolfVariant(Identifier id, WolfVariant variant) {
		return this.addCodecData(id, "wolf_variant", WolfVariant.CODEC, variant);
	}

	@Override
	public byte[] addZombieNautilusVariant(Identifier id, ZombieNautilusVariant variant) {
		return this.addCodecData(id, "zombie_nautilus_variant", ZombieNautilusVariant.CODEC, variant);
	}

	@Override
	public byte[] addChickenVariant(Identifier id, ChickenVariant variant) {
		return this.addCodecData(id, "chicken_variant", ChickenVariant.CODEC, variant);
	}

	@Override
	public byte[] addCowVariant(Identifier id, CowVariant variant) {
		return this.addCodecData(id, "cow_variant", CowVariant.CODEC, variant);
	}

	@Override
	public byte[] addPigVariant(Identifier id, PigVariant variant) {
		return this.addCodecData(id, "pig_variant", PigVariant.CODEC, variant);
	}

	@Override
	public byte[] addWolfSoundVariant(Identifier id, WolfSoundVariant variant) {
		return this.addCodecData(id, "wolf_sound_variant", WolfSoundVariant.CODEC, variant);
	}

	@Override
	public byte[] addCatSoundVariant(Identifier id, CatSoundVariant variant) {
		return this.addCodecData(id, "cat_sound_variant", CatSoundVariant.CODEC, variant);
	}

	@Override
	public byte[] addChickenSoundVariant(Identifier id, ChickenSoundVariant variant) {
		return this.addCodecData(id, "chicken_sound_variant", ChickenSoundVariant.CODEC, variant);
	}

	@Override
	public byte[] addCowSoundVariant(Identifier id, CowSoundVariant variant) {
		return this.addCodecData(id, "cow_sound_variant", CowSoundVariant.CODEC, variant);
	}

	@Override
	public byte[] addPigSoundVariant(Identifier id, PigSoundVariant variant) {
		return this.addCodecData(id, "pig_sound_variant", PigSoundVariant.CODEC, variant);
	}

	@Override
	public byte[] addSimpleMobVariant(Identifier variantFolder, Identifier id, SimpleMobVariant variant) {
		return this.addCodecData(id, variantFolder.getPath(), SimpleMobVariant.CODEC, variant);
	}

	@Override
	public byte[] addCatVariant(Identifier id, SimpleMobVariant v) {
		return addSimpleMobVariant(Identifier.fromNamespaceAndPath("minecraft", "cat_variant"), id, v);
	}

	@Override
	public byte[] addFrogVariant(Identifier id, SimpleMobVariant v) {
		return addSimpleMobVariant(Identifier.fromNamespaceAndPath("minecraft", "frog_variant"), id, v);
	}

	@Override
	public byte[] addPaintingVariant(Identifier id, PaintingVariant variant) {
		return this.addCodecData(id, "painting_variant", PaintingVariant.CODEC, variant);
	}

	@Override
	public Future<byte[]> addAsyncResource(PackType type, Identifier path, CallableFunction<Identifier, byte[]> data) {
		Future<byte[]> future = EXECUTOR_SERVICE.submit(() -> data.get(path));
		this.getSys(type).put(path, () -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new ARRPException("Async resource generation failed", e);
			}
		});
		return future;
	}

	@Override
	public void addLazyResource(PackType type, Identifier path, BiFunction<RuntimeResourcePack, Identifier, byte[]> func) {
		this.getSys(type).put(path, new Memoized<>(func, path));
	}

	@Override
	public byte[] addResource(PackType type, Identifier path, byte[] data) {
		this.getSys(type).put(path, () -> data);
		return data;
	}

	@Override
	public Future<byte[]> addAsyncRootResource(String path, CallableFunction<String, byte[]> data) {
		List<String> key = rootKey(path);
		Future<byte[]> future = EXECUTOR_SERVICE.submit(() -> data.get(path));
		this.root.put(key, () -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new ARRPException("Async resource generation failed", e);
			}
		});
		return future;
	}

	@Override
	public void addLazyRootResource(String path, BiFunction<RuntimeResourcePack, String, byte[]> data) {
		this.root.put(rootKey(path), new Memoized<>(data, path));
	}

	@Override
	public byte[] addRootResource(String path, byte[] data) {
		this.root.put(rootKey(path), () -> data);
		return data;
	}

	@Override
	public byte[] addPackMcmeta(String description, int packFormat) {
		String json = "{\n"
				+ "  \"pack\": {\n"
				+ "    \"description\": " + GSON.toJson(description) + ",\n"
				+ "    \"pack_format\": " + packFormat + "\n"
				+ "  }\n"
				+ "}";
		return this.addRootResource("pack.mcmeta", json.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public byte[] addAsset(Identifier path, byte[] data) {
		return this.addResource(PackType.CLIENT_RESOURCES, path, data);
	}

	@Override
	public byte[] addData(Identifier path, byte[] data) {
		return this.addResource(PackType.SERVER_DATA, path, data);
	}

	@Override
	public byte[] addAdvancement(Advancement advancement, Identifier path) {
		return this.addCodecData(path, "advancement", Advancement.CODEC, advancement);
	}

	@Override
	public byte[] addModel(Model model, Identifier path) {
		return this.addJsonAsset(path, "models", model);
	}

	@Override
	public byte[] addItemModelInfo(ItemInfo model, Identifier path) {
		return this.addCodecAsset(path, "items", ItemInfo.CODEC, model);
	}

	@Override
	public byte[] addEquipmentModel(EquipmentModel model, Identifier path) {
		return this.addCodecAsset(path, "equipment", EquipmentModel.CODEC, model);
	}

	@Override
	public byte[] addTrimMaterial(Identifier id, TrimMaterial material) {
		return this.addCodecData(id, "trim_material", TrimMaterial.CODEC, material);
	}

	@Override
	public byte[] addTrimPattern(Identifier id, TrimPattern pattern) {
		return this.addCodecData(id, "trim_pattern", TrimPattern.CODEC, pattern);
	}

	@Override
	public byte[] addBannerPattern(Identifier id, BannerPattern pattern) {
		return this.addCodecData(id, "banner_pattern", BannerPattern.CODEC, pattern);
	}

	@Override
	public byte[] addDecoratedPotPattern(Identifier id, DecoratedPotPattern pattern) {
		return this.addCodecData(id, "decorated_pot_pattern", DecoratedPotPattern.CODEC, pattern);
	}

	@Override
	public byte[] addEnchantment(Identifier id, Enchantment enchantment) {
		return this.addCodecData(id, "enchantment", Enchantment.CODEC, enchantment);
	}

	@Override
	public byte[] addDamageType(Identifier id, DamageType damageType) {
		return this.addCodecData(id, "damage_type", DamageType.CODEC, damageType);
	}

	@Override
	public byte[] addInstrument(Identifier id, Instrument instrument) {
		return this.addCodecData(id, "instrument", Instrument.CODEC, instrument);
	}

	@Override
	public byte[] addJukeboxSong(Identifier id, JukeboxSong song) {
		return this.addCodecData(id, "jukebox_song", JukeboxSong.CODEC, song);
	}

	@Override
	public byte[] addConfiguredCarver(Identifier id, ConfiguredCarver configuredCarver) {
		return this.addCodecData(id, "worldgen/configured_carver", ConfiguredCarver.CODEC, configuredCarver);
	}

	@Override
	public byte[] addProcessorList(Identifier id, ProcessorList processorList) {
		return this.addCodecData(id, "worldgen/processor_list", ProcessorList.CODEC, processorList);
	}

	@Override
	public byte[] addTemplatePool(Identifier id, TemplatePool templatePool) {
		return this.addCodecData(id, "worldgen/template_pool", TemplatePool.CODEC, templatePool);
	}

	@Override
	public byte[] addWorldPreset(Identifier id, WorldPreset worldPreset) {
		return this.addCodecData(id, "worldgen/world_preset", WorldPreset.CODEC, worldPreset);
	}

	@Override
	public byte[] addFlatLevelGeneratorPreset(Identifier id, FlatLevelGeneratorPreset preset) {
		return this.addCodecData(id, "worldgen/flat_level_generator_preset", FlatLevelGeneratorPreset.CODEC, preset);
	}

	@Override
	public byte[] addTradeSet(Identifier id, TradeSet tradeSet) {
		return this.addCodecData(id, "trade_set", TradeSet.CODEC, tradeSet);
	}

	@Override
	public byte[] addVillagerTrade(Identifier id, VillagerTrade trade) {
		return this.addCodecData(id, "villager_trade", VillagerTrade.CODEC, trade);
	}

	@Override
	public byte[] addDialog(Identifier id, Dialog dialog) {
		return this.addCodecData(id, "dialog", Dialog.CODEC, dialog);
	}

	@Override
	public byte[] addWorldClock(Identifier id, WorldClock clock) {
		return this.addCodecData(id, "world_clock", WorldClock.CODEC, clock);
	}

	@Override
	public byte[] addBlockState(BlockState state, Identifier path) {
		return this.addCodecAsset(path, "blockstates", BlockState.CODEC, state);
	}

	@Override
	public byte[] addTexture(Identifier id, BufferedImage image) {
		UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", ubaos);
		} catch (IOException e) {
			throw new AssertionError("ImageIO.write to in-memory buffer should never fail", e);
		}
		return this.addAsset(fix(id, "textures", "png"), ubaos.getBytes());
	}

	@Override
	public byte[] addAnimation(Identifier id, Animation animation) {
		return this.addAsset(fix(id, "textures", "png.mcmeta"), toJsonBytes(animation));
	}

	@Override
	public byte[] addTag(Identifier id, Tag tag) {
		return this.addCodecData(id, "tags", Tag.CODEC, tag);
	}

	@Override
	public byte[] addRecipe(Identifier id, Recipe recipe) {
		return this.addCodecData(id, "recipe", Recipe.CODEC, recipe);
	}

	@Override
	public byte[] addRecipe(Identifier id, RecipeBuilder recipe) {
		if (recipe instanceof ShapedRecipeBuilder shapedRecipeBuilder) {
			var accessor = ((ShapedRecipeBuilderAccessor) shapedRecipeBuilder);
			ShapedRecipePattern pattern = ShapedRecipePattern.of(accessor.getKey(), accessor.getRows());
			ShapedRecipe recipe1 = new ShapedRecipe(
					RecipeBuilder.createCraftingCommonInfo(accessor.isShowNotification()),
					RecipeBuilder.createCraftingBookInfo(accessor.getCategory(), accessor.getGroup()), pattern, accessor.getResult()
			);
			return this.addCodecData(id, "recipe", ShapedRecipe.CODEC, recipe1);
		}
		return new byte[0];
	}

	@Override
	public byte[] addTimeline(Identifier id, Timeline timeline) {
		return this.addCodecData(id, "timelines", Timeline.CODEC, timeline);
	}

	@Override
	public byte[] addBiome(Identifier id, Biome biome) {
		return this.addCodecData(id, "worldgen/biome", Biome.CODEC, biome);
	}

	@Override
	public byte[] addDimension(Identifier id, Dimension dimension) {
		return this.addCodecData(id, "dimension", Dimension.CODEC, dimension);
	}

	@Override
	public byte[] addDimensionType(Identifier id, DimensionType dimensionType) {
		return this.addCodecData(id, "dimension_type", DimensionType.CODEC, dimensionType);
	}

	@Override
	public byte[] addConfiguredFeature(Identifier id, ConfiguredFeature configuredFeature) {
		return this.addCodecData(id, "worldgen/configured_feature", ConfiguredFeature.CODEC, configuredFeature);
	}

	@Override
	public byte[] addPlacedFeature(Identifier id, PlacedFeature placedFeature) {
		return this.addCodecData(id, "worldgen/placed_feature", PlacedFeature.CODEC, placedFeature);
	}

	@Override
	public byte[] addNoiseSettings(Identifier id, NoiseSettings noiseSettings) {
		return this.addCodecData(id, "worldgen/noise_settings", NoiseSettings.CODEC, noiseSettings);
	}

	@Override
	public byte[] addStructure(Identifier id, Structure structure) {
		return this.addCodecData(id, "worldgen/structure", Structure.CODEC, structure);
	}

	@Override
	public byte[] addStructureSet(Identifier id, StructureSet structureSet) {
		return this.addCodecData(id, "worldgen/structure_set", StructureSet.CODEC, structureSet);
	}

	@Override
	public Future<?> async(Consumer<RuntimeResourcePack> action) {
		this.lock();
		try {
			return EXECUTOR_SERVICE.submit(() -> {
				try {
					action.accept(this);
				} finally {
					this.waiting.unlock();
				}
			});
		} catch (RuntimeException e) {
			this.waiting.unlock();
			throw e;
		}
	}

	@Override
	public void dumpDirect(Path output) {
		LOGGER.info("dumping {}'s assets and data", this.id);
		// data dump time
		try {
			for (Map.Entry<List<String>, Supplier<byte[]>> e : this.root.entrySet()) {
				String pathStr = String.join("/", e.getKey());
				Path path = output.resolve(pathStr);
				if (path.toAbsolutePath().startsWith(output.toAbsolutePath())) {
					Files.createDirectories(path.getParent());
					Files.write(path, e.getValue().get());
				} else {
					LOGGER.error("RRP contains out-of-directory path! \"{}\"", pathStr);
				}
			}

			Path assets = output.resolve("assets");
			Files.createDirectories(assets);
			for (Map.Entry<Identifier, Supplier<byte[]>> entry : this.assets.entrySet()) {
				this.write(assets, entry.getKey(), entry.getValue().get());
			}

			Path data = output.resolve("data");
			Files.createDirectories(data);
			for (Map.Entry<Identifier, Supplier<byte[]>> entry : this.data.entrySet()) {
				this.write(data, entry.getKey(), entry.getValue().get());
			}
		} catch (IOException exception) {
			throw new ARRPException("Failed to dump pack to directory", exception);
		}
	}

	@Override
	public void load(Path dir) throws IOException {
		try (Stream<Path> stream = Files.walk(dir)) {
			for (Path file : (Iterable<Path>) () -> stream.filter(Files::isRegularFile).iterator()) {
				String s = dir.relativize(file).toString().replace(File.separatorChar, '/');
				if (s.startsWith("assets/")) {
					String path = s.substring("assets".length() + 1);
					this.load(path, this.assets, Files.readAllBytes(file));
				} else if (s.startsWith("data/")) {
					String path = s.substring("data".length() + 1);
					this.load(path, this.data, Files.readAllBytes(file));
				} else {
					byte[] data = Files.readAllBytes(file);
					this.root.put(rootKey(s), () -> data);
				}
			}
		}
	}

	@Override
	public void dump(ZipOutputStream zos) throws IOException {
		this.lock();
		try {
			for (Map.Entry<List<String>, Supplier<byte[]>> entry : this.root.entrySet()) {
				zos.putNextEntry(new ZipEntry(String.join("/", entry.getKey())));
				zos.write(entry.getValue().get());
				zos.closeEntry();
			}

			for (Map.Entry<Identifier, Supplier<byte[]>> entry : this.assets.entrySet()) {
				Identifier id = entry.getKey();
				zos.putNextEntry(new ZipEntry("assets/" + id.getNamespace() + "/" + id.getPath()));
				zos.write(entry.getValue().get());
				zos.closeEntry();
			}

			for (Map.Entry<Identifier, Supplier<byte[]>> entry : this.data.entrySet()) {
				Identifier id = entry.getKey();
				zos.putNextEntry(new ZipEntry("data/" + id.getNamespace() + "/" + id.getPath()));
				zos.write(entry.getValue().get());
				zos.closeEntry();
			}
		} finally {
			this.waiting.unlock();
		}
	}

	@Override
	public void load(ZipInputStream stream) throws IOException {
		ZipEntry entry;
		while ((entry = stream.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				continue;
			}
			String s = entry.getName();
			if (s.startsWith("assets/")) {
				String path = s.substring("assets".length() + 1);
				this.load(path, this.assets, this.read(entry, stream));
			} else if (s.startsWith("data/")) {
				String path = s.substring("data".length() + 1);
				this.load(path, this.data, this.read(entry, stream));
			} else {
				byte[] data = this.read(entry, stream);
				this.root.put(rootKey(s), () -> data);
			}
		}
	}

	/**
	 * pack.png and that's about it I think/hope
	 *
	 * @param segments the name of the file, can't be a path tho
	 * @return the pack.png image as a stream
	 */
	@Override
	public IoSupplier<InputStream> getRootResource(String... segments) {
		this.lock();
		try {
			Supplier<byte[]> supplier = this.root.get(Arrays.asList(segments));
			if (supplier == null) {
				return null;
			}
			return () -> new ByteArrayInputStream(supplier.get());
		} finally {
			this.waiting.unlock();
		}
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType type, Identifier id) {
		this.lock();
		try {
			Supplier<byte[]> supplier = this.getSys(type).get(id);
			if (supplier == null) {
				//LOGGER.warn("No resource found for " + id);
				return null;
			}
			return () -> new ByteArrayInputStream(supplier.get());
		} finally {
			this.waiting.unlock();
		}
	}

	@Override
	public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
		this.lock();
		try {
			for (Identifier identifier : this.getSys(type).keySet()) {
				Supplier<byte[]> supplier = this.getSys(type).get(identifier);
				if (supplier == null) {
					//LOGGER.warn("No resource found for " + identifier);
					continue;
				}
				IoSupplier<InputStream> inputSupplier = () -> new ByteArrayInputStream(supplier.get());
				if (identifier.getNamespace().equals(namespace) && identifier.getPath().startsWith(prefix)) {
					consumer.accept(identifier, inputSupplier);
				}
			}
		} finally {
			this.waiting.unlock();
		}
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		this.lock();
		try {
			Set<String> namespaces = new HashSet<>();
			for (Identifier identifier : this.getSys(type).keySet()) {
				namespaces.add(identifier.getNamespace());
			}
			return namespaces;
		} finally {
			this.waiting.unlock();
		}
	}

	@Override
	public void close() {
		LOGGER.info("closing rrp {}", this.id);

		// lock
		this.lock();
		try {
			if (DUMP) {
				this.dump();
			}
		} finally {
			// unlock
			this.waiting.unlock();
		}
	}

	private byte[] addJsonAsset(Identifier id, String prefix, Object value) {
		return this.addAsset(fix(id, prefix, "json"), toJsonBytes(value));
	}

	private <T> byte[] addCodecAsset(Identifier id, String prefix, Codec<T> codec, T value) {
		return this.addAsset(fix(id, prefix, "json"), JsonBytes.encodeToPrettyBytes(codec, value));
	}

	private <T> byte[] addCodecData(Identifier id, String prefix, Codec<T> codec, T value) {
		return this.addData(fix(id, prefix, "json"), JsonBytes.encodeToPrettyBytes(codec, value));
	}

	private static byte[] toJsonBytes(Object object) {
		UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(ubaos, StandardCharsets.UTF_8);
		GSON.toJson(object, writer);
		try {
			writer.close();
		} catch (IOException e) {
			throw new ARRPException("Failed to serialize resource to JSON bytes", e);
		}
		return ubaos.getBytes();
	}

	private static Identifier fix(Identifier identifier, String prefix, String append) {
		return Identifier.fromNamespaceAndPath(identifier.getNamespace(), prefix + '/' + identifier.getPath() + '.' + append);
	}

	protected byte[] read(ZipEntry entry, InputStream stream) throws IOException {
		return stream.readAllBytes();
	}

	protected void load(String fullPath, Map<Identifier, Supplier<byte[]>> map, byte[] data) {
		int sep = fullPath.indexOf('/');
		if (sep <= 0 || sep == fullPath.length() - 1) {
			throw new IllegalArgumentException("Invalid resource path: " + fullPath);
		}
		String namespace = fullPath.substring(0, sep);
		String path = fullPath.substring(sep + 1);
		map.put(Identifier.fromNamespaceAndPath(namespace, path), () -> data);
	}

	private void lock() {
		if (!this.waiting.tryLock()) {
			if (DEBUG_PERFORMANCE) {
				long start = System.currentTimeMillis();
				this.waiting.lock();
				long end = System.currentTimeMillis();
				LOGGER.warn("waited " + (end - start) + "ms for lock in RRP: " + this.id);
			} else {
				this.waiting.lock();
			}
		}
	}

	private void write(Path dir, Identifier identifier, byte[] data) {
		try {
			String namespace = identifier.getNamespace();
			String path = identifier.getPath();
			Path file = dir.resolve(namespace).resolve(path);
			if (file.toAbsolutePath().startsWith(dir.toAbsolutePath())) {
				Files.createDirectories(file.getParent());
				try (OutputStream output = Files.newOutputStream(file)) {
					output.write(data);
				}
			} else {
				LOGGER.error("RRP contains out-of-directory location! \"" + namespace + "/" + path + "\"");
			}

		} catch (IOException e) {
			throw new ARRPException("Failed to write resource file: " + identifier.getNamespace() + "/" + identifier.getPath(), e);
		}
	}

	private Map<Identifier, Supplier<byte[]>> getSys(PackType side) {
		return side == PackType.CLIENT_RESOURCES ? this.assets : this.data;
	}

	private static List<String> rootKey(String path) {
		String normalized = path.replace('\\', '/');
		if (normalized.isEmpty()
				|| normalized.startsWith("/")
				|| normalized.contains(":")
				|| normalized.endsWith("/")) {
			throw new IllegalArgumentException("Invalid root resource path: " + path);
		}
		String[] split = normalized.split("/");
		for (String segment : split) {
			if (segment.isEmpty() || segment.equals(".") || segment.equals("..")) {
				throw new IllegalArgumentException("Invalid root resource path: " + path);
			}
		}
		return List.of(split);
	}

	private class Memoized<T> implements Supplier<byte[]> {
		private final BiFunction<RuntimeResourcePack, T, byte[]> func;
		private final T path;
		private byte[] data;

		public Memoized(BiFunction<RuntimeResourcePack, T, byte[]> func, T path) {
			this.func = func;
			this.path = path;
		}

		@Override
		public byte[] get() {
			if (this.data == null) {
				this.data = func.apply(RuntimeResourcePackImpl.this, path);
			}
			return this.data;
		}
	}
}
