package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public final class JFeatureConfigs {
	private JFeatureConfigs() {
	}

	public static <T> DataResult<FeatureConfig> decode(String type, DynamicOps<T> ops, T input) {
		return switch (FeatureConfigUtil.normalizeType(type)) {
			case "simple_block" -> SimpleBlockConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "ore" -> OreConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "random_patch", "flower" -> RandomPatchConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "tree" -> SimpleTreeConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "disk" -> DiskConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "spring_feature" -> SpringConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "random_selector" -> RandomSelectorConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "simple_random_selector" -> SimpleRandomSelectorConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "random_boolean_selector" -> RandomBooleanSelectorConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "block_pile" -> BlockPileConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "huge_fungus" -> HugeFungusConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "lake" -> LakeConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "iceberg" -> BlockStateConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "replace_single_block" -> ReplaceBlockConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "replace_blobs" -> ReplaceSphereConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "huge_red_mushroom", "huge_brown_mushroom" -> HugeMushroomConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "nether_forest_vegetation" -> NetherForestVegetationConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "delta_feature" -> DeltaConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "basalt_columns" -> ColumnConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "fill_layer" -> LayerConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "sea_pickle" -> CountConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "bamboo" -> ProbabilityConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			case "underwater_magma" -> UnderwaterMagmaConfig.CODEC.decode(ops, input).map(Pair::getFirst);
			default -> DataResult.error(() -> "Unsupported configured feature type: " + type);
		};
	}

	public static <T> DataResult<T> encode(FeatureConfig config, DynamicOps<T> ops) {
		if (config instanceof SimpleBlockConfig simpleBlock) return SimpleBlockConfig.CODEC.encodeStart(ops, simpleBlock);
		if (config instanceof OreConfig ore) return OreConfig.CODEC.encodeStart(ops, ore);
		if (config instanceof RandomPatchConfig randomPatch) return RandomPatchConfig.CODEC.encodeStart(ops, randomPatch);
		if (config instanceof SimpleTreeConfig tree) return SimpleTreeConfig.CODEC.encodeStart(ops, tree);
		if (config instanceof DiskConfig disk) return DiskConfig.CODEC.encodeStart(ops, disk);
		if (config instanceof SpringConfig spring) return SpringConfig.CODEC.encodeStart(ops, spring);
		if (config instanceof RandomSelectorConfig randomSelector) return RandomSelectorConfig.CODEC.encodeStart(ops, randomSelector);
		if (config instanceof SimpleRandomSelectorConfig simpleRandomSelector) return SimpleRandomSelectorConfig.CODEC.encodeStart(ops, simpleRandomSelector);
		if (config instanceof RandomBooleanSelectorConfig randomBooleanSelector) return RandomBooleanSelectorConfig.CODEC.encodeStart(ops, randomBooleanSelector);
		if (config instanceof BlockPileConfig blockPile) return BlockPileConfig.CODEC.encodeStart(ops, blockPile);
		if (config instanceof HugeFungusConfig hugeFungus) return HugeFungusConfig.CODEC.encodeStart(ops, hugeFungus);
		if (config instanceof LakeConfig lake) return LakeConfig.CODEC.encodeStart(ops, lake);
		if (config instanceof BlockStateConfig blockState) return BlockStateConfig.CODEC.encodeStart(ops, blockState);
		if (config instanceof ReplaceBlockConfig replaceBlock) return ReplaceBlockConfig.CODEC.encodeStart(ops, replaceBlock);
		if (config instanceof ReplaceSphereConfig replaceSphere) return ReplaceSphereConfig.CODEC.encodeStart(ops, replaceSphere);
		if (config instanceof HugeMushroomConfig hugeMushroom) return HugeMushroomConfig.CODEC.encodeStart(ops, hugeMushroom);
		if (config instanceof NetherForestVegetationConfig netherForestVegetation) return NetherForestVegetationConfig.CODEC.encodeStart(ops, netherForestVegetation);
		if (config instanceof DeltaConfig delta) return DeltaConfig.CODEC.encodeStart(ops, delta);
		if (config instanceof ColumnConfig column) return ColumnConfig.CODEC.encodeStart(ops, column);
		if (config instanceof LayerConfig layer) return LayerConfig.CODEC.encodeStart(ops, layer);
		if (config instanceof CountConfig count) return CountConfig.CODEC.encodeStart(ops, count);
		if (config instanceof ProbabilityConfig probability) return ProbabilityConfig.CODEC.encodeStart(ops, probability);
		if (config instanceof UnderwaterMagmaConfig underwaterMagma) return UnderwaterMagmaConfig.CODEC.encodeStart(ops, underwaterMagma);
		return DataResult.error(() -> "Unsupported configured feature config: " + config.getClass().getSimpleName());
	}
}
