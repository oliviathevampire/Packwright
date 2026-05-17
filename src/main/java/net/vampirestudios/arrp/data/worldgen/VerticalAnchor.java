package net.vampirestudios.arrp.data.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import java.util.function.Function;

public interface VerticalAnchor {
	Codec<VerticalAnchor> CODEC = Codec.xor(Absolute.CODEC, Codec.xor(AboveBottom.CODEC, BelowTop.CODEC))
			.xmap(VerticalAnchor::merge, VerticalAnchor::split);
	VerticalAnchor BOTTOM = aboveBottom(0);
	VerticalAnchor TOP = belowTop(0);
	int MIN_Y = -2032;
	int MAX_Y = 2031;

	public static VerticalAnchor absolute(int value) {
		return new Absolute(value);
	}

	public static VerticalAnchor aboveBottom(int offset) {
		return new AboveBottom(offset);
	}

	public static VerticalAnchor belowTop(int offset) {
		return new BelowTop(offset);
	}

	public static VerticalAnchor bottom() {
		return BOTTOM;
	}

	public static VerticalAnchor top() {
		return TOP;
	}

	private static VerticalAnchor merge(Either<Absolute, Either<AboveBottom, BelowTop>> either) {
		return either.map(Function.identity(), Either::unwrap);
	}

	private static Either<Absolute, Either<AboveBottom, BelowTop>> split(VerticalAnchor anchor) {
		return anchor instanceof Absolute absolute
				? Either.left(absolute)
				: Either.right(anchor instanceof AboveBottom aboveBottom ? Either.left(aboveBottom) : Either.right((BelowTop) anchor));
	}

	public record AboveBottom(int offset) implements VerticalAnchor {
		public static final Codec<AboveBottom> CODEC = Codec.intRange(MIN_Y, MAX_Y)
				.fieldOf("above_bottom")
				.xmap(AboveBottom::new, AboveBottom::offset)
				.codec();
	}

	public record Absolute(int y) implements VerticalAnchor {
		public static final Codec<Absolute> CODEC = Codec.intRange(MIN_Y, MAX_Y)
				.fieldOf("absolute")
				.xmap(Absolute::new, Absolute::y)
				.codec();
	}

	public record BelowTop(int offset) implements VerticalAnchor {
		public static final Codec<BelowTop> CODEC = Codec.intRange(MIN_Y, MAX_Y)
				.fieldOf("below_top")
				.xmap(BelowTop::new, BelowTop::offset)
				.codec();
	}
}
