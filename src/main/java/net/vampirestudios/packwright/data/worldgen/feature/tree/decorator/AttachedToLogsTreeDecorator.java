package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

import java.util.List;

public record AttachedToLogsTreeDecorator(float probability, BlockStateProvider provider, List<String> directions) implements TreeDecorator {
	public static final MapCodec<AttachedToLogsTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:attached_to_logs"),
			Codec.FLOAT.fieldOf("probability").forGetter(AttachedToLogsTreeDecorator::probability),
			BlockStateProvider.CODEC.fieldOf("provider").forGetter(AttachedToLogsTreeDecorator::provider),
			Codec.STRING.listOf().fieldOf("directions").forGetter(AttachedToLogsTreeDecorator::directions)
	).apply(i, (type, probability, provider, directions) -> new AttachedToLogsTreeDecorator(probability, provider, directions)));
}
