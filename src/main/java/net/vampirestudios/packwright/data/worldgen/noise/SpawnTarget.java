package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Parameter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * One {@code spawn_target} entry of a noise settings file: fitness constraints for
 * the world-spawn search, keyed by {@code worldgen/density_function} reference.
 *
 * <pre>{@code
 * {
 *   "minecraft:overworld/continents": [-0.11, 1.0],
 *   "minecraft:overworld/erosion": [-1.0, 1.0]
 * }
 * }</pre>
 */
public record SpawnTarget(Map<Identifier, Parameter> parameters) {

	public static final Codec<SpawnTarget> CODEC =
			Codec.unboundedMap(Identifier.CODEC, Parameter.CODEC)
					.xmap(SpawnTarget::new, SpawnTarget::parameters);

	public SpawnTarget {
		Objects.requireNonNull(parameters, "parameters");
		// keep insertion order so the serialized pack stays byte-stable across runs
		parameters = Collections.unmodifiableMap(new LinkedHashMap<>(parameters));
	}

	public static Builder target() {
		return new Builder();
	}

	public static final class Builder {
		private final Map<Identifier, Parameter> parameters = new LinkedHashMap<>();

		private Builder() {}

		/** constrain the value of a referenced density function at the spawn column */
		public Builder parameter(Identifier densityFunction, Parameter parameter) {
			this.parameters.put(
					Objects.requireNonNull(densityFunction, "densityFunction"),
					Objects.requireNonNull(parameter, "parameter")
			);
			return this;
		}

		public SpawnTarget build() {
			return new SpawnTarget(this.parameters);
		}
	}
}
