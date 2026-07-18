package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.util.LootValue;

import java.util.List;
import java.util.Optional;

public record LootFireworkExplosion(
		String shape,
		List<Integer> colors,
		List<Integer> fadeColors,
		Boolean trail,
		Boolean twinkle
) {
	public static final Codec<LootFireworkExplosion> COMPONENT_CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("shape").forGetter(LootFireworkExplosion::shape),
			Codec.INT.listOf().optionalFieldOf("colors", List.of()).forGetter(explosion -> explosion.colors == null ? List.of() : explosion.colors),
			Codec.INT.listOf().optionalFieldOf("fade_colors", List.of()).forGetter(explosion -> explosion.fadeColors == null ? List.of() : explosion.fadeColors),
			Codec.BOOL.optionalFieldOf("has_trail", false).forGetter(explosion -> Boolean.TRUE.equals(explosion.trail)),
			Codec.BOOL.optionalFieldOf("has_twinkle", false).forGetter(explosion -> Boolean.TRUE.equals(explosion.twinkle))
	).apply(i, LootFireworkExplosion::newComponent));
	public static final Codec<LootFireworkExplosion> PATCH_CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("shape").forGetter(explosion -> Optional.ofNullable(explosion.shape)),
			Codec.INT.listOf().optionalFieldOf("colors").forGetter(explosion -> Optional.ofNullable(explosion.colors)),
			Codec.INT.listOf().optionalFieldOf("fade_colors").forGetter(explosion -> Optional.ofNullable(explosion.fadeColors)),
			Codec.BOOL.optionalFieldOf("trail").forGetter(explosion -> Optional.ofNullable(explosion.trail)),
			Codec.BOOL.optionalFieldOf("twinkle").forGetter(explosion -> Optional.ofNullable(explosion.twinkle))
	).apply(i, (shape, colors, fadeColors, trail, twinkle) -> new LootFireworkExplosion(
			shape.orElse(null),
			colors.orElse(null),
			fadeColors.orElse(null),
			trail.orElse(null),
			twinkle.orElse(null)
	)));

	private static LootFireworkExplosion newComponent(String shape, List<Integer> colors, List<Integer> fadeColors, boolean trail, boolean twinkle) {
		return new LootFireworkExplosion(shape, colors, fadeColors, trail, twinkle);
	}

	public Object componentValue() {
		return LootValue.encode(COMPONENT_CODEC, this);
	}

	public Object patchValue() {
		return LootValue.encode(PATCH_CODEC, this);
	}
}
