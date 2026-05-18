package net.vampirestudios.arrp.data.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.config.BlockPileConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.BlockStateConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.ColumnConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.CountConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.DeltaConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.DiskConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.FeatureConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.HugeFungusConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.HugeMushroomConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.FeatureConfigs;
import net.vampirestudios.arrp.data.worldgen.feature.config.LakeConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.LayerConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.NetherForestVegetationConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.OreConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.ProbabilityConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.RandomBooleanSelectorConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.RandomPatchConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.RandomSelectorConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.ReplaceBlockConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.ReplaceSphereConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.SimpleBlockConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.SimpleRandomSelectorConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.SimpleTreeConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.SpringConfig;
import net.vampirestudios.arrp.data.worldgen.feature.config.UnderwaterMagmaConfig;
import net.minecraft.resources.Identifier;

public class ConfiguredFeature {
	public static final Codec<ConfiguredFeature> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<ConfiguredFeature, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String type = string(map, ops, "type", null);
				T configInput = map.get("config");
				if (type == null) return DataResult.error(() -> "Configured feature is missing type");
				if (configInput == null) return DataResult.error(() -> "Configured feature is missing config");
				return FeatureConfigs.decode(type, ops, configInput)
						.map(config -> Pair.of(new ConfiguredFeature().type(type).config(config), input));
			});
		}

		@Override
		public <T> DataResult<T> encode(ConfiguredFeature feature, DynamicOps<T> ops, T prefix) {
			RecordBuilder<T> builder = ops.mapBuilder();
			if (feature.type != null) builder.add("type", ops.createString(feature.type));
			if (feature.config != null) builder.add("config", FeatureConfigs.encode(feature.config, ops));
			return builder.build(prefix);
		}
	};

	private String type = "minecraft:simple_block";
	private FeatureConfig config = SimpleBlockConfig.simpleBlock(Identifier.tryParse("minecraft:stone"));

	public static ConfiguredFeature feature() {
		return new ConfiguredFeature();
	}

	public static ConfiguredFeature simpleBlock(SimpleBlockConfig config) {
		return feature().type("minecraft:simple_block").config(config);
	}

	public static ConfiguredFeature ore(OreConfig config) {
		return feature().type("minecraft:ore").config(config);
	}

	public static ConfiguredFeature randomPatch(RandomPatchConfig config) {
		return feature().type("minecraft:random_patch").config(config);
	}

	public static ConfiguredFeature tree(SimpleTreeConfig config) {
		return feature().type("minecraft:tree").config(config);
	}

	public static ConfiguredFeature disk(DiskConfig config) {
		return feature().type("minecraft:disk").config(config);
	}

	public static ConfiguredFeature spring(SpringConfig config) {
		return feature().type("minecraft:spring_feature").config(config);
	}

	public static ConfiguredFeature randomSelector(RandomSelectorConfig config) {
		return feature().type("minecraft:random_selector").config(config);
	}

	public static ConfiguredFeature simpleRandomSelector(SimpleRandomSelectorConfig config) {
		return feature().type("minecraft:simple_random_selector").config(config);
	}

	public static ConfiguredFeature randomBooleanSelector(RandomBooleanSelectorConfig config) {
		return feature().type("minecraft:random_boolean_selector").config(config);
	}

	public static ConfiguredFeature blockPile(BlockPileConfig config) {
		return feature().type("minecraft:block_pile").config(config);
	}

	public static ConfiguredFeature hugeFungus(HugeFungusConfig config) {
		return feature().type("minecraft:huge_fungus").config(config);
	}

	public static ConfiguredFeature lake(LakeConfig config) {
		return feature().type("minecraft:lake").config(config);
	}

	public static ConfiguredFeature iceberg(BlockStateConfig config) {
		return feature().type("minecraft:iceberg").config(config);
	}

	public static ConfiguredFeature replaceSingleBlock(ReplaceBlockConfig config) {
		return feature().type("minecraft:replace_single_block").config(config);
	}

	public static ConfiguredFeature replaceBlobs(ReplaceSphereConfig config) {
		return feature().type("minecraft:replace_blobs").config(config);
	}

	public static ConfiguredFeature hugeRedMushroom(HugeMushroomConfig config) {
		return feature().type("minecraft:huge_red_mushroom").config(config);
	}

	public static ConfiguredFeature hugeBrownMushroom(HugeMushroomConfig config) {
		return feature().type("minecraft:huge_brown_mushroom").config(config);
	}

	public static ConfiguredFeature netherForestVegetation(NetherForestVegetationConfig config) {
		return feature().type("minecraft:nether_forest_vegetation").config(config);
	}

	public static ConfiguredFeature deltaFeature(DeltaConfig config) {
		return feature().type("minecraft:delta_feature").config(config);
	}

	public static ConfiguredFeature basaltColumns(ColumnConfig config) {
		return feature().type("minecraft:basalt_columns").config(config);
	}

	public static ConfiguredFeature fillLayer(LayerConfig config) {
		return feature().type("minecraft:fill_layer").config(config);
	}

	public static ConfiguredFeature seaPickle(CountConfig config) {
		return feature().type("minecraft:sea_pickle").config(config);
	}

	public static ConfiguredFeature bamboo(ProbabilityConfig config) {
		return feature().type("minecraft:bamboo").config(config);
	}

	public static ConfiguredFeature underwaterMagma(UnderwaterMagmaConfig config) {
		return feature().type("minecraft:underwater_magma").config(config);
	}

	private ConfiguredFeature type(String type) {
		this.type = type;
		return this;
	}

	private ConfiguredFeature config(FeatureConfig config) {
		this.config = config;
		return this;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
