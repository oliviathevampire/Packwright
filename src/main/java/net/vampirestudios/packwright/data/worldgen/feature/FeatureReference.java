package net.vampirestudios.packwright.data.worldgen.feature;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public record FeatureReference(Identifier id, Feature inline) {
	public static final Codec<FeatureReference> CODEC = Codec.either(Identifier.CODEC, Feature.CODEC).xmap(
			either -> either.map(FeatureReference::id, FeatureReference::inline),
			reference -> reference.id != null ? Either.left(reference.id) : Either.right(reference.inline)
	);

	public static FeatureReference id(Identifier id) {
		return new FeatureReference(id, null);
	}

	public static FeatureReference inline(Feature feature) {
		return new FeatureReference(null, feature);
	}
}
