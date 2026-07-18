package net.vampirestudios.packwright.data.loot.providers.number;

import net.vampirestudios.packwright.data.loot.EntityTarget;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ScoreNumberProvider(EntityTarget target, String score, float scale) implements NumberProvider {
	public static final MapCodec<ScoreNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:score"),
			EntityTarget.CODEC.fieldOf("target").forGetter(ScoreNumberProvider::target),
			Codec.STRING.fieldOf("score").forGetter(ScoreNumberProvider::score),
			Codec.FLOAT.optionalFieldOf("scale", 1.0F).forGetter(ScoreNumberProvider::scale)
	).apply(i, (type, target, score, scale) -> new ScoreNumberProvider(target, score, scale)));
}
