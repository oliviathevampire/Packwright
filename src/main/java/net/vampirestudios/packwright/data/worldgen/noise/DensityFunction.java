package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public interface DensityFunction {
	Codec<DensityFunction> CODEC = Codec.recursive(
			"PackwrightDensityFunction",
			DensityFunction::createCodec
	);

	private static Codec<DensityFunction> createCodec(Codec<DensityFunction> selfCodec) {
		Codec<Direct> directCodec = DensityFunctionTypes.directCodec(selfCodec);

		return Codec.either(
				Codec.DOUBLE,
				Codec.either(Identifier.CODEC, directCodec)
		).xmap(
				value -> value.map(
						DensityFunctions::constant,
						referenceOrDirect -> referenceOrDirect.map(
								Reference::new,
								function -> function
						)
				),
				function -> {
					if (function instanceof DensityFunctions.Constant constant) {
						return Either.left(constant.value());
					}

					if (function instanceof Reference reference) {
						return Either.right(Either.left(reference.id()));
					}

					if (function instanceof Direct direct) {
						return Either.right(Either.right(direct));
					}

					throw new IllegalStateException(
							"Unsupported density function: " + function
					);
				}
		);
	}

	/**
	 * An inline density function serialized as an object containing a
	 * {@code type} field.
	 */
	interface Direct extends DensityFunction {
		DensityFunctionType<?> type();
	}

	/**
	 * A reference to an entry in the
	 * {@code worldgen/density_function} registry.
	 */
	record Reference(Identifier id) implements DensityFunction {
	}
}