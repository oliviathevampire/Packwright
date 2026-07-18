package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record EnvironmentAttributeNumberProvider(Identifier attribute) implements NumberProvider {
	public static final MapCodec<EnvironmentAttributeNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:environment_attribute"),
			Identifier.CODEC.fieldOf("attribute").forGetter(EnvironmentAttributeNumberProvider::attribute)
	).apply(i, (type, attribute) -> new EnvironmentAttributeNumberProvider(attribute)));
}
