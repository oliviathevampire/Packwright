package net.vampirestudios.arrp.json.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import net.vampirestudios.arrp.json.worldgen.feature.config.BlockPileConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.BlockStateConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.ColumnConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.CountConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.DeltaConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.DiskConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.FeatureConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.HugeFungusConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.HugeMushroomConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.JFeatureConfigs;
import net.vampirestudios.arrp.json.worldgen.feature.config.LakeConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.LayerConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.NetherForestVegetationConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.OreConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.ProbabilityConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.RandomBooleanSelectorConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.RandomPatchConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.RandomSelectorConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.ReplaceBlockConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.ReplaceSphereConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.SimpleBlockConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.SimpleRandomSelectorConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.SimpleTreeConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.SpringConfig;
import net.vampirestudios.arrp.json.worldgen.feature.config.UnderwaterMagmaConfig;

public class JConfiguredFeature {
	public static final Codec<JConfiguredFeature> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<JConfiguredFeature, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String type = string(map, ops, "type", null);
				T configInput = map.get("config");
				if (type == null) return DataResult.error(() -> "Configured feature is missing type");
				if (configInput == null) return DataResult.error(() -> "Configured feature is missing config");
				return JFeatureConfigs.decode(type, ops, configInput)
						.map(config -> Pair.of(new JConfiguredFeature().type(type).config(config), input));
			});
		}

		@Override
		public <T> DataResult<T> encode(JConfiguredFeature feature, DynamicOps<T> ops, T prefix) {
			RecordBuilder<T> builder = ops.mapBuilder();
			if (feature.type != null) builder.add("type", ops.createString(feature.type));
			if (feature.config != null) builder.add("config", JFeatureConfigs.encode(feature.config, ops));
			return builder.build(prefix);
		}
	};

	private String type = "minecraft:simple_block";
	private FeatureConfig config = SimpleBlockConfig.simpleBlock("minecraft:stone");

	public static JConfiguredFeature feature() {
		return new JConfiguredFeature();
	}

	public static JConfiguredFeature simpleBlock(SimpleBlockConfig config) {
		return feature().type("minecraft:simple_block").config(config);
	}

	public static JConfiguredFeature ore(OreConfig config) {
		return feature().type("minecraft:ore").config(config);
	}

	public static JConfiguredFeature randomPatch(RandomPatchConfig config) {
		return feature().type("minecraft:random_patch").config(config);
	}

	public static JConfiguredFeature tree(SimpleTreeConfig config) {
		return feature().type("minecraft:tree").config(config);
	}

	public static JConfiguredFeature disk(DiskConfig config) {
		return feature().type("minecraft:disk").config(config);
	}

	public static JConfiguredFeature spring(SpringConfig config) {
		return feature().type("minecraft:spring_feature").config(config);
	}

	public static JConfiguredFeature randomSelector(RandomSelectorConfig config) {
		return feature().type("minecraft:random_selector").config(config);
	}

	public static JConfiguredFeature simpleRandomSelector(SimpleRandomSelectorConfig config) {
		return feature().type("minecraft:simple_random_selector").config(config);
	}

	public static JConfiguredFeature randomBooleanSelector(RandomBooleanSelectorConfig config) {
		return feature().type("minecraft:random_boolean_selector").config(config);
	}

	public static JConfiguredFeature blockPile(BlockPileConfig config) {
		return feature().type("minecraft:block_pile").config(config);
	}

	public static JConfiguredFeature hugeFungus(HugeFungusConfig config) {
		return feature().type("minecraft:huge_fungus").config(config);
	}

	public static JConfiguredFeature lake(LakeConfig config) {
		return feature().type("minecraft:lake").config(config);
	}

	public static JConfiguredFeature iceberg(BlockStateConfig config) {
		return feature().type("minecraft:iceberg").config(config);
	}

	public static JConfiguredFeature replaceSingleBlock(ReplaceBlockConfig config) {
		return feature().type("minecraft:replace_single_block").config(config);
	}

	public static JConfiguredFeature replaceBlobs(ReplaceSphereConfig config) {
		return feature().type("minecraft:replace_blobs").config(config);
	}

	public static JConfiguredFeature hugeRedMushroom(HugeMushroomConfig config) {
		return feature().type("minecraft:huge_red_mushroom").config(config);
	}

	public static JConfiguredFeature hugeBrownMushroom(HugeMushroomConfig config) {
		return feature().type("minecraft:huge_brown_mushroom").config(config);
	}

	public static JConfiguredFeature netherForestVegetation(NetherForestVegetationConfig config) {
		return feature().type("minecraft:nether_forest_vegetation").config(config);
	}

	public static JConfiguredFeature deltaFeature(DeltaConfig config) {
		return feature().type("minecraft:delta_feature").config(config);
	}

	public static JConfiguredFeature basaltColumns(ColumnConfig config) {
		return feature().type("minecraft:basalt_columns").config(config);
	}

	public static JConfiguredFeature fillLayer(LayerConfig config) {
		return feature().type("minecraft:fill_layer").config(config);
	}

	public static JConfiguredFeature seaPickle(CountConfig config) {
		return feature().type("minecraft:sea_pickle").config(config);
	}

	public static JConfiguredFeature bamboo(ProbabilityConfig config) {
		return feature().type("minecraft:bamboo").config(config);
	}

	public static JConfiguredFeature underwaterMagma(UnderwaterMagmaConfig config) {
		return feature().type("minecraft:underwater_magma").config(config);
	}

	private JConfiguredFeature type(String type) {
		this.type = type;
		return this;
	}

	private JConfiguredFeature config(FeatureConfig config) {
		this.config = config;
		return this;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
