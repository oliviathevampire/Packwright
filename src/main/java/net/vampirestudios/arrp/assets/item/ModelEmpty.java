package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.Tint;

public final class ModelEmpty extends ItemModel {
	public static final String TYPE = "minecraft:empty";

	public static final MapCodec<ModelEmpty> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			// base fields first
			Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelEmpty::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelEmpty::codecGetFallback)
	).apply(i, (tints, fallback) -> {
		ModelEmpty m = new ModelEmpty();
		ItemModel.applyBase(m, tints, fallback);
		return m;
	}));

	static {
		ItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	public ModelEmpty() {
		super(TYPE);
	}
}
