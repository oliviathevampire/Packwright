package net.vampirestudios.packwright.data.worldgen.feature;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.feature.builders.*;
import net.vampirestudios.packwright.data.worldgen.feature.builders.geode.GeodeFeatureBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.foliage.TreeFoliagePlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.size.TreeFeatureSize;
import net.vampirestudios.packwright.data.worldgen.feature.trunk.TreeTrunkPlacer;

public final class Features {
	private Features() {
	}

	public static FeatureBuilder feature(String type) {
		return new FeatureBuilder(type);
	}

	public static SimpleBlockFeatureBuilder simpleBlock(Identifier block) {
		return new SimpleBlockFeatureBuilder().toPlace(block);
	}

	public static OreFeatureBuilder ore(Identifier replaceableTag, Identifier block, int size) {
		return new OreFeatureBuilder().targetTag(replaceableTag, block).size(size);
	}

	/** an ore whose target is a single block instead of a tag */
	public static OreFeatureBuilder oreInBlock(Identifier replaceableBlock, Identifier block, int size) {
		return new OreFeatureBuilder().targetBlock(replaceableBlock, block).size(size);
	}

	/**
	 * @deprecated the {@code random_patch} feature type was removed from the game in 26.1;
	 * use {@link #projectedRandomPatchySquare(Identifier)} for scattered vegetation
	 */
	@Deprecated
	public static RandomPatchFeatureBuilder randomPatch(PlacedFeature feature) {
		return new RandomPatchFeatureBuilder().feature(feature);
	}

	public static TreeFeatureBuilder simpleTree(Identifier trunkBlock, Identifier foliageBlock) {
		return simpleTree(trunkBlock, foliageBlock, Identifier.withDefaultNamespace("dirt"));
	}

	public static TreeFeatureBuilder simpleTree(Identifier trunkBlock, Identifier foliageBlock, Identifier dirtBlock) {
		return new TreeFeatureBuilder()
				.trunkBlock(trunkBlock)
				.foliageBlock(foliageBlock)
				.belowTrunkProvider(BlockStateProvider.simple(dirtBlock))
				.straightTrunk(4, 2, 0)
				.blobFoliage(IntProvider.constant(2), IntProvider.constant(0), 3)
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 1));
	}

	public static TreeFeatureBuilder poplarTree(Identifier trunkBlock, Identifier foliageBlock) {
		return poplarTree(trunkBlock, foliageBlock, Identifier.withDefaultNamespace("dirt"));
	}

	public static TreeFeatureBuilder poplarTree(Identifier trunkBlock, Identifier foliageBlock, Identifier dirtBlock) {
		return new TreeFeatureBuilder()
				.trunkBlock(trunkBlock)
				.foliageBlock(foliageBlock)
				.belowTrunkProvider(BlockStateProvider.simple(dirtBlock))
				.trunkPlacer(TreeTrunkPlacer.poplar(7, 4, 0, IntProvider.uniform(0, 8), IntProvider.uniform(1, 4)))
				.foliagePlacer(TreeFoliagePlacer.poplar(IntProvider.constant(0), IntProvider.constant(0), IntProvider.uniform(5, 16), 0.25F))
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 1))
				.shelfMushrooms(0.1F);
	}

	public static DiskFeatureBuilder disk(Identifier block, Identifier targetBlock, int radius) {
		return new DiskFeatureBuilder().stateProvider(block).targetBlock(targetBlock).radius(radius);
	}

	public static SpringFeatureBuilder spring(Identifier fluidBlock) {
		return new SpringFeatureBuilder().state(fluidBlock);
	}

	public static RandomSelectorFeatureBuilder randomSelector() {
		return new RandomSelectorFeatureBuilder();
	}

	public static BlockPileFeatureBuilder blockPile(Identifier block) {
		return new BlockPileFeatureBuilder().stateProvider(block);
	}

	public static LakeFeatureBuilder lake(Identifier fluid, Identifier barrier) {
		return new LakeFeatureBuilder().fluid(fluid).barrier(barrier);
	}

	public static HugeFungusFeatureBuilder hugeFungus() {
		return new HugeFungusFeatureBuilder();
	}

	public static StateFeatureBuilder iceberg(Identifier block) {
		return new StateFeatureBuilder("minecraft:iceberg").state(block);
	}

	public static ReplaceSingleBlockFeatureBuilder replaceSingleBlock() {
		return new ReplaceSingleBlockFeatureBuilder();
	}

	/**
	 * @deprecated the {@code replace_blobs} feature type no longer exists in the game;
	 * use {@link #oreInBlock(Identifier, Identifier, int)} for similar block replacement
	 */
	@Deprecated
	public static ReplaceBlobsFeatureBuilder replaceBlobs(Identifier target, Identifier state, int radius) {
		return new ReplaceBlobsFeatureBuilder().target(target).state(state).radius(radius);
	}

	public static HugeMushroomFeatureBuilder hugeRedMushroom(Identifier capBlock, Identifier stemBlock) {
		return new HugeMushroomFeatureBuilder("minecraft:huge_red_mushroom").capProvider(capBlock).stemProvider(stemBlock);
	}

	public static HugeMushroomFeatureBuilder hugeBrownMushroom(Identifier capBlock, Identifier stemBlock) {
		return new HugeMushroomFeatureBuilder("minecraft:huge_brown_mushroom").capProvider(capBlock).stemProvider(stemBlock);
	}

	public static NetherForestVegetationFeatureBuilder netherForestVegetation(Identifier block) {
		return new NetherForestVegetationFeatureBuilder().stateProvider(block);
	}

	public static DeltaFeatureBuilder deltaFeature(Identifier contents, Identifier rim) {
		return new DeltaFeatureBuilder().contents(contents).rim(rim);
	}

	public static SteppedColumnClusterFeatureBuilder steppedColumnCluster(Identifier block) {
		return new SteppedColumnClusterFeatureBuilder().block(block);
	}

	public static SingleBlockPillarFeatureBuilder singleBlockPillar(Identifier block) {
		return new SingleBlockPillarFeatureBuilder().block(block);
	}

	public static RandomNeighborSpreadFeatureBuilder randomNeighborSpread(Identifier block) {
		return new RandomNeighborSpreadFeatureBuilder().block(block);
	}

	public static OverlayFeatureBuilder overlay(Identifier... features) {
		return new OverlayFeatureBuilder().features(features);
	}

	public static ProjectedRandomPatchySquareFeatureBuilder projectedRandomPatchySquare(Identifier block) {
		return new ProjectedRandomPatchySquareFeatureBuilder().block(block);
	}

	public static FillLayerFeatureBuilder fillLayer(int height, Identifier block) {
		return new FillLayerFeatureBuilder().height(height).state(block);
	}

	public static CountFeatureBuilder seaPickle(IntProvider count) {
		return new CountFeatureBuilder("minecraft:sea_pickle").count(count);
	}

	public static ProbabilityFeatureBuilder bamboo(float probability) {
		return new ProbabilityFeatureBuilder("minecraft:bamboo").probability(probability);
	}

	public static UnderwaterMagmaFeatureBuilder underwaterMagma(int floorSearchRange, int placementRadiusAroundFloor, float probability) {
		return new UnderwaterMagmaFeatureBuilder()
				.floorSearchRange(floorSearchRange)
				.placementRadiusAroundFloor(placementRadiusAroundFloor)
				.placementProbabilityPerValidPosition(probability);
	}

	public static EndPodiumFeatureBuilder endPodium(boolean active) {
		return new EndPodiumFeatureBuilder().active(active);
	}

	public static FeatureListFeatureBuilder sequence() {
		return new FeatureListFeatureBuilder("minecraft:sequence");
	}

	public static FeatureListFeatureBuilder simpleRandomSelector() {
		return new FeatureListFeatureBuilder("minecraft:simple_random_selector");
	}

	public static FeatureListFeatureBuilder randomBooleanSelector(PlacedFeature featureTrue, PlacedFeature featureFalse) {
		return new FeatureListFeatureBuilder("minecraft:random_boolean_selector")
				.featureTrue(featureTrue)
				.featureFalse(featureFalse);
	}

	public static BlockColumnFeatureBuilder blockColumn() {
		return new BlockColumnFeatureBuilder();
	}

	public static VegetationPatchFeatureBuilder vegetationPatch() {
		return new VegetationPatchFeatureBuilder("minecraft:vegetation_patch");
	}

	public static VegetationPatchFeatureBuilder waterloggedVegetationPatch() {
		return new VegetationPatchFeatureBuilder("minecraft:waterlogged_vegetation_patch");
	}

	public static RootSystemFeatureBuilder rootSystem() {
		return new RootSystemFeatureBuilder();
	}

	public static SculkPatchFeatureBuilder sculkPatch() {
		return new SculkPatchFeatureBuilder();
	}

	public static DripstoneClusterFeatureBuilder dripstoneCluster() {
		return new DripstoneClusterFeatureBuilder();
	}

	public static LargeDripstoneFeatureBuilder largeDripstone() {
		return new LargeDripstoneFeatureBuilder();
	}

	public static GeodeFeatureBuilder geode() {
		return new GeodeFeatureBuilder();
	}

	public static EndGatewayFeatureBuilder endGateway() {
		return new EndGatewayFeatureBuilder();
	}

	public static StateFeatureBuilder forestRock(Identifier block) {
		return new StateFeatureBuilder("minecraft:forest_rock").state(block);
	}

	public static StateFeatureBuilder blockBlob(Identifier block) {
		return new StateFeatureBuilder("minecraft:block_blob").state(block);
	}

	public static StateFeatureBuilder iceSpike(Identifier block) {
		return new StateFeatureBuilder("minecraft:ice_spike").state(block);
	}

	public static NoConfigFeatureBuilder noConfig(String type) {
		return new NoConfigFeatureBuilder(type);
	}

	public static NoConfigFeatureBuilder noOp() { return noConfig("minecraft:no_op"); }
	public static NoConfigFeatureBuilder monsterRoom() { return noConfig("minecraft:monster_room"); }
	public static NoConfigFeatureBuilder desertWell() { return noConfig("minecraft:desert_well"); }
	public static NoConfigFeatureBuilder fossil() { return noConfig("minecraft:fossil"); }
	public static NoConfigFeatureBuilder blueIce() { return noConfig("minecraft:blue_ice"); }
	public static NoConfigFeatureBuilder icePatch() { return noConfig("minecraft:ice_patch"); }
	public static NoConfigFeatureBuilder freezeTopLayer() { return noConfig("minecraft:freeze_top_layer"); }
	public static NoConfigFeatureBuilder vines() { return noConfig("minecraft:vines"); }
	public static NoConfigFeatureBuilder voidStartPlatform() { return noConfig("minecraft:void_start_platform"); }
	public static NoConfigFeatureBuilder endIsland() { return noConfig("minecraft:end_island"); }
	public static NoConfigFeatureBuilder endSpike() { return noConfig("minecraft:end_spike"); }
	public static NoConfigFeatureBuilder bonusChest() { return noConfig("minecraft:bonus_chest"); }
	public static NoConfigFeatureBuilder kelp() { return noConfig("minecraft:kelp"); }
	public static NoConfigFeatureBuilder seagrass() { return noConfig("minecraft:seagrass"); }
	public static NoConfigFeatureBuilder coralTree() { return noConfig("minecraft:coral_tree"); }
	public static NoConfigFeatureBuilder coralMushroom() { return noConfig("minecraft:coral_mushroom"); }
	public static NoConfigFeatureBuilder coralClaw() { return noConfig("minecraft:coral_claw"); }
	public static NoConfigFeatureBuilder weepingVines() { return noConfig("minecraft:weeping_vines"); }
	public static NoConfigFeatureBuilder twistingVines() { return noConfig("minecraft:twisting_vines"); }
	public static NoConfigFeatureBuilder glowLichen() { return noConfig("minecraft:glow_lichen"); }
	public static NoConfigFeatureBuilder pointedDripstone() { return noConfig("minecraft:pointed_dripstone"); }
}
