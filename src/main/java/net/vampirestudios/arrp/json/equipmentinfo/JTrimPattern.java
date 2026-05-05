package net.vampirestudios.arrp.json.equipmentinfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;

public class JTrimPattern {
	public static final Codec<JTrimPattern> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			ComponentSerialization.CODEC.fieldOf("description").forGetter(x -> x.description),
			Codec.BOOL.fieldOf("decal").orElse(false).forGetter(x -> x.decal)
	).apply(i, (assetId, description, decal) -> {
		JTrimPattern out = new JTrimPattern();
		out.assetId = assetId;
		out.description = description;
		out.decal = decal;
		return out;
	}));

	private Identifier assetId;
	private Component description;
	private boolean decal;

	public static JTrimPattern trimPattern() {
		return new JTrimPattern();
	}

	public JTrimPattern assetId(Identifier id) { this.assetId = id; return this; }
	public JTrimPattern description(Component description) { this.description = description; return this; }
	public JTrimPattern description(String description) { return this.description(Component.literal(description)); }
	public JTrimPattern decal(boolean decal) { this.decal = decal; return this; }

	public Identifier getAssetId() { return assetId; }
	public Component getDescription() { return description; }
	public boolean isDecal() { return decal; }
}
