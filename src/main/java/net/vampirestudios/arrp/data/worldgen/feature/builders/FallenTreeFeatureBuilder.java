package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.arrp.data.worldgen.feature.tree.decorator.TreeDecorator;

import java.util.ArrayList;
import java.util.List;

public final class FallenTreeFeatureBuilder extends FeatureBuilder {
	private final List<TreeDecorator> stumpDecorators = new ArrayList<>();
	private final List<TreeDecorator> logDecorators = new ArrayList<>();

	public FallenTreeFeatureBuilder() {
		super("minecraft:fallen_tree");
	}

	public FallenTreeFeatureBuilder logProvider(BlockStateProvider provider) {
		feature.property("log_provider", BlockStateProvider.CODEC, provider);
		return this;
	}

	public FallenTreeFeatureBuilder logBlock(Identifier block) {
		return logProvider(BlockStateProvider.simple(block));
	}

	public FallenTreeFeatureBuilder logLength(IntProvider length) {
		feature.property("log_length", IntProvider.CODEC, length);
		return this;
	}

	public FallenTreeFeatureBuilder stumpDecorator(TreeDecorator decorator) {
		this.stumpDecorators.add(decorator);
		feature.property("stump_decorators", this.stumpDecorators, TreeDecorator.CODEC);
		return this;
	}

	public FallenTreeFeatureBuilder logDecorator(TreeDecorator decorator) {
		this.logDecorators.add(decorator);
		feature.property("log_decorators", this.logDecorators, TreeDecorator.CODEC);
		return this;
	}
}
