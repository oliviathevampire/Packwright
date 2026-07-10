package net.vampirestudios.arrp.data.worldgen.feature.builders;

public final class EndGatewayFeatureBuilder extends FeatureBuilder {
	public EndGatewayFeatureBuilder() {
		super("minecraft:end_gateway");
	}

	public EndGatewayFeatureBuilder exact(boolean value) {
		feature.property("exact", value);
		return this;
	}

	public EndGatewayFeatureBuilder exit(int x, int y, int z) {
		feature.property("exit", java.util.List.of(x, y, z));
		return this;
	}
}
