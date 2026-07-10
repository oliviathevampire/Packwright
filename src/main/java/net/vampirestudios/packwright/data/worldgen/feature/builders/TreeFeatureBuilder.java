package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.packwright.data.worldgen.feature.foliage.TreeFoliagePlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.decorator.TreeDecorator;
import net.vampirestudios.packwright.data.worldgen.feature.tree.root.TreeRootPlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.size.TreeFeatureSize;
import net.vampirestudios.packwright.data.worldgen.feature.trunk.TreeTrunkPlacer;

import java.util.ArrayList;
import java.util.List;

public final class TreeFeatureBuilder extends FeatureBuilder {
	private final List<TreeDecorator> decorators = new ArrayList<>();

	public TreeFeatureBuilder() {
		super("minecraft:tree");
		// the game requires the decorators field, even when empty
		feature.property("decorators", this.decorators, TreeDecorator.CODEC);
	}

	public TreeFeatureBuilder trunkProvider(BlockStateProvider provider) {
		feature.property("trunk_provider", BlockStateProvider.CODEC, provider);
		return this;
	}

	public TreeFeatureBuilder trunkBlock(Identifier block) {
		return trunkProvider(BlockStateProvider.simple(block));
	}

	public TreeFeatureBuilder foliageProvider(BlockStateProvider provider) {
		feature.property("foliage_provider", BlockStateProvider.CODEC, provider);
		return this;
	}

	public TreeFeatureBuilder foliageBlock(Identifier block) {
		return foliageProvider(BlockStateProvider.simple(block));
	}

	/** the block placed below the trunk; required, replaced {@code dirt_provider} in 26.1 */
	public TreeFeatureBuilder belowTrunkProvider(BlockStateProvider provider) {
		feature.property("below_trunk_provider", BlockStateProvider.CODEC, provider);
		return this;
	}

	/** @deprecated the field was renamed; use {@link #belowTrunkProvider(BlockStateProvider)} */
	@Deprecated
	public TreeFeatureBuilder dirtProvider(BlockStateProvider provider) {
		return belowTrunkProvider(provider);
	}

	public TreeFeatureBuilder trunkPlacer(TreeTrunkPlacer placer) {
		feature.property("trunk_placer", TreeTrunkPlacer.CODEC, placer);
		return this;
	}

	public TreeFeatureBuilder straightTrunk(int baseHeight, int heightRandA, int heightRandB) {
		return trunkPlacer(TreeTrunkPlacer.straight(baseHeight, heightRandA, heightRandB));
	}

	public TreeFeatureBuilder fancyTrunk(int baseHeight, int heightRandA, int heightRandB) {
		return trunkPlacer(TreeTrunkPlacer.fancy(baseHeight, heightRandA, heightRandB));
	}

	public TreeFeatureBuilder forkingTrunk(int baseHeight, int heightRandA, int heightRandB) {
		return trunkPlacer(TreeTrunkPlacer.forking(baseHeight, heightRandA, heightRandB));
	}

	public TreeFeatureBuilder giantTrunk(int baseHeight, int heightRandA, int heightRandB) {
		return trunkPlacer(TreeTrunkPlacer.giant(baseHeight, heightRandA, heightRandB));
	}

	public TreeFeatureBuilder megaJungleTrunk(int baseHeight, int heightRandA, int heightRandB) {
		return trunkPlacer(TreeTrunkPlacer.megaJungle(baseHeight, heightRandA, heightRandB));
	}

	public TreeFeatureBuilder darkOakTrunk(int baseHeight, int heightRandA, int heightRandB) {
		return trunkPlacer(TreeTrunkPlacer.darkOak(baseHeight, heightRandA, heightRandB));
	}

	public TreeFeatureBuilder poplarTrunk(IntProvider trunkHeightAboveBranches, IntProvider branchAmount) {
		return trunkPlacer(TreeTrunkPlacer.poplar(trunkHeightAboveBranches, branchAmount));
	}

	public TreeFeatureBuilder poplarTrunk(int baseHeight, int heightRandA, int heightRandB, IntProvider trunkHeightAboveBranches, IntProvider branchAmount) {
		return trunkPlacer(TreeTrunkPlacer.poplar(baseHeight, heightRandA, heightRandB, trunkHeightAboveBranches, branchAmount));
	}

	public TreeFeatureBuilder cherryTrunk(
			int baseHeight,
			int heightRandA,
			int heightRandB,
			IntProvider branchCount,
			IntProvider branchHorizontalLength,
			IntProvider branchStartOffsetFromTop,
			IntProvider branchEndOffsetFromTop
	) {
		return trunkPlacer(TreeTrunkPlacer.cherry(baseHeight, heightRandA, heightRandB, branchCount, branchHorizontalLength, branchStartOffsetFromTop, branchEndOffsetFromTop));
	}

	public TreeFeatureBuilder bendingTrunk(int baseHeight, int heightRandA, int heightRandB, int minHeightForLeaves, IntProvider bendLength) {
		return trunkPlacer(TreeTrunkPlacer.bending(baseHeight, heightRandA, heightRandB, minHeightForLeaves, bendLength));
	}

	public TreeFeatureBuilder upwardsBranchingTrunk(
			int baseHeight,
			int heightRandA,
			int heightRandB,
			IntProvider extraBranchSteps,
			float placeBranchPerLogProbability,
			IntProvider extraBranchLength,
			List<Identifier> canGrowThrough
	) {
		return trunkPlacer(TreeTrunkPlacer.upwardsBranching(baseHeight, heightRandA, heightRandB, extraBranchSteps, placeBranchPerLogProbability, extraBranchLength, canGrowThrough));
	}

	public TreeFeatureBuilder foliagePlacer(TreeFoliagePlacer placer) {
		feature.property("foliage_placer", TreeFoliagePlacer.CODEC, placer);
		return this;
	}

	public TreeFeatureBuilder blobFoliage(IntProvider radius, IntProvider offset, int height) {
		return foliagePlacer(TreeFoliagePlacer.blob(radius, offset, height));
	}

	public TreeFeatureBuilder fancyFoliage(IntProvider radius, IntProvider offset, int height) {
		return foliagePlacer(TreeFoliagePlacer.fancy(radius, offset, height));
	}

	public TreeFeatureBuilder spruceFoliage(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
		return foliagePlacer(TreeFoliagePlacer.spruce(radius, offset, trunkHeight));
	}

	public TreeFeatureBuilder pineFoliage(IntProvider radius, IntProvider offset, IntProvider height) {
		return foliagePlacer(TreeFoliagePlacer.pine(radius, offset, height));
	}

	public TreeFeatureBuilder acaciaFoliage(IntProvider radius, IntProvider offset) {
		return foliagePlacer(TreeFoliagePlacer.acacia(radius, offset));
	}

	public TreeFeatureBuilder bushFoliage(IntProvider radius, IntProvider offset, int height) {
		return foliagePlacer(TreeFoliagePlacer.bush(radius, offset, height));
	}

	public TreeFeatureBuilder megaPineFoliage(IntProvider radius, IntProvider offset, IntProvider crownHeight) {
		return foliagePlacer(TreeFoliagePlacer.megaPine(radius, offset, crownHeight));
	}

	public TreeFeatureBuilder megaJungleFoliage(IntProvider radius, IntProvider offset, int height) {
		return foliagePlacer(TreeFoliagePlacer.megaJungle(radius, offset, height));
	}

	public TreeFeatureBuilder randomSpreadFoliage(IntProvider radius, IntProvider offset, int foliageHeight, int leafPlacementAttempts) {
		return foliagePlacer(TreeFoliagePlacer.randomSpread(radius, offset, foliageHeight, leafPlacementAttempts));
	}

	public TreeFeatureBuilder poplarFoliage(IntProvider height, float sideHoleChance) {
		return foliagePlacer(TreeFoliagePlacer.poplar(height, sideHoleChance));
	}

	public TreeFeatureBuilder poplarFoliage(IntProvider radius, IntProvider offset, IntProvider height, float sideHoleChance) {
		return foliagePlacer(TreeFoliagePlacer.poplar(radius, offset, height, sideHoleChance));
	}

	public TreeFeatureBuilder cherryFoliage(IntProvider radius, IntProvider offset, IntProvider height, float wideBottomLayerHoleChance, float cornerHoleChance, float hangingLeavesChance, float hangingLeavesExtensionChance) {
		return foliagePlacer(TreeFoliagePlacer.cherry(radius, offset, height, wideBottomLayerHoleChance, cornerHoleChance, hangingLeavesChance, hangingLeavesExtensionChance));
	}

	public TreeFeatureBuilder minimumSize(TreeFeatureSize size) {
		feature.property("minimum_size", TreeFeatureSize.CODEC, size);
		return this;
	}

	public TreeFeatureBuilder rootPlacer(TreeRootPlacer placer) {
		feature.property("root_placer", TreeRootPlacer.CODEC, placer);
		return this;
	}

	public TreeFeatureBuilder decorator(TreeDecorator decorator) {
		this.decorators.add(decorator);
		feature.property("decorators", this.decorators, TreeDecorator.CODEC);
		return this;
	}

	public TreeFeatureBuilder decorators(List<TreeDecorator> decorators) {
		for (TreeDecorator decorator : decorators) {
			decorator(decorator);
		}
		return this;
	}

	public TreeFeatureBuilder shelfMushrooms(float probability) {
		return decorator(TreeDecorator.shelfMushroom(probability));
	}

	public TreeFeatureBuilder ignoreVines(boolean ignoreVines) {
		feature.property("ignore_vines", ignoreVines);
		return this;
	}

	public TreeFeatureBuilder ignoreVines() {
		return ignoreVines(true);
	}
}
