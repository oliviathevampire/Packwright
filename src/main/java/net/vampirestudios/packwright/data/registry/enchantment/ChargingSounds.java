package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/** one entry of {@code crossbow_charging_sounds}: the sounds played at each charge stage */
public record ChargingSounds(Optional<Identifier> start, Optional<Identifier> mid, Optional<Identifier> end) {
	public static final Codec<ChargingSounds> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.optionalFieldOf("start").forGetter(ChargingSounds::start),
			Identifier.CODEC.optionalFieldOf("mid").forGetter(ChargingSounds::mid),
			Identifier.CODEC.optionalFieldOf("end").forGetter(ChargingSounds::end)
	).apply(i, ChargingSounds::new));

	public static ChargingSounds of(Identifier start, Identifier mid, Identifier end) {
		return new ChargingSounds(Optional.ofNullable(start), Optional.ofNullable(mid), Optional.ofNullable(end));
	}
}
