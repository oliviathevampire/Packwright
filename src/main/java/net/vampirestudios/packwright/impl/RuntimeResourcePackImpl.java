package net.vampirestudios.packwright.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackMetadataResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.vampirestudios.packwright.PackwrightException;
import net.vampirestudios.packwright.api.ResourceType;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.assets.animation.Animation;
import net.vampirestudios.packwright.assets.lang.Lang;
import net.vampirestudios.packwright.assets.texture.TextureMeta;
import net.vampirestudios.packwright.mixin.ShapedRecipeBuilderAccessor;
import net.vampirestudios.packwright.util.CallableFunction;
import net.vampirestudios.packwright.util.CountingInputStream;
import net.vampirestudios.packwright.util.GrowableByteBuffer;
import net.vampirestudios.packwright.util.JsonBytes;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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

@ApiStatus.Internal
public class RuntimeResourcePackImpl extends AbstractPackMetadataResources implements RuntimeResourcePack {
	public static final PackwrightConfig CONFIG = PackwrightConfig.get();
	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(
			CONFIG.threads,
			new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Packwright-Workers-%s").build()
	);
	// if it works, don't touch it
	static final Set<String> KEY_WARNINGS = Collections.newSetFromMap(new ConcurrentHashMap<>());
	public static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("Packwright");

	static {
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
		super(new PackLocationInfo(id.getNamespace() + ";" + id.getPath(), Component.nullToEmpty("Packwright pack " + id), PackSource.DEFAULT, Optional.empty()));
		this.id = id;
	}

	public RuntimeResourcePackImpl(Identifier id, int packFormat) {
		this(id);
		this.addPackMcmeta("Packwright pack " + id, packFormat);
	}

	@Override
	public void addRecoloredImage(Identifier identifier, InputStream target, IntUnaryOperator operator) {
		this.addLazyResource(PackType.CLIENT_RESOURCES, fix(identifier, "textures", "png"), (i, r) -> {
			try {
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
				GrowableByteBuffer buffer = new GrowableByteBuffer(is.bytes());
				ImageIO.write(recolored, "png", buffer);
				return buffer.toByteArray();
			} catch (Throwable e) {
				e.printStackTrace();
				throw new PackwrightException("Failed to recolor texture", e);
			}
		});
	}

	@Override
	public <T> byte[] add(ResourceType<T> type, Identifier id, T value) {
		return this.addResource(type.packType(), fix(id, type.directory(), type.extension()),
				JsonBytes.encodeToPrettyBytes(type.codec(), value));
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
	public Future<byte[]> addAsyncResource(PackType type, Identifier path, CallableFunction<Identifier, byte[]> data) {
		Future<byte[]> future = EXECUTOR_SERVICE.submit(() -> data.get(path));
		this.getSys(type).put(path, () -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new PackwrightException("Async resource generation failed", e);
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
				throw new PackwrightException("Async resource generation failed", e);
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
		JsonObject packSection = new JsonObject();
		packSection.addProperty("description", description);
		packSection.addProperty("pack_format", packFormat);
		JsonObject root = new JsonObject();
		root.add("pack", packSection);
		return this.addRootResource("pack.mcmeta", toJsonBytes(root));
	}

	@Override
	public byte[] addPackMcmeta(String description, int formatMajor, int formatMinor) {
		JsonArray format = new JsonArray();
		format.add(formatMajor);
		format.add(formatMinor);
		JsonObject packSection = new JsonObject();
		packSection.addProperty("description", description);
		packSection.add("min_format", format);
		packSection.add("max_format", format.deepCopy());
		JsonObject root = new JsonObject();
		root.add("pack", packSection);
		return this.addRootResource("pack.mcmeta", toJsonBytes(root));
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
	public byte[] addTexture(Identifier id, BufferedImage image) {
		GrowableByteBuffer buffer = new GrowableByteBuffer();
		try {
			ImageIO.write(image, "png", buffer);
		} catch (IOException e) {
			throw new AssertionError("ImageIO.write to in-memory buffer should never fail", e);
		}
		return this.addAsset(fix(id, "textures", "png"), buffer.toByteArray());
	}

	@Override
	public byte[] addAnimation(Identifier id, Animation animation) {
		// the mcmeta needs the section wrapper: {"animation": {...}}
		return this.addTextureMeta(id, TextureMeta.meta().animation(animation));
	}

	@Override
	public byte[] addTextureMeta(Identifier id, TextureMeta meta) {
		return this.addAsset(fix(id, "textures", "png.mcmeta"),
				toJsonBytes(JsonBytes.toJsonElement(TextureMeta.CODEC, meta)));
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
			return this.add(ResourceType.data("recipe", ShapedRecipe.CODEC), id, recipe1);
		}
		return new byte[0];
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
					LOGGER.error("Packwright contains out-of-directory path! \"{}\"", pathStr);
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
			throw new PackwrightException("Failed to dump pack to directory", exception);
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
	 * pack.png and that's about it, I think/hope
	 *
	 * @param segments the name of the file, can't be a path tho
	 * @return the pack.png image as a stream
	 */
	@Override
	public IoSupplier<InputStream> getRootResource(String @NonNull ... segments) {
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
	public IoSupplier<InputStream> getResource(@NonNull PackType type, @NonNull Identifier id) {
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
	public void listResources(@NonNull PackType type, @NonNull String namespace, @NonNull String prefix, @NonNull ResourceOutput consumer) {
		this.lock();
		try {
			for (Identifier identifier : this.getSys(type).keySet()) {
				Supplier<byte[]> supplier = this.getSys(type).get(identifier);
				if (supplier == null) {
					LOGGER.warn("No resource found for {}", identifier);
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
	public @NonNull Set<String> getNamespaces(@NonNull PackType type) {
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
		LOGGER.info("closing pack {}", this.id);

		// lock
		this.lock();
		try {
			if (CONFIG.dumpOnClose) {
				this.dump();
			}
		} finally {
			// unlock
			this.waiting.unlock();
		}
	}

	private static byte[] toJsonBytes(JsonElement element) {
		return CONFIG.prettyJson ? JsonBytes.toPrettyBytes(element) : JsonBytes.toBytes(element);
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
			if (CONFIG.debugPerformance) {
				long start = System.currentTimeMillis();
				this.waiting.lock();
				long end = System.currentTimeMillis();
				LOGGER.warn("{}{}", "waited " + (end - start) + "ms for lock in Packwright: ", this.id);
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
				LOGGER.error("{}\"", "Packwright contains out-of-directory location! \"" + namespace + "/" + path);
			}

		} catch (IOException e) {
			throw new PackwrightException("Failed to write resource file: " + identifier.getNamespace() + "/" + identifier.getPath(), e);
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
