package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * Represents a basic model type "minecraft:model".
 */
public class ModelBasic extends ItemModel {
	public static final String TYPE = "minecraft:model";
	public static final MapCodec<ModelBasic> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			// base fields first
			Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelBasic::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelBasic::codecGetFallback),
			Identifier.CODEC.fieldOf("model").forGetter(ModelBasic::getModel)
	).apply(i, (tint, fallback, model) -> {
		ModelBasic m = new ModelBasic(model);
		ItemModel.applyBase(m, tint, fallback);
		return m;
	}));

	static {
		ItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private final Identifier model;

	public ModelBasic(Identifier model) {
		super(TYPE);
		this.model = model;
	}

	// factories (unchanged)
	public static ModelBasic of(Identifier model) {
		return new ModelBasic(model);
	}

	// Getters and Setters
	public Identifier getModel() {
		return model;
	}
}
