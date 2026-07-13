package net.vampirestudios.packwright.data.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;

/**
 * A vanilla-style height provider.
 *
 * <p>Constant providers use the compact vertical-anchor form, while the other
 * providers use a typed object containing a {@code type} field.</p>
 */
public sealed interface HeightProvider permits
		HeightProvider.Constant,
		HeightProvider.Direct {

	Codec<HeightProvider> CODEC = Codec.either(
			VerticalAnchor.CODEC,
			Direct.CODEC
	).xmap(
			value -> value.map(Constant::new, direct -> direct),
			provider -> provider instanceof Constant(VerticalAnchor value)
					? Either.left(value)
					: Either.right((Direct) provider)
	);

	/**
	 * A fixed height. This serializes directly as its vertical anchor, matching
	 * vanilla's compact constant-height representation.
	 */
	record Constant(VerticalAnchor value) implements HeightProvider {
	}

	/**
	 * A typed height provider serialized using codec dispatch.
	 */
	sealed interface Direct extends HeightProvider permits
			Uniform,
			BiasedToBottom,
			VeryBiasedToBottom,
			Trapezoid {

		Codec<Direct> CODEC = Type.CODEC.dispatch(
				"type",
				Direct::type,
				Direct::codecFor
		);

		Type type();

		private static MapCodec<? extends Direct> codecFor(Type type) {
			return switch (type) {
				case UNIFORM ->
						Uniform.CODEC;

				case BIASED_TO_BOTTOM ->
						BiasedToBottom.CODEC;

				case VERY_BIASED_TO_BOTTOM ->
						VeryBiasedToBottom.CODEC;

				case TRAPEZOID ->
						Trapezoid.CODEC;
			};
		}
	}

	/**
	 * Uniformly selects a height between the inclusive bounds.
	 */
	record Uniform(
			VerticalAnchor minInclusive,
			VerticalAnchor maxInclusive
	) implements Direct {
		private static final MapCodec<Uniform> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				VerticalAnchor.CODEC
						.fieldOf("min_inclusive")
						.forGetter(Uniform::minInclusive),
				VerticalAnchor.CODEC
						.fieldOf("max_inclusive")
						.forGetter(Uniform::maxInclusive)
		).apply(instance, Uniform::new));

		@Override
		public Type type() {
			return Type.UNIFORM;
		}
	}

	/**
	 * Selects heights with a bias toward the lower bound.
	 */
	record BiasedToBottom(
			VerticalAnchor minInclusive,
			VerticalAnchor maxInclusive,
			int inner
	) implements Direct {
		private static final MapCodec<BiasedToBottom> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				VerticalAnchor.CODEC
						.fieldOf("min_inclusive")
						.forGetter(BiasedToBottom::minInclusive),
				VerticalAnchor.CODEC
						.fieldOf("max_inclusive")
						.forGetter(BiasedToBottom::maxInclusive),
				Codec.intRange(1, Integer.MAX_VALUE)
						.fieldOf("inner")
						.forGetter(BiasedToBottom::inner)
		).apply(instance, BiasedToBottom::new));

		@Override
		public Type type() {
			return Type.BIASED_TO_BOTTOM;
		}
	}

	/**
	 * Selects heights with a stronger bias toward the lower bound.
	 */
	record VeryBiasedToBottom(
			VerticalAnchor minInclusive,
			VerticalAnchor maxInclusive,
			int inner
	) implements Direct {
		private static final MapCodec<VeryBiasedToBottom> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				VerticalAnchor.CODEC
						.fieldOf("min_inclusive")
						.forGetter(VeryBiasedToBottom::minInclusive),
				VerticalAnchor.CODEC
						.fieldOf("max_inclusive")
						.forGetter(VeryBiasedToBottom::maxInclusive),
				Codec.intRange(1, Integer.MAX_VALUE)
						.fieldOf("inner")
						.forGetter(VeryBiasedToBottom::inner)
		).apply(instance, VeryBiasedToBottom::new));

		@Override
		public Type type() {
			return Type.VERY_BIASED_TO_BOTTOM;
		}
	}

	/**
	 * Selects heights using a trapezoidal distribution.
	 *
	 * @param plateau width of the flat center of the distribution
	 */
	record Trapezoid(
			VerticalAnchor minInclusive,
			VerticalAnchor maxInclusive,
			int plateau
	) implements Direct {
		private static final MapCodec<Trapezoid> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				VerticalAnchor.CODEC
						.fieldOf("min_inclusive")
						.forGetter(Trapezoid::minInclusive),
				VerticalAnchor.CODEC
						.fieldOf("max_inclusive")
						.forGetter(Trapezoid::maxInclusive),
				Codec.intRange(0, Integer.MAX_VALUE)
						.fieldOf("plateau")
						.forGetter(Trapezoid::plateau)
		).apply(instance, Trapezoid::new));

		@Override
		public Type type() {
			return Type.TRAPEZOID;
		}
	}

	enum Type {
		UNIFORM("minecraft:uniform"),
		BIASED_TO_BOTTOM("minecraft:biased_to_bottom"),
		VERY_BIASED_TO_BOTTOM("minecraft:very_biased_to_bottom"),
		TRAPEZOID("minecraft:trapezoid");

		static final Codec<Type> CODEC = Codec.STRING.comapFlatMap(
				id -> Arrays.stream(values())
						.filter(type -> type.id.equals(id))
						.findFirst()
						.map(DataResult::success)
						.orElseGet(() -> DataResult.error(
								() -> "Unknown height provider type: " + id
						)),
				Type::id
		);

		private final String id;

		Type(String id) {
			this.id = id;
		}

		public String id() {
			return this.id;
		}
	}

	static HeightProvider constant(VerticalAnchor value) {
		return new Constant(value);
	}

	static HeightProvider constant(int y) {
		return constant(VerticalAnchor.absolute(y));
	}

	static HeightProvider uniform(VerticalAnchor min, VerticalAnchor max) {
		return new Uniform(min, max);
	}

	static HeightProvider biasedToBottom(
			VerticalAnchor min,
			VerticalAnchor max,
			int inner
	) {
		return new BiasedToBottom(min, max, inner);
	}

	static HeightProvider veryBiasedToBottom(
			VerticalAnchor min,
			VerticalAnchor max,
			int inner
	) {
		return new VeryBiasedToBottom(min, max, inner);
	}

	static HeightProvider trapezoid(
			VerticalAnchor min,
			VerticalAnchor max,
			int plateau
	) {
		return new Trapezoid(min, max, plateau);
	}
}