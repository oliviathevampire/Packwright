package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class DensityFunctionTypes {
	private static final Map<Identifier, DensityFunctionType<?>> TYPES =
			new LinkedHashMap<>();

	private static final Codec<DensityFunctionType<?>> TYPE_CODEC =
			Identifier.CODEC.comapFlatMap(
					id -> {
						DensityFunctionType<?> type = TYPES.get(id);

						return type != null
								? DataResult.success(type)
								: DataResult.error(
										() -> "Unknown density function type: " + id
								);
					},
					DensityFunctionType::id
			);

	public static final DensityFunctionType<DensityFunctions.Constant> CONSTANT =
			register("constant", DensityFunctions.Constant::codec);

	public static final DensityFunctionType<DensityFunctions.Noise> NOISE =
			register("noise", DensityFunctions.Noise::codec);

	public static final DensityFunctionType<DensityFunctions.YClampedGradient> Y_CLAMPED_GRADIENT =
			register("y_clamped_gradient", DensityFunctions.YClampedGradient::codec);

	public static final DensityFunctionType<DensityFunctions.Mapped> ABS =
			register(
					"abs",
					codec -> DensityFunctions.Mapped.codec(
							codec,
							DensityFunctions.Mapped.Type.ABS
					)
			);

	public static final DensityFunctionType<DensityFunctions.Mapped> SQUARE =
			register(
					"square",
					codec -> DensityFunctions.Mapped.codec(
							codec,
							DensityFunctions.Mapped.Type.SQUARE
					)
			);

	public static final DensityFunctionType<DensityFunctions.Mapped> CUBE =
			register(
					"cube",
					codec -> DensityFunctions.Mapped.codec(
							codec,
							DensityFunctions.Mapped.Type.CUBE
					)
			);

	public static final DensityFunctionType<DensityFunctions.Mapped> HALF_NEGATIVE =
			register(
					"half_negative",
					codec -> DensityFunctions.Mapped.codec(
							codec,
							DensityFunctions.Mapped.Type.HALF_NEGATIVE
					)
			);

	public static final DensityFunctionType<DensityFunctions.Mapped> QUARTER_NEGATIVE =
			register(
					"quarter_negative",
					codec -> DensityFunctions.Mapped.codec(
							codec,
							DensityFunctions.Mapped.Type.QUARTER_NEGATIVE
					)
			);

	public static final DensityFunctionType<DensityFunctions.Mapped> SQUEEZE =
			register(
					"squeeze",
					codec -> DensityFunctions.Mapped.codec(
							codec,
							DensityFunctions.Mapped.Type.SQUEEZE
					)
			);

	public static final DensityFunctionType<DensityFunctions.Marker> INTERPOLATED =
			register(
					"interpolated",
					codec -> DensityFunctions.Marker.codec(
							codec,
							DensityFunctions.Marker.Type.INTERPOLATED
					)
			);

	public static final DensityFunctionType<DensityFunctions.Marker> FLAT_CACHE =
			register(
					"flat_cache",
					codec -> DensityFunctions.Marker.codec(
							codec,
							DensityFunctions.Marker.Type.FLAT_CACHE
					)
			);

	public static final DensityFunctionType<DensityFunctions.Marker> CACHE_2D =
			register(
					"cache_2d",
					codec -> DensityFunctions.Marker.codec(
							codec,
							DensityFunctions.Marker.Type.CACHE_2D
					)
			);

	public static final DensityFunctionType<DensityFunctions.Marker> CACHE_ONCE =
			register(
					"cache_once",
					codec -> DensityFunctions.Marker.codec(
							codec,
							DensityFunctions.Marker.Type.CACHE_ONCE
					)
			);

	public static final DensityFunctionType<DensityFunctions.TwoArgumentSimpleFunction> ADD =
			register(
					"add",
					codec -> DensityFunctions.TwoArgumentSimpleFunction.codec(
							codec,
							DensityFunctions.TwoArgumentSimpleFunction.Type.ADD
					)
			);

	public static final DensityFunctionType<DensityFunctions.TwoArgumentSimpleFunction> MUL =
			register(
					"mul",
					codec -> DensityFunctions.TwoArgumentSimpleFunction.codec(
							codec,
							DensityFunctions.TwoArgumentSimpleFunction.Type.MUL
					)
			);

	public static final DensityFunctionType<DensityFunctions.TwoArgumentSimpleFunction> MIN =
			register(
					"min",
					codec -> DensityFunctions.TwoArgumentSimpleFunction.codec(
							codec,
							DensityFunctions.TwoArgumentSimpleFunction.Type.MIN
					)
			);

	public static final DensityFunctionType<DensityFunctions.TwoArgumentSimpleFunction> MAX =
			register(
					"max",
					codec -> DensityFunctions.TwoArgumentSimpleFunction.codec(
							codec,
							DensityFunctions.TwoArgumentSimpleFunction.Type.MAX
					)
			);

	public static final DensityFunctionType<DensityFunctions.BlendDensity> BLEND_DENSITY =
			register("blend_density", DensityFunctions.BlendDensity::codec);

	public static final DensityFunctionType<DensityFunctions.Clamp> CLAMP =
			register("clamp", DensityFunctions.Clamp::codec);

	public static final DensityFunctionType<DensityFunctions.RangeChoice> RANGE_CHOICE =
			register("range_choice", DensityFunctions.RangeChoice::codec);

	private DensityFunctionTypes() {
	}

	static Codec<DensityFunction.Direct> directCodec(
			Codec<DensityFunction> densityFunctionCodec
	) {
		return TYPE_CODEC.dispatch(
				"type",
				DensityFunction.Direct::type,
				type -> codecFor(type, densityFunctionCodec)
		);
	}

	private static MapCodec<? extends DensityFunction.Direct> codecFor(
			DensityFunctionType<?> type,
			Codec<DensityFunction> densityFunctionCodec
	) {
		return type.codec(densityFunctionCodec);
	}

	private static <T extends DensityFunction.Direct> DensityFunctionType<T> register(
			String name,
			Function<Codec<DensityFunction>, com.mojang.serialization.MapCodec<T>> codec
	) {
		Identifier id = Identifier.fromNamespaceAndPath("minecraft", name);
		DensityFunctionType<T> type = new DensityFunctionType<>(id, codec);

		if (TYPES.putIfAbsent(id, type) != null) {
			throw new IllegalStateException(
					"Duplicate density function type: " + id
			);
		}

		return type;
	}
}