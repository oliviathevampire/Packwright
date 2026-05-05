package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JDecoratedPotPattern {
	public static final Codec<JDecoratedPotPattern> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId)
	).apply(i, assetId -> {
		JDecoratedPotPattern out = new JDecoratedPotPattern();
		out.assetId = assetId;
		return out;
	}));

	private Identifier assetId;

	public static JDecoratedPotPattern decoratedPotPattern() {
		return new JDecoratedPotPattern();
	}

	public JDecoratedPotPattern assetId(Identifier id) { this.assetId = id; return this; }

	public Identifier getAssetId() { return assetId; }
}
