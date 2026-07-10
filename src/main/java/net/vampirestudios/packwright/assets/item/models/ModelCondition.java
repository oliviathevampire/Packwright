package net.vampirestudios.packwright.assets.item.models;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.properties.Property;
import net.vampirestudios.packwright.assets.item.tints.Tint;
import net.vampirestudios.packwright.assets.models.Transformation;

/**
 * "minecraft:condition" — property MUST resolve to a boolean at runtime.
 * Inlines Property at the root. Requires on_true, optional on_false.
 */
public class ModelCondition extends ItemModel {
	public static final String TYPE = "minecraft:condition";

	public static final MapCodec<ModelCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelCondition::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelCondition::codecGetFallback),
			Transformation.CODEC.optionalFieldOf("transformation").forGetter(ModelCondition::codecGetTransformation),
			Property.MAP_CODEC.forGetter(ModelCondition::getProperty),
			LAZY_SELF.fieldOf("on_true").forGetter(ModelCondition::getOnTrue),
			LAZY_SELF.optionalFieldOf("on_false").forGetter(ModelCondition::codecGetOnFalse)
	).apply(i, (tints, fallback, transformation, property, onTrue, onFalse) -> {
		ModelCondition m = new ModelCondition();
		applyBase(m, tints, fallback, transformation);
		m.property = property;
		m.onTrue = onTrue;
		onFalse.ifPresent(f -> m.onFalse = f);
		return m;
	}));

	static {
		ItemModel.register(TYPE, CODEC);
	}

	private Property property;        // boolean property
	private ItemModel onTrue;          // required
	private ItemModel onFalse;         // optional

	public ModelCondition() {
		super(TYPE);
	}

	public Property getProperty() { return property; }
	public ItemModel getOnTrue() { return onTrue; }
	public java.util.Optional<ItemModel> codecGetOnFalse() { return java.util.Optional.ofNullable(onFalse); }

	public ModelCondition property(Property p) {
		this.property = p;
		return this;
	}

	public ModelCondition onTrue(ItemModel m) {
		this.onTrue = m;
		return this;
	}

	public ModelCondition onFalse(ItemModel m) {
		this.onFalse = m;
		return this;
	}

	public static Builder builder() { return new Builder(); }
	public static final class Builder {
		private Property property;
		private ItemModel onTrue, onFalse;
		private final java.util.List<Tint> tints = new java.util.ArrayList<>();
		private ItemModel fallback;

		public Builder property(Property p) { this.property = p; return this; }
		public Builder then(ItemModel m) { this.onTrue = m; return this; }
		public Builder otherwise(ItemModel m) { this.onFalse = m; return this; }
		public Builder tint(Tint t) { if (t!=null) tints.add(t); return this; }
		public Builder tints(java.util.Collection<? extends Tint> ts) { if (ts!=null) tints.addAll(ts); return this; }
		public Builder fallback(ItemModel fb) { this.fallback = fb; return this; }

		public ModelCondition build() {
			var c = new ModelCondition();
			c.property = java.util.Objects.requireNonNull(property, "property");
			c.onTrue   = java.util.Objects.requireNonNull(onTrue, "on_true");
			c.onFalse  = onFalse;
			if (!tints.isEmpty()) c.tints(tints);
			if (fallback != null) c.fallback(fallback);
			return c;
		}
	}

	// sugar
	public static ModelCondition when(Property p, ItemModel thenModel) {
		return builder().property(p).then(thenModel).build();
	}
	public static ModelCondition when(Property p, ItemModel thenModel, ItemModel elseModel) {
		return builder().property(p).then(thenModel).otherwise(elseModel).build();
	}
}
