package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.Identifier;

public class JStackedResult extends JResult {
	public static final Codec<JStackedResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("item").forGetter(JStackedResult::getItem),
			DataComponentPatch.CODEC.fieldOf("components").forGetter(JStackedResult::getComponents),
			Codec.INT.fieldOf("count").forGetter(JStackedResult::getCount)
	).apply(instance, JStackedResult::new));
	protected Integer count;

	JStackedResult(final Identifier item) {
		super(item);
	}

	public JStackedResult(Identifier item, DataComponentPatch components, Integer count) {
		super(item, components);
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}
}
