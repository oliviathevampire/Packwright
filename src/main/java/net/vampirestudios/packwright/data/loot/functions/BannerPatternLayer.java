package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.util.LootValue;

public record BannerPatternLayer(Identifier pattern, String color) {
	public static final Codec<BannerPatternLayer> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("pattern").forGetter(BannerPatternLayer::pattern),
			Codec.STRING.fieldOf("color").forGetter(BannerPatternLayer::color)
	).apply(i, BannerPatternLayer::new));

	public Object value() {
		return LootValue.encode(CODEC, this);
	}
}
