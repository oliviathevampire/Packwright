package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class DecoratedPotPattern {
	public static final Codec<DecoratedPotPattern> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId)
	).apply(i, assetId -> {
		DecoratedPotPattern out = new DecoratedPotPattern();
		out.assetId = assetId;
		return out;
	}));

	private Identifier assetId;

	public static DecoratedPotPattern decoratedPotPattern() {
		return new DecoratedPotPattern();
	}

	public DecoratedPotPattern assetId(Identifier id) { this.assetId = id; return this; }

	public Identifier getAssetId() { return assetId; }
}
