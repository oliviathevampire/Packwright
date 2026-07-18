package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.util.DynamicMap;

public class StackedResult extends Result {
	public static final Codec<StackedResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(StackedResult::getItemId),
			DynamicMap.CODEC.fieldOf("components").forGetter(StackedResult::getComponents),
			Codec.INT.fieldOf("count").forGetter(StackedResult::getCount)
	).apply(instance, StackedResult::new));
	protected Integer count;

	StackedResult(final Identifier item) {
		super(item);
	}

	public StackedResult(Identifier item, DynamicMap components, Integer count) {
		super(item, components);
		this.count = count;
	}

	public int getCount() {
		return count;
	}
}
