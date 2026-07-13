package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public record DensityFunctionType<T extends DensityFunction.Direct>(
		Identifier id,
		Function<Codec<DensityFunction>, MapCodec<T>> codecFactory
) {
	public MapCodec<T> codec(Codec<DensityFunction> densityFunctionCodec) {
		return this.codecFactory.apply(densityFunctionCodec);
	}
}