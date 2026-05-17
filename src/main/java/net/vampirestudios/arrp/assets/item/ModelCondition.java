// src/.../model/ModelCondition.java
package net.vampirestudios.arrp.assets.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.assets.item.Property;
import net.vampirestudios.arrp.assets.item.Tint;

/**
 * "minecraft:condition" — property MUST resolve to a boolean at runtime.
 * Inlines Property at the root. Requires on_true, optional on_false.
 */
public class ModelCondition extends ItemModel {
	public static final String TYPE = "minecraft:condition";

	public static final Codec<ModelCondition> CODEC = Codec.of(
			// Encoder
			new Encoder<>() {
				@Override
				public <T> DataResult<T> encode(ModelCondition v, DynamicOps<T> ops, T prefix) {
					if (v.property == null) return DataResult.error(() -> "condition: missing property");
					if (v.onTrue == null) return DataResult.error(() -> "condition: missing 'on_true'");

					var b = ops.mapBuilder();

					// base: tints[], fallback
					if (v.tints != null && !v.tints.isEmpty()) {
						var tintsNode = Tint.CODEC.listOf().encodeStart(ops, v.tints);
						if (tintsNode.result().isEmpty()) return tintsNode;
						b.add(ops.createString("tints"), tintsNode.result().get());
					}
					if (v.fallback != null) {
						var fbNode = ItemModel.CODEC.encodeStart(ops, v.fallback);
						if (fbNode.result().isEmpty()) return fbNode;
						b.add(ops.createString("fallback"), fbNode.result().get());
					}

					// inline property
					var propEl = Property.CODEC.encodeStart(ops, v.property);
					if (propEl.result().isEmpty()) return propEl;
					var propMap = ops.getMap(propEl.result().get());
					if (propMap.result().isEmpty())
						return DataResult.error(() -> "condition: property didn't encode to object");
					for (var e : propMap.result().get().entries().toList()) b.add(e.getFirst(), e.getSecond());

					// on_true
					var t = ItemModel.CODEC.encodeStart(ops, v.onTrue);
					if (t.result().isEmpty()) return t;
					b.add(ops.createString("on_true"), t.result().get());

					// on_false (optional)
					if (v.onFalse != null) {
						var f = ItemModel.CODEC.encodeStart(ops, v.onFalse);
						if (f.result().isEmpty()) return f;
						b.add(ops.createString("on_false"), f.result().get());
					}
					return b.build(prefix);
				}
			},
			// Decoder
			new Decoder<>() {
				@Override
				public <T> DataResult<Pair<ModelCondition, T>> decode(DynamicOps<T> ops, T input) {
					var mapRes = ops.getMap(input);
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "condition: expected object");
					var map = mapRes.result().get();

					// base
					var tintsNode = map.get("tints");
					java.util.List<Tint> tints = null;
					if (tintsNode != null) {
						var tRes = Tint.CODEC.listOf().decode(ops, tintsNode);
						if (tRes.result().isEmpty()) return DataResult.error(() -> "condition: invalid tints");
						tints = tRes.result().get().getFirst();
					}
					var fbNode = map.get("fallback");
					ItemModel fallback = null;
					if (fbNode != null) {
						var fRes = ItemModel.CODEC.decode(ops, fbNode);
						if (fRes.result().isEmpty()) return DataResult.error(() -> "condition: invalid fallback");
						fallback = fRes.result().get().getFirst();
					}

					// inline property (boolean kind at runtime)
					var propRes = Property.CODEC.decode(ops, input);
					if (propRes.result().isEmpty())
						return DataResult.error(() -> "condition: invalid/missing property");
					var property = propRes.result().get().getFirst();

					// on_true (required)
					var onTrueNode = map.get("on_true");
					if (onTrueNode == null) return DataResult.error(() -> "condition: missing 'on_true'");
					var onTrueRes = ItemModel.CODEC.decode(ops, onTrueNode);
					if (onTrueRes.result().isEmpty()) return DataResult.error(() -> "condition: invalid 'on_true'");
					var onTrue = onTrueRes.result().get().getFirst();

					// on_false (optional)
					var onFalseNode = map.get("on_false");
					ItemModel onFalse = null;
					if (onFalseNode != null) {
						var onFalseRes = ItemModel.CODEC.decode(ops, onFalseNode);
						if (onFalseRes.result().isEmpty())
							return DataResult.error(() -> "condition: invalid 'on_false'");
						onFalse = onFalseRes.result().get().getFirst();
					}

					var m = new ModelCondition();
					m.type = TYPE;
					m.property = property;
					m.onTrue = onTrue;
					m.onFalse = onFalse;
					if (tints != null && !tints.isEmpty()) for (var t : tints) m.tint(t);
					if (fallback != null) m.fallback(fallback);
					return DataResult.success(Pair.of(m, input));
				}
			}
	);

	static {
		ItemModel.register(TYPE, MapCodec.assumeMapUnsafe(CODEC).xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private Property property;        // boolean property
	private ItemModel onTrue;          // required
	private ItemModel onFalse;         // optional

	public ModelCondition() {
		super(TYPE);
	}

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
