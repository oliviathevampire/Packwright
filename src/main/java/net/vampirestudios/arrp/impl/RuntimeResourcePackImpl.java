package net.vampirestudios.arrp.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.json.JsonBytes;
import net.vampirestudios.arrp.json.advancement.JAdvancement;
import net.vampirestudios.arrp.json.animation.JAnimation;
import net.vampirestudios.arrp.json.blockstate.JState;
import net.vampirestudios.arrp.json.entityVariants.*;
import net.vampirestudios.arrp.json.equipmentinfo.JEquipmentModel;
import net.vampirestudios.arrp.json.iteminfo.JItemInfo;
import net.vampirestudios.arrp.json.lang.JLang;
import net.vampirestudios.arrp.json.loot.JLootTable;
import net.vampirestudios.arrp.json.models.JModel;
import net.vampirestudios.arrp.json.models.JTextures;
import net.vampirestudios.arrp.json.recipe.JRecipe;
import net.vampirestudios.arrp.json.tags.JTag;
import net.vampirestudios.arrp.json.timeline.JTimeline;
import net.vampirestudios.arrp.json.worldgen.biome.JBiome;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimension;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimensionType;
import net.vampirestudios.arrp.json.worldgen.feature.JConfiguredFeature;
import net.vampirestudios.arrp.json.worldgen.feature.JPlacedFeature;
import net.vampirestudios.arrp.json.worldgen.noise.JNoiseSettings;
import net.vampirestudios.arrp.json.worldgen.structure.JStructure;
import net.vampirestudios.arrp.json.worldgen.structure.JStructureSet;
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
									.registerTypeAdapter(JTextures.class, new JTextures.Serializer())
									.registerTypeAdapter(JAnimation.class, new JAnimation.Serializer())
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
	private final Map<Identifier, JLang> langMergable = new ConcurrentHashMap<>();

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
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public byte[] addLang(Identifier identifier, JLang lang) {
		return this.addAsset(fix(identifier, "lang", "json"), serialize(lang.getLang()));
	}

	@Override
	public void mergeLang(Identifier identifier, JLang lang) {
		this.langMergable.compute(identifier, (identifier1, lang1) -> {
			if (lang1 == null) {
				lang1 = new JLang();
				JLang finalLang = lang1;
				this.addLazyResource(PackType.CLIENT_RESOURCES, identifier, (pack, identifier2) -> {
					return pack.addLang(identifier, finalLang);
				});
			}
			lang1.getLang().putAll(lang.getLang());
			return lang1;
		});
	}

	@Override
	public byte[] addLootTable(Identifier identifier, JLootTable table) {
		return this.addData(fix(identifier, "loot_table", "json"), JsonBytes.encodeToPrettyBytes(JLootTable.CODEC, table));
	}

	@Override
	public byte[] addWolfVariant(Identifier id, JWolfVariant variant) {
		return this.addData(fix(id, "wolf_variant", "json"),
				JsonBytes.encodeToPrettyBytes(JWolfVariant.CODEC, variant));
	}

	@Override
	public byte[] addZombieNautilusVariant(Identifier id, JZombieNautilusVariant variant) {
		return this.addData(fix(id, "zombie_nautilus_variant", "json"),
				JsonBytes.encodeToPrettyBytes(JZombieNautilusVariant.CODEC, variant));
	}

	@Override
	public byte[] addChickenVariant(Identifier id, JChickenVariant variant) {
		return this.addData(fix(id, "chicken_variant", "json"),
				JsonBytes.encodeToPrettyBytes(JChickenVariant.CODEC, variant));
	}

	@Override
	public byte[] addCowVariant(Identifier id, JCowVariant variant) {
		return this.addData(fix(id, "cow_variant", "json"),
				JsonBytes.encodeToPrettyBytes(JCowVariant.CODEC, variant));
	}

	@Override
	public byte[] addPigVariant(Identifier id, JPigVariant variant) {
		return this.addData(fix(id, "pig_variant", "json"),
				JsonBytes.encodeToPrettyBytes(JPigVariant.CODEC, variant));
	}

	@Override
	public byte[] addWolfSoundVariant(Identifier id, JWolfSoundVariant variant) {
		return this.addData(fix(id, "wolf_sound_variant", "json"),
				JsonBytes.encodeToPrettyBytes(JWolfSoundVariant.CODEC, variant));
	}

	@Override
	public byte[] addSimpleMobVariant(Identifier variantFolder, Identifier id, JSimpleMobVariant variant) {
		return this.addData(fix(id, variantFolder.getPath(), "json"),
				JsonBytes.encodeToPrettyBytes(JSimpleMobVariant.CODEC, variant));
	}

	@Override
	public byte[] addCatVariant(Identifier id, JSimpleMobVariant v) {
		return addSimpleMobVariant(Identifier.fromNamespaceAndPath("minecraft","cat_variant"), id, v);
	}

	@Override
	public byte[] addFrogVariant(Identifier id, JSimpleMobVariant v) {
		return addSimpleMobVariant(Identifier.fromNamespaceAndPath("minecraft","frog_variant"), id, v);
	}

	@Override
	public Future<byte[]> addAsyncResource(PackType type, Identifier path, CallableFunction<Identifier, byte[]> data) {
		Future<byte[]> future = EXECUTOR_SERVICE.submit(() -> data.get(path));
		this.getSys(type).put(path, () -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
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
				throw new RuntimeException(e);
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
	public byte[] addAdvancement(JAdvancement advancement, Identifier path) {
		return this.addData(fix(path, "advancement", "json"), JsonBytes.encodeToPrettyBytes(JAdvancement.CODEC, advancement));
	}

	@Override
	public byte[] addModel(JModel model, Identifier path) {
		return this.addAsset(fix(path, "models", "json"), serialize(model));
	}

	@Override
	public byte[] addItemModelInfo(JItemInfo model, Identifier path) {
		return this.addAsset(fix(path, "items", "json"), JsonBytes.encodeToPrettyBytes(JItemInfo.CODEC, model));
	}

	@Override
	public byte[] addEquipmentModel(JEquipmentModel model, Identifier path) {
		return this.addAsset(fix(path, "equipment", "json"), JsonBytes.encodeToPrettyBytes(JEquipmentModel.CODEC, model));
	}

	@Override
	public byte[] addBlockState(JState state, Identifier path) {
		return this.addAsset(fix(path, "blockstates", "json"), JsonBytes.encodeToPrettyBytes(JState.CODEC, state));
	}

	@Override
	public byte[] addTexture(Identifier id, BufferedImage image) {
		UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", ubaos);
		} catch (IOException e) {
			throw new RuntimeException("impossible.", e);
		}
		return this.addAsset(fix(id, "textures", "png"), ubaos.getBytes());
	}

	@Override
	public byte[] addAnimation(Identifier id, JAnimation animation) {
		return this.addAsset(fix(id, "textures", "png.mcmeta"), serialize(animation));
	}

	@Override
	public byte[] addTag(Identifier id, JTag tag) {
		return this.addData(fix(id, "tags", "json"), JsonBytes.encodeToPrettyBytes(JTag.CODEC, tag));
	}

	@Override
	public byte[] addRecipe(Identifier id, JRecipe recipe) {
		return this.addData(fix(id, "recipe", "json"), JsonBytes.encodeToPrettyBytes(JRecipe.CODEC, recipe));
	}

	@Override
	public byte[] addTimeline(Identifier id, JTimeline timeline) {
		return this.addData(fix(id, "timelines", "json"), JsonBytes.encodeToPrettyBytes(JTimeline.CODEC, timeline));
	}

	@Override
	public byte[] addBiome(Identifier id, JBiome biome) {
		return this.addData(fix(id, "worldgen/biome", "json"), JsonBytes.encodeToPrettyBytes(JBiome.CODEC, biome));
	}

	@Override
	public byte[] addDimension(Identifier id, JDimension dimension) {
		return this.addData(fix(id, "dimension", "json"), JsonBytes.encodeToPrettyBytes(JDimension.CODEC, dimension));
	}

	@Override
	public byte[] addDimensionType(Identifier id, JDimensionType dimensionType) {
		return this.addData(fix(id, "dimension_type", "json"), JsonBytes.encodeToPrettyBytes(JDimensionType.CODEC, dimensionType));
	}

	@Override
	public byte[] addConfiguredFeature(Identifier id, JConfiguredFeature configuredFeature) {
		return this.addData(fix(id, "worldgen/configured_feature", "json"), JsonBytes.encodeToPrettyBytes(JConfiguredFeature.CODEC, configuredFeature));
	}

	@Override
	public byte[] addPlacedFeature(Identifier id, JPlacedFeature placedFeature) {
		return this.addData(fix(id, "worldgen/placed_feature", "json"), JsonBytes.encodeToPrettyBytes(JPlacedFeature.CODEC, placedFeature));
	}

	@Override
	public byte[] addNoiseSettings(Identifier id, JNoiseSettings noiseSettings) {
		return this.addData(fix(id, "worldgen/noise_settings", "json"), JsonBytes.encodeToPrettyBytes(JNoiseSettings.CODEC, noiseSettings));
	}

	@Override
	public byte[] addStructure(Identifier id, JStructure structure) {
		return this.addData(fix(id, "worldgen/structure", "json"), JsonBytes.encodeToPrettyBytes(JStructure.CODEC, structure));
	}

	@Override
	public byte[] addStructureSet(Identifier id, JStructureSet structureSet) {
		return this.addData(fix(id, "worldgen/structure_set", "json"), JsonBytes.encodeToPrettyBytes(JStructureSet.CODEC, structureSet));
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
			throw new RuntimeException(exception);
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
		Supplier<byte[]> supplier = this.root.get(Arrays.asList(segments));
		if (supplier == null) {
			this.waiting.unlock();
			return null;
		}
		this.waiting.unlock();
		return () -> new ByteArrayInputStream(supplier.get());
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType type, Identifier id) {
		this.lock();
		Supplier<byte[]> supplier = this.getSys(type).get(id);
		if (supplier == null) {
			//LOGGER.warn("No resource found for " + id);
			this.waiting.unlock();
			return null;
		}
		this.waiting.unlock();
		return () -> new ByteArrayInputStream(supplier.get());
	}

	@Override
	public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
		this.lock();
		for (Identifier identifier : this.getSys(type).keySet()) {
			Supplier<byte[]> supplier = this.getSys(type).get(identifier);
			if (supplier == null) {
				//LOGGER.warn("No resource found for " + identifier);
				this.waiting.unlock();
				continue;
			}
			IoSupplier<InputStream> inputSupplier = () -> new ByteArrayInputStream(supplier.get());
			if (identifier.getNamespace().equals(namespace) && identifier.getPath().startsWith(prefix)) {
				consumer.accept(identifier, inputSupplier);
			}
		}
		this.waiting.unlock();
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		this.lock();
		Set<String> namespaces = new HashSet<>();
		for (Identifier identifier : this.getSys(type).keySet()) {
			namespaces.add(identifier.getNamespace());
		}
		this.waiting.unlock();
		return namespaces;
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

	private static byte[] serialize(Object object) {
		UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(ubaos, StandardCharsets.UTF_8);
		GSON.toJson(object, writer);
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
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
			throw new RuntimeException(e);
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
