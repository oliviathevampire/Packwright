package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.HeightProvider;
import net.vampirestudios.packwright.data.worldgen.VerticalAnchor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.vampirestudios.packwright.util.ResourceHelpers.vanillaId;

/**
 * A {@code worldgen/structure} file. The common fields ({@code type}, {@code biomes},
 * {@code step}, {@code spawn_overrides}) are typed; type-specific configuration is set
 * through the typed {@code property} setters and the jigsaw helpers.
 */
public class Structure {

	public static final Codec<Structure> CODEC = Codec.PASSTHROUGH.comapFlatMap(Structure::fromDynamic, Structure::toDynamic);

	private Identifier type;
	private Biomes biomes;
	private String step;
	private SpawnOverrides spawnOverrides;
	private final Map<String, Object> config = new LinkedHashMap<>();

	public static Structure structure() {
		return new Structure();
	}

	// Core setters

	public Structure type(Identifier type) { this.type = type; return this; }

	public Structure biomes(Biomes biomes) { this.biomes = biomes; return this; }

	public Structure biomesId(Identifier id) {
		this.biomes = id == null ? null : Biomes.biome(id);
		return this;
	}

	public Structure biomesTag(Identifier tag) {
		this.biomes = tag == null ? null : Biomes.tag(tag);
		return this;
	}

	public Structure step(String step) { this.step = step; return this; }

	public Structure spawnOverrides(SpawnOverrides overrides) {
		this.spawnOverrides = overrides;
		return this;
	}

	// Type-specific configuration

	public Structure property(String key, String value) {
		this.config.put(key, value);
		return this;
	}

	public Structure property(String key, Identifier value) {
		this.config.put(key, value == null ? null : value.toString());
		return this;
	}

	public Structure property(String key, int value) {
		this.config.put(key, value);
		return this;
	}

	public Structure property(String key, double value) {
		this.config.put(key, value);
		return this;
	}

	public Structure property(String key, boolean value) {
		this.config.put(key, value);
		return this;
	}

	public <T> Structure property(String key, Codec<T> codec, T value) {
		this.config.put(key, encode(codec, value));
		return this;
	}

	// Convenience helpers for jigsaw-like structures

	public static Structure jigsaw(String startPool) {
		return Structure.structure()
				.type(vanillaId("jigsaw"))
				.property("start_pool", startPool);
	}

	public Structure size(int size) {
		return property("size", size);
	}

	public Structure maxDistanceFromCenter(int v) {
		return property("max_distance_from_center", v);
	}

	/** absolute start height; the game requires a vertical anchor, not a bare int */
	public Structure startHeightInt(int v) {
		return startHeight(VerticalAnchor.absolute(v));
	}

	public Structure startHeight(VerticalAnchor anchor) {
		return property("start_height", VerticalAnchor.CODEC, anchor);
	}

	public Structure startHeight(HeightProvider provider) {
		return property("start_height", HeightProvider.CODEC, provider);
	}

	public Structure useExpansionHack(boolean v) {
		return property("use_expansion_hack", v);
	}

	public Structure projectStartToHeightmap(String heightmap) {
		return property("project_start_to_heightmap", heightmap);
	}

	public Structure terrainAdaptation(String adaptation) {
		return property("terrain_adaptation", adaptation);
	}

	// Codec plumbing (plain Java values, no JSON)

	private Dynamic<?> toDynamic() {
		Map<String, Object> out = new LinkedHashMap<>();
		if (this.type != null) out.put("type", this.type.toString());
		if (this.biomes != null) out.put("biomes", this.biomes.value());
		if (this.step != null) out.put("step", this.step);
		// spawn_overrides is required by the game; default to no overrides
		out.put("spawn_overrides", encode(SpawnOverrides.CODEC, this.spawnOverrides != null ? this.spawnOverrides : SpawnOverrides.none()));
		out.putAll(this.config);
		return new Dynamic<>(JavaOps.INSTANCE, out);
	}

	private static DataResult<Structure> fromDynamic(Dynamic<?> dynamic) {
		Object raw = dynamic.convert(JavaOps.INSTANCE).getValue();
		if (!(raw instanceof Map<?, ?> map)) {
			return DataResult.error(() -> "Structure must be an object");
		}
		Structure structure = new Structure();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			String key = String.valueOf(entry.getKey());
			Object value = entry.getValue();
			switch (key) {
				case "type" -> structure.type = Identifier.tryParse(String.valueOf(value));
				case "biomes" -> structure.biomes = Biomes.fromValue(value);
				case "step" -> structure.step = String.valueOf(value);
				case "spawn_overrides" -> structure.spawnOverrides = SpawnOverrides.CODEC.parse(JavaOps.INSTANCE, value).result().orElse(null);
				default -> structure.config.put(key, value);
			}
		}
		return DataResult.success(structure);
	}

	private static <T> Object encode(Codec<T> codec, T value) {
		return codec.encodeStart(JavaOps.INSTANCE, value).getOrThrow();
	}

	/** the {@code biomes} field: a single biome id, a {@code #tag}, or a list of ids */
	public static final class Biomes {
		private final Object value; // String or List<String>

		private Biomes(Object value) {
			this.value = value;
		}

		public static Biomes biome(Identifier id) {
			return new Biomes(id.toString());
		}

		public static Biomes tag(Identifier tag) {
			return new Biomes("#" + tag);
		}

		public static Biomes list(List<Identifier> ids) {
			return new Biomes(ids.stream().map(Identifier::toString).toList());
		}

		Object value() {
			return this.value;
		}

		static Biomes fromValue(Object value) {
			if (value instanceof String string) return new Biomes(string);
			if (value instanceof List<?> list) return new Biomes(list.stream().map(String::valueOf).toList());
			return null;
		}
	}
}
