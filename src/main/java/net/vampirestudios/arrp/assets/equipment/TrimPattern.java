package net.vampirestudios.arrp.assets.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class TrimPattern {
	public static final Codec<TrimPattern> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description),
			Codec.BOOL.fieldOf("decal").orElse(false).forGetter(x -> x.decal)
	).apply(i, (assetId, description, decal) -> {
		TrimPattern out = new TrimPattern();
		out.assetId = assetId;
		out.description = description;
		out.decal = decal;
		return out;
	}));

	private Identifier assetId;
	private String description;
	private boolean decal;

	public static TrimPattern trimPattern() {
		return new TrimPattern();
	}

	public TrimPattern assetId(Identifier id) { this.assetId = id; return this; }
	public TrimPattern description(String description) { this.description = description; return this; }
	public TrimPattern decal(boolean decal) { this.decal = decal; return this; }

	public Identifier getAssetId() { return assetId; }
	public String getDescription() { return description; }
	public boolean isDecal() { return decal; }
}
