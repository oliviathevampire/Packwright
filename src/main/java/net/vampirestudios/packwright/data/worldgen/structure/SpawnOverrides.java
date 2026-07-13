package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A structure's {@code spawn_overrides}: per mob category ({@code monster},
 * {@code creature}, ...), which mobs spawn inside the structure.
 */
public class SpawnOverrides {
	public static final Codec<SpawnOverrides> CODEC =
			Codec.unboundedMap(Codec.STRING, Override.CODEC).xmap(map -> {
				SpawnOverrides out = new SpawnOverrides();
				out.byCategory.putAll(map);
				return out;
			}, overrides -> new LinkedHashMap<>(overrides.byCategory));

	private final Map<String, Override> byCategory = new LinkedHashMap<>();

	public static SpawnOverrides overrides() {
		return new SpawnOverrides();
	}

	/** no overrides: mobs spawn as the biome dictates */
	public static SpawnOverrides none() {
		return new SpawnOverrides();
	}

	/** override one mob category, e.g. {@code "monster"} */
	public SpawnOverrides override(String category, Override override) {
		this.byCategory.put(category, override);
		return this;
	}

	/** one category's override: bounding box mode plus the spawn list */
	public static class Override {
		public static final Codec<Override> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("bounding_box").forGetter(x -> x.boundingBox),
				Spawn.CODEC.listOf().fieldOf("spawns").orElse(List.of()).forGetter(x -> List.copyOf(x.spawns))
		).apply(i, (boundingBox, spawns) -> {
			Override out = new Override();
			out.boundingBox = boundingBox;
			out.spawns = new ArrayList<>(spawns);
			return out;
		}));

		private String boundingBox = "piece";
		private List<Spawn> spawns = new ArrayList<>();

		/** applies inside each structure piece's bounding box */
		public static Override piece() {
			return new Override();
		}

		/** applies inside the whole structure's bounding box */
		public static Override full() {
			Override out = new Override();
			out.boundingBox = "full";
			return out;
		}

		public Override spawn(Spawn spawn) {
			this.spawns.add(spawn);
			return this;
		}
	}

	/** one weighted spawn entry */
	public static class Spawn {
		public static final Codec<Spawn> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
				Codec.INT.fieldOf("weight").forGetter(x -> x.weight),
				Codec.INT.fieldOf("minCount").forGetter(x -> x.minCount),
				Codec.INT.fieldOf("maxCount").forGetter(x -> x.maxCount)
		).apply(i, Spawn::of));

		private Identifier type;
		private int weight = 1;
		private int minCount = 1;
		private int maxCount = 1;

		public static Spawn of(Identifier type, int weight, int minCount, int maxCount) {
			Spawn out = new Spawn();
			out.type = type;
			out.weight = weight;
			out.minCount = minCount;
			out.maxCount = maxCount;
			return out;
		}
	}
}
