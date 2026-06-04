package net.vampirestudios.arrp.assets.item.models;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.ItemModel;
import net.vampirestudios.arrp.assets.item.tints.Tint;
import net.vampirestudios.arrp.assets.models.Transformation;

import java.util.ArrayList;
import java.util.List;

public final class ModelComposite extends ItemModel {
	public static final String TYPE = "minecraft:composite";
	public static final MapCodec<ModelComposite> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			// base fields first
			Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelComposite::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelComposite::codecGetFallback),
			Transformation.CODEC.optionalFieldOf("transformation").forGetter(ModelComposite::codecGetTransformation),
			// subtype
			LAZY_SELF.listOf().fieldOf("parts").forGetter(ModelComposite::getParts)
	).apply(i, (tints, fallback, transformation, parts) -> {
		ModelComposite m = new ModelComposite();
		m.parts = parts;
		ItemModel.applyBase(m, tints, fallback, transformation);
		return m;
	}));

	static {
		ItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private List<ItemModel> parts;

	public ModelComposite() {
		super(TYPE);
		this.parts = new ArrayList<>();
	}

	// fluent adders
	public ModelComposite model(ItemModel child) {
		this.parts.add(child);
		return this;
	}

	public ModelComposite models(java.util.Collection<? extends ItemModel> children) {
		this.parts.addAll(children);
		return this;
	}

	// getter for codec
	public List<ItemModel> getParts() {
		return parts;
	}
}
