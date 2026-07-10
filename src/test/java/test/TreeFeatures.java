package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.arrp.data.worldgen.feature.Feature;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.data.worldgen.feature.builders.FallenTreeFeatureBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.builders.HugeFungusFeatureBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.builders.HugeMushroomFeatureBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.builders.TreeFeatureBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.foliage.TreeFoliagePlacer;
import net.vampirestudios.arrp.data.worldgen.feature.tree.decorator.TreeDecorator;
import net.vampirestudios.arrp.data.worldgen.feature.tree.root.AboveRootPlacement;
import net.vampirestudios.arrp.data.worldgen.feature.tree.root.MangroveRootPlacement;
import net.vampirestudios.arrp.data.worldgen.feature.tree.root.MangroveRootPlacer;
import net.vampirestudios.arrp.data.worldgen.feature.tree.size.TreeFeatureSize;
import net.vampirestudios.arrp.data.worldgen.feature.trunk.TreeTrunkPlacer;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.util.VanillaIds;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TreeFeatures {
	private static final BlockStateProvider BELOW_TRUNK = BlockStateProvider.simple(VanillaIds.DIRT);

	public static Map<String, Feature> bootstrap() {
		Map<String, Feature> features = new LinkedHashMap<>();
		TreeDecorator beehive = TreeDecorator.beehive(1.0F);
		TreeDecorator beehive0002 = TreeDecorator.beehive(0.002F);
		TreeDecorator beehive001 = TreeDecorator.beehive(0.01F);
		TreeDecorator beehive002 = TreeDecorator.beehive(0.02F);
		TreeDecorator beehive005 = TreeDecorator.beehive(0.05F);
		TreeDecorator sparseLeafLitter = TreeDecorator.placeOnGround(1, 1, 1, leafLitter(1, 2));
		TreeDecorator thickLeafLitter = TreeDecorator.placeOnGround(2, 2, 2, leafLitter(2, 4));

		features.put("crimson_fungus", crimsonFungus(false));
		features.put("crimson_fungus_planted", crimsonFungus(true));
		features.put("warped_fungus", warpedFungus(false));
		features.put("warped_fungus_planted", warpedFungus(true));
		features.put("huge_brown_mushroom", hugeMushroom("minecraft:huge_brown_mushroom", VanillaIds.BROWN_MUSHROOM_BLOCK, 3));
		features.put("huge_red_mushroom", hugeMushroom("minecraft:huge_red_mushroom", VanillaIds.RED_MUSHROOM_BLOCK, 2));
		features.put("oak", createOak().build());
		features.put("dark_oak", createDarkOak(VanillaIds.DARK_OAK_LOG, VanillaIds.DARK_OAK_LEAVES).build());
		features.put("pale_oak", createDarkOak(VanillaIds.PALE_OAK_LOG, VanillaIds.PALE_OAK_LEAVES).decorator(TreeDecorator.paleMoss(0.8F, 0.2F, 0.1F)).build());
		features.put("pale_oak_bonemeal", createDarkOak(VanillaIds.PALE_OAK_LOG, VanillaIds.PALE_OAK_LEAVES).decorator(TreeDecorator.paleMoss(0.8F, 0.2F, 0.1F)).build());
		features.put("pale_oak_creaking", createDarkOak(VanillaIds.PALE_OAK_LOG, VanillaIds.PALE_OAK_LEAVES).decorators(List.of(TreeDecorator.paleMoss(0.8F, 0.2F, 0.1F), TreeDecorator.creakingHeart(1.0F))).build());
		features.put("birch", createBirch().build());
		features.put("acacia", acacia().build());
		features.put("cherry", cherry().build());
		features.put("spruce", spruce().build());
		features.put("pine", pine().build());
		features.put("jungle_tree", createJungleTree().decorators(List.of(TreeDecorator.cocoa(0.2F), TreeDecorator.trunkVine(), TreeDecorator.leaveVine(0.25F))).ignoreVines().build());
		features.put("fancy_oak", createFancyOak().build());
		features.put("jungle_tree_no_vine", createJungleTree().ignoreVines().build());
		features.put("mega_jungle_tree", megaJungle().build());
		features.put("mega_spruce", megaSpruce(IntProvider.uniform(13, 17)).build());
		features.put("mega_pine", megaSpruce(IntProvider.uniform(3, 7)).build());
		features.put("super_birch_bees_0002", createSuperBirch().decorators(List.of(beehive0002, TreeDecorator.shelfMushroom(0.3F))).build());
		features.put("super_birch_bees", createSuperBirch().decorators(List.of(beehive, TreeDecorator.shelfMushroom(0.3F))).build());
		features.put("swamp_oak", createStraightBlobTree(VanillaIds.OAK_LOG, VanillaIds.OAK_LEAVES, 5, 3, 0, 3).decorator(TreeDecorator.leaveVine(0.25F)).build());
		features.put("peat_bog_oak", createStraightBlobTree(VanillaIds.OAK_LOG, VanillaIds.OAK_LEAVES, 5, 3, 0, 3).build());
		features.put("jungle_bush", bush(VanillaIds.JUNGLE_LOG, VanillaIds.OAK_LEAVES).build());
		features.put("spruce_bush", bush(VanillaIds.SPRUCE_LOG, VanillaIds.SPRUCE_LEAVES).build());
		features.put("azalea_tree", azalea().build());
		features.put("mangrove", mangrove(2, 4, IntProvider.uniform(1, 4), IntProvider.uniform(1, 3), 2, beehive001).build());
		features.put("tall_mangrove", mangrove(4, 9, IntProvider.uniform(1, 6), IntProvider.uniform(3, 7), 3, beehive001).build());
		features.put("oak_bees_0002_leaf_litter", createOak().decorators(List.of(beehive0002, sparseLeafLitter, thickLeafLitter)).build());
		features.put("oak_bees_002", createOak().decorator(beehive002).build());
		features.put("oak_bees_005", createOak().decorator(beehive005).build());
		features.put("birch_bees_0002", createBirch().decorator(beehive0002).build());
		features.put("birch_bees_0002_leaf_litter", createBirch().decorators(List.of(beehive0002, sparseLeafLitter, thickLeafLitter)).build());
		features.put("birch_bees_002", createBirch().decorator(beehive002).build());
		features.put("birch_bees_005", createBirch().decorator(beehive005).build());
		features.put("fancy_oak_bees_0002_leaf_litter", createFancyOak().decorators(List.of(beehive0002, sparseLeafLitter, thickLeafLitter)).build());
		features.put("fancy_oak_bees_002", createFancyOak().decorator(beehive002).build());
		features.put("fancy_oak_bees_005", createFancyOak().decorator(beehive005).build());
		features.put("fancy_oak_bees", createFancyOak().decorator(beehive).build());
		features.put("cherry_bees_005", cherry().decorator(beehive005).build());
		features.put("oak_leaf_litter", createOak().decorators(List.of(sparseLeafLitter, thickLeafLitter)).build());
		features.put("dark_oak_leaf_litter", createDarkOak(VanillaIds.DARK_OAK_LOG, VanillaIds.DARK_OAK_LEAVES).ignoreVines().decorators(List.of(sparseLeafLitter, thickLeafLitter)).build());
		features.put("birch_leaf_litter", createBirch().decorators(List.of(sparseLeafLitter, thickLeafLitter)).build());
		features.put("fancy_oak_leaf_litter", createFancyOak().decorators(List.of(sparseLeafLitter, thickLeafLitter)).build());
		features.put("red_poplar", createPoplar(VanillaIds.RED_POPLAR_LEAVES).decorator(TreeDecorator.shelfMushroom(0.4F)).build());
		features.put("orange_poplar", createPoplar(VanillaIds.ORANGE_POPLAR_LEAVES).decorator(TreeDecorator.shelfMushroom(0.4F)).build());
		features.put("yellow_poplar", createPoplar(VanillaIds.YELLOW_POPLAR_LEAVES).decorator(TreeDecorator.shelfMushroom(0.4F)).build());
		features.put("large_red_poplar", createLargePoplar(VanillaIds.RED_POPLAR_LEAVES).decorator(TreeDecorator.shelfMushroom(0.6F)).build());
		features.put("large_orange_poplar", createLargePoplar(VanillaIds.ORANGE_POPLAR_LEAVES).decorator(TreeDecorator.shelfMushroom(0.6F)).build());
		features.put("large_yellow_poplar", createLargePoplar(VanillaIds.YELLOW_POPLAR_LEAVES).decorator(TreeDecorator.shelfMushroom(0.6F)).build());
		features.put("red_poplar_leaf_litter", createPoplar(VanillaIds.RED_POPLAR_LEAVES).decorators(List.of(sparseLeafLitter, thickLeafLitter, TreeDecorator.shelfMushroom(0.4F))).build());
		features.put("orange_poplar_leaf_litter", createPoplar(VanillaIds.ORANGE_POPLAR_LEAVES).decorators(List.of(sparseLeafLitter, thickLeafLitter, TreeDecorator.shelfMushroom(0.4F))).build());
		features.put("yellow_poplar_leaf_litter", createPoplar(VanillaIds.YELLOW_POPLAR_LEAVES).decorators(List.of(sparseLeafLitter, thickLeafLitter, TreeDecorator.shelfMushroom(0.4F))).build());
		features.put("fallen_oak_tree", createFallenOak().build());
		features.put("fallen_birch_tree", createFallenBirch(8).build());
		features.put("fallen_super_birch_tree", createFallenBirch(15).build());
		features.put("fallen_jungle_tree", createFallenJungle().build());
		features.put("fallen_spruce_tree", createFallenSpruce().build());
		features.put("fallen_poplar_tree", createFallenPoplar().build());
		return features;
	}

	private static TreeFeatureBuilder createStraightBlobTree(
			Identifier log,
			Identifier leaves,
			int baseHeight,
			int heightRandA,
			int heightRandB,
			int blobRadius
	) {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(log))
				.trunkPlacer(TreeTrunkPlacer.straight(baseHeight, heightRandA, heightRandB))
				.foliageProvider(BlockStateProvider.simple(leaves))
				.foliagePlacer(TreeFoliagePlacer.blob(IntProvider.constant(blobRadius), IntProvider.constant(0), 3))
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 1))
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder createOak() {
		return createStraightBlobTree(VanillaIds.OAK_LOG, VanillaIds.OAK_LEAVES, 4, 2, 0, 2).ignoreVines();
	}

	private static TreeFeatureBuilder createDarkOak(Identifier log, Identifier leaves) {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(log))
				.trunkPlacer(TreeTrunkPlacer.darkOak(6, 2, 1))
				.foliageProvider(BlockStateProvider.simple(leaves))
				.foliagePlacer(TreeFoliagePlacer.darkOak(IntProvider.constant(0), IntProvider.constant(0)))
				.minimumSize(TreeFeatureSize.threeLayers(1, 1, 0, 1, 2))
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder createBirch() {
		return createStraightBlobTree(VanillaIds.BIRCH_LOG, VanillaIds.BIRCH_LEAVES, 5, 2, 0, 2).ignoreVines();
	}

	private static TreeFeatureBuilder createSuperBirch() {
		return createStraightBlobTree(VanillaIds.BIRCH_LOG, VanillaIds.BIRCH_LEAVES, 5, 2, 6, 2).ignoreVines();
	}

	private static TreeFeatureBuilder createJungleTree() {
		return createStraightBlobTree(VanillaIds.JUNGLE_LOG, VanillaIds.JUNGLE_LEAVES, 4, 8, 0, 2);
	}

	private static TreeFeatureBuilder createFancyOak() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.OAK_LOG))
				.fancyTrunk(3, 11, 0)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.OAK_LEAVES))
				.fancyFoliage(IntProvider.constant(2), IntProvider.constant(4), 4)
				.minimumSize(TreeFeatureSize.twoLayers(0, 0, 0, Optional.of(4)))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder acacia() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.ACACIA_LOG))
				.forkingTrunk(5, 2, 2)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.ACACIA_LEAVES))
				.acaciaFoliage(IntProvider.constant(2), IntProvider.constant(0))
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 2))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder cherry() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.CHERRY_LOG))
				.cherryTrunk(7, 1, 0, IntProvider.weightedList(
						IntProvider.weighted(IntProvider.constant(1), 1),
						IntProvider.weighted(IntProvider.constant(2), 1),
						IntProvider.weighted(IntProvider.constant(3), 1)
				), IntProvider.uniform(2, 4), IntProvider.uniform(-4, -3), IntProvider.uniform(-1, 0))
				.foliageProvider(BlockStateProvider.simple(VanillaIds.CHERRY_LEAVES))
				.cherryFoliage(IntProvider.constant(4), IntProvider.constant(0), IntProvider.constant(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F)
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 2))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder spruce() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.SPRUCE_LOG))
				.straightTrunk(5, 2, 1)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.SPRUCE_LEAVES))
				.spruceFoliage(IntProvider.uniform(2, 3), IntProvider.uniform(0, 2), IntProvider.uniform(1, 2))
				.minimumSize(TreeFeatureSize.twoLayers(2, 0, 2))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder pine() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.SPRUCE_LOG))
				.straightTrunk(6, 4, 0)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.SPRUCE_LEAVES))
				.pineFoliage(IntProvider.constant(1), IntProvider.constant(1), IntProvider.uniform(3, 4))
				.minimumSize(TreeFeatureSize.twoLayers(2, 0, 2))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder megaJungle() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.JUNGLE_LOG))
				.megaJungleTrunk(10, 2, 19)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.JUNGLE_LEAVES))
				.megaJungleFoliage(IntProvider.constant(2), IntProvider.constant(0), 2)
				.minimumSize(TreeFeatureSize.twoLayers(1, 1, 2))
				.decorators(List.of(TreeDecorator.trunkVine(), TreeDecorator.leaveVine(0.25F)))
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder megaSpruce(IntProvider crownHeight) {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.SPRUCE_LOG))
				.giantTrunk(13, 2, 14)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.SPRUCE_LEAVES))
				.megaPineFoliage(IntProvider.constant(0), IntProvider.constant(0), crownHeight)
				.minimumSize(TreeFeatureSize.twoLayers(1, 1, 2))
				.decorator(TreeDecorator.alterGround(BlockStateProvider.ruleBased(
						BlockStateProvider.simple(VanillaIds.PODZOL),
						BlockStateProvider.rule(PlacedFeature.BlockPredicate.matchingBlockTag("minecraft:beneath_tree_podzol_replaceable"), BlockStateProvider.simple(VanillaIds.PODZOL))
				)))
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder bush(Identifier log, Identifier leaves) {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(log))
				.straightTrunk(1, 0, 0)
				.foliageProvider(BlockStateProvider.simple(leaves))
				.bushFoliage(IntProvider.constant(2), IntProvider.constant(1), 2)
				.minimumSize(TreeFeatureSize.twoLayers(0, 0, 0))
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder azalea() {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.OAK_LOG))
				.bendingTrunk(4, 2, 0, 3, IntProvider.uniform(1, 2))
				.foliageProvider(BlockStateProvider.weighted(
						BlockStateProvider.weightedEntry(VanillaIds.AZALEA_LEAVES, 3),
						BlockStateProvider.weightedEntry(VanillaIds.FLOWERING_AZALEA_LEAVES, 1)
				))
				.randomSpreadFoliage(IntProvider.constant(3), IntProvider.constant(0), 2, 50)
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 1))
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK)
				.belowTrunkProvider(BlockStateProvider.simple(VanillaIds.ROOTED_DIRT));
	}

	private static TreeFeatureBuilder mangrove(int baseHeight, int heightRandB, IntProvider extraBranchSteps, IntProvider rootOffset, int lowerSize, TreeDecorator beehive) {
		List<Identifier> canGrowThrough = List.of(id("mangrove_roots"), id("muddy_mangrove_roots"), VanillaIds.MUD);
		BlockStateProvider propagule = BlockStateProvider.randomizedInt(
				BlockStateProvider.simple(WorldgenBlockState.blockState(VanillaIds.MANGROVE_PROPAGULE).property("hanging", true)),
				"age",
				IntProvider.uniform(0, 4)
		);
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.MANGROVE_LOG))
				.upwardsBranchingTrunk(baseHeight, 1, heightRandB, extraBranchSteps, 0.5F, IntProvider.uniform(0, 1), canGrowThrough)
				.foliageProvider(BlockStateProvider.simple(VanillaIds.MANGROVE_LEAVES))
				.randomSpreadFoliage(IntProvider.constant(3), IntProvider.constant(0), 2, 70)
				.rootPlacer(new MangroveRootPlacer(
						Optional.of(rootOffset),
						BlockStateProvider.simple(VanillaIds.MANGROVE_ROOTS),
						Optional.of(new AboveRootPlacement(BlockStateProvider.simple(VanillaIds.MOSS_CARPET), 0.5F)),
						Optional.of(new MangroveRootPlacement(canGrowThrough, BlockStateProvider.simple(VanillaIds.MUD), BlockStateProvider.simple(VanillaIds.MUDDY_MANGROVE_ROOTS), 8, 15, 0.2F)),
						Optional.empty()
				))
				.minimumSize(TreeFeatureSize.twoLayers(lowerSize, 0, 2))
				.belowTrunkProvider(defaultBelowMangroveTrunkProvider())
				.decorators(List.of(TreeDecorator.leaveVine(0.125F), TreeDecorator.attachedToLeaves(0.14F, 1, 0, propagule, 2, "down"), beehive))
				.ignoreVines();
	}

	private static TreeFeatureBuilder createPoplar(Identifier leafBlock) {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.POPLAR_LOG))
				.poplarTrunk(7, 4, 0, IntProvider.constant(4), IntProvider.uniform(1, 4))
				.foliageProvider(BlockStateProvider.simple(leafBlock))
				.poplarFoliage(IntProvider.weightedList(
						IntProvider.weighted(IntProvider.constant(5), 5),
						IntProvider.weighted(IntProvider.constant(6), 5),
						IntProvider.weighted(IntProvider.constant(7), 1),
						IntProvider.weighted(IntProvider.constant(8), 1)
				), IntProvider.constant(0), IntProvider.uniform(5, 6), 0.15F)
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 2))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static TreeFeatureBuilder createLargePoplar(Identifier leafBlock) {
		return new TreeFeatureBuilder()
				.trunkProvider(BlockStateProvider.simple(VanillaIds.POPLAR_LOG))
				.poplarTrunk(10, 4, 3, IntProvider.constant(5), IntProvider.uniform(2, 5))
				.foliageProvider(BlockStateProvider.simple(leafBlock))
				.poplarFoliage(IntProvider.weightedList(
						IntProvider.weighted(IntProvider.constant(6), 3),
						IntProvider.weighted(IntProvider.constant(7), 5),
						IntProvider.weighted(IntProvider.constant(8), 5),
						IntProvider.weighted(IntProvider.constant(9), 2)
				), IntProvider.constant(0), IntProvider.uniform(6, 8), 0.15F)
				.minimumSize(TreeFeatureSize.twoLayers(1, 0, 2))
				.ignoreVines()
				.belowTrunkProvider(TreeFeatures.BELOW_TRUNK);
	}

	private static FallenTreeFeatureBuilder createFallenOak() {
		return createFallenTrees(VanillaIds.OAK_LOG, 4, 7).stumpDecorator(TreeDecorator.trunkVine());
	}

	private static FallenTreeFeatureBuilder createFallenBirch(int maxHeight) {
		return createFallenTrees(VanillaIds.BIRCH_LOG, 5, maxHeight);
	}

	private static FallenTreeFeatureBuilder createFallenJungle() {
		return createFallenTrees(VanillaIds.JUNGLE_LOG, 4, 11).stumpDecorator(TreeDecorator.trunkVine());
	}

	private static FallenTreeFeatureBuilder createFallenSpruce() {
		return createFallenTrees(VanillaIds.SPRUCE_LOG, 6, 10);
	}

	private static FallenTreeFeatureBuilder createFallenPoplar() {
		return new FallenTreeFeatureBuilder()
				.logBlock(VanillaIds.POPLAR_LOG)
				.logLength(IntProvider.uniform(4, 7))
				.logDecorator(TreeDecorator.attachedToLogs(0.1F, BlockStateProvider.simple(VanillaIds.BROWN_MUSHROOM), "up"))
				.logDecorator(TreeDecorator.shelfMushroom(0.8F));
	}

	private static FallenTreeFeatureBuilder createFallenTrees(Identifier logBlock, int minLength, int maxLength) {
		return new FallenTreeFeatureBuilder()
				.logBlock(logBlock)
				.logLength(IntProvider.uniform(minLength, maxLength))
				.logDecorator(TreeDecorator.attachedToLogs(0.1F, mushroomProvider(), "up"));
	}

	private static Feature crimsonFungus(boolean planted) {
		return new HugeFungusFeatureBuilder()
				.validBaseBlock(VanillaIds.CRIMSON_NYLIUM)
				.stemState(VanillaIds.CRIMSON_STEM)
				.hatState(id("nether_wart_block"))
				.decorState(VanillaIds.SHROOMLIGHT)
				.planted(planted)
				.build();
	}

	private static Feature warpedFungus(boolean planted) {
		return new HugeFungusFeatureBuilder()
				.validBaseBlock(VanillaIds.WARPED_NYLIUM)
				.stemState(VanillaIds.WARPED_STEM)
				.hatState(id("warped_wart_block"))
				.decorState(VanillaIds.SHROOMLIGHT)
				.planted(planted)
				.build();
	}

	private static Feature hugeMushroom(String type, Identifier cap, int radius) {
		return new HugeMushroomFeatureBuilder(type)
				.capProvider(cap)
				.stemProvider(VanillaIds.MUSHROOM_STEM)
				.foliageRadius(radius)
				.canPlaceOn(PlacedFeature.BlockPredicate.matchingBlockTag(type.endsWith("brown_mushroom") ? "minecraft:huge_brown_mushroom_can_place_on" : "minecraft:huge_red_mushroom_can_place_on"))
				.build();
	}

	private static BlockStateProvider leafLitter(int minSegments, int maxSegments) {
		return BlockStateProvider.randomizedInt(
				BlockStateProvider.simple(id("leaf_litter")),
				"segment_amount",
				IntProvider.uniform(minSegments, maxSegments)
		);
	}

	private static BlockStateProvider mushroomProvider() {
		return BlockStateProvider.weighted(
				BlockStateProvider.weightedEntry(VanillaIds.RED_MUSHROOM, 2),
				BlockStateProvider.weightedEntry(VanillaIds.BROWN_MUSHROOM, 1)
		);
	}

	private static BlockStateProvider defaultBelowMangroveTrunkProvider() {
		return BlockStateProvider.ruleBased(
				BlockStateProvider.simple(VanillaIds.DIRT),
				BlockStateProvider.rule(
						PlacedFeature.BlockPredicate.matchingBlocks(VanillaIds.MUD, VanillaIds.MUDDY_MANGROVE_ROOTS),
						BlockStateProvider.simple(VanillaIds.MUD)
				)
		);
	}

	private static Identifier id(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	/**
	 * registers every tree feature under {@code mymod:trees/<name>}
	 */
	public static void registerAll(RuntimeResourcePack pack) {
		bootstrap().forEach((name, feature) ->
				pack.addFeature(Identifier.fromNamespaceAndPath("mymod", "trees/" + name), feature));
	}

	static void main() {
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, bootstrap().get("mangrove")));

		// dump the whole catalogue as its own datapack
		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:tree_features");
		pack.addDataPackMcmeta("Tree feature catalogue, generated by ARRP");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/tree_features"));
	}
}
