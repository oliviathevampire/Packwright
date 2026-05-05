// src/.../model/JModelCondition.java
package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.json.iteminfo.property.JProperty;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

/**
 * "minecraft:condition" — property MUST resolve to a boolean at runtime.
 * Inlines JProperty at the root. Requires on_true, optional on_false.
 */
public class JModelCondition extends JItemModel {
	public static final String TYPE = "minecraft:condition";

	public static final Codec<JModelCondition> CODEC = Codec.of(
			// Encoder
			new Encoder<>() {
				@Override
				public <T> DataResult<T> encode(JModelCondition v, DynamicOps<T> ops, T prefix) {
					if (v.property == null) return DataResult.error(() -> "condition: missing property");
					if (v.onTrue == null) return DataResult.error(() -> "condition: missing 'on_true'");

					var b = ops.mapBuilder();

					// base: tints[], fallback
					if (v.tints != null && !v.tints.isEmpty()) {
						var tintsNode = JTint.CODEC.listOf().encodeStart(ops, v.tints);
						if (tintsNode.result().isEmpty()) return tintsNode;
						b.add(ops.createString("tints"), tintsNode.result().get());
					}
					if (v.fallback != null) {
						var fbNode = JItemModel.CODEC.encodeStart(ops, v.fallback);
						if (fbNode.result().isEmpty()) return fbNode;
						b.add(ops.createString("fallback"), fbNode.result().get());
					}

					// inline property
					var propEl = JProperty.CODEC.encodeStart(ops, v.property);
					if (propEl.result().isEmpty()) return propEl;
					var propMap = ops.getMap(propEl.result().get());
					if (propMap.result().isEmpty())
						return DataResult.error(() -> "condition: property didn't encode to object");
					for (var e : propMap.result().get().entries().toList()) b.add(e.getFirst(), e.getSecond());

					// on_true
					var t = JItemModel.CODEC.encodeStart(ops, v.onTrue);
					if (t.result().isEmpty()) return t;
					b.add(ops.createString("on_true"), t.result().get());

					// on_false (optional)
					if (v.onFalse != null) {
						var f = JItemModel.CODEC.encodeStart(ops, v.onFalse);
						if (f.result().isEmpty()) return f;
						b.add(ops.createString("on_false"), f.result().get());
					}
					return b.build(prefix);
				}
			},
			// Decoder
			new Decoder<>() {
				@Override
				public <T> DataResult<Pair<JModelCondition, T>> decode(DynamicOps<T> ops, T input) {
					var mapRes = ops.getMap(input);
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "condition: expected object");
					var map = mapRes.result().get();

					// base
					var tintsNode = map.get("tints");
					java.util.List<JTint> tints = null;
					if (tintsNode != null) {
						var tRes = JTint.CODEC.listOf().decode(ops, tintsNode);
						if (tRes.result().isEmpty()) return DataResult.error(() -> "condition: invalid tints");
						tints = tRes.result().get().getFirst();
					}
					var fbNode = map.get("fallback");
					JItemModel fallback = null;
					if (fbNode != null) {
						var fRes = JItemModel.CODEC.decode(ops, fbNode);
						if (fRes.result().isEmpty()) return DataResult.error(() -> "condition: invalid fallback");
						fallback = fRes.result().get().getFirst();
					}

					// inline property (boolean kind at runtime)
					var propRes = JProperty.CODEC.decode(ops, input);
					if (propRes.result().isEmpty())
						return DataResult.error(() -> "condition: invalid/missing property");
					var property = propRes.result().get().getFirst();

					// on_true (required)
					var onTrueNode = map.get("on_true");
					if (onTrueNode == null) return DataResult.error(() -> "condition: missing 'on_true'");
					var onTrueRes = JItemModel.CODEC.decode(ops, onTrueNode);
					if (onTrueRes.result().isEmpty()) return DataResult.error(() -> "condition: invalid 'on_true'");
					var onTrue = onTrueRes.result().get().getFirst();

					// on_false (optional)
					var onFalseNode = map.get("on_false");
					JItemModel onFalse = null;
					if (onFalseNode != null) {
						var onFalseRes = JItemModel.CODEC.decode(ops, onFalseNode);
						if (onFalseRes.result().isEmpty())
							return DataResult.error(() -> "condition: invalid 'on_false'");
						onFalse = onFalseRes.result().get().getFirst();
					}

					var m = new JModelCondition();
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
		JItemModel.register(TYPE, MapCodec.assumeMapUnsafe(CODEC).xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private JProperty property;        // boolean property
	private JItemModel onTrue;          // required
	private JItemModel onFalse;         // optional

	public JModelCondition() {
		super(TYPE);
	}

	public JModelCondition property(JProperty p) {
		this.property = p;
		return this;
	}

	public JModelCondition onTrue(JItemModel m) {
		this.onTrue = m;
		return this;
	}

	public JModelCondition onFalse(JItemModel m) {
		this.onFalse = m;
		return this;
	}

	public static Builder builder() { return new Builder(); }
	public static final class Builder {
		private JProperty property;
		private JItemModel onTrue, onFalse;
		private final java.util.List<JTint> tints = new java.util.ArrayList<>();
		private JItemModel fallback;

		public Builder property(JProperty p) { this.property = p; return this; }
		public Builder then(JItemModel m) { this.onTrue = m; return this; }
		public Builder otherwise(JItemModel m) { this.onFalse = m; return this; }
		public Builder tint(JTint t) { if (t!=null) tints.add(t); return this; }
		public Builder tints(java.util.Collection<? extends JTint> ts) { if (ts!=null) tints.addAll(ts); return this; }
		public Builder fallback(JItemModel fb) { this.fallback = fb; return this; }

		public JModelCondition build() {
			var c = new JModelCondition();
			c.property = java.util.Objects.requireNonNull(property, "property");
			c.onTrue   = java.util.Objects.requireNonNull(onTrue, "on_true");
			c.onFalse  = onFalse;
			if (!tints.isEmpty()) c.tints(tints);
			if (fallback != null) c.fallback(fallback);
			return c;
		}
	}

	// sugar
	public static JModelCondition when(JProperty p, JItemModel thenModel) {
		return builder().property(p).then(thenModel).build();
	}
	public static JModelCondition when(JProperty p, JItemModel thenModel, JItemModel elseModel) {
		return builder().property(p).then(thenModel).otherwise(elseModel).build();
	}
}
