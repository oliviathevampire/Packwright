package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * A distance-travelled predicate, as used by advancement triggers such as
 * {@code minecraft:levitation}, {@code minecraft:nether_travel} and
 * {@code minecraft:fall_after_explosion}. Each axis defaults to unbounded ("any")
 * when absent.
 */
public record DistancePredicate(
		Optional<DoubleBound> x, Optional<DoubleBound> y, Optional<DoubleBound> z,
		Optional<DoubleBound> horizontal, Optional<DoubleBound> absolute
) {
	public static final Codec<DistancePredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleBound.CODEC.optionalFieldOf("x").forGetter(DistancePredicate::x),
			DoubleBound.CODEC.optionalFieldOf("y").forGetter(DistancePredicate::y),
			DoubleBound.CODEC.optionalFieldOf("z").forGetter(DistancePredicate::z),
			DoubleBound.CODEC.optionalFieldOf("horizontal").forGetter(DistancePredicate::horizontal),
			DoubleBound.CODEC.optionalFieldOf("absolute").forGetter(DistancePredicate::absolute)
	).apply(i, DistancePredicate::new));

	public static DistancePredicate of() {
		return new DistancePredicate(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	}

	public DistancePredicate x(DoubleBound bound) { return new DistancePredicate(Optional.of(bound), y, z, horizontal, absolute); }
	public DistancePredicate y(DoubleBound bound) { return new DistancePredicate(x, Optional.of(bound), z, horizontal, absolute); }
	public DistancePredicate z(DoubleBound bound) { return new DistancePredicate(x, y, Optional.of(bound), horizontal, absolute); }
	public DistancePredicate horizontal(DoubleBound bound) { return new DistancePredicate(x, y, z, Optional.of(bound), absolute); }
	public DistancePredicate absolute(DoubleBound bound) { return new DistancePredicate(x, y, z, horizontal, Optional.of(bound)); }
}
