package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.json.iteminfo.property.JProperty;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a select model type "minecraft:select".
 */
public class JModelSelect extends JItemModel {
	public static final String TYPE = "minecraft:select";
	public static final Codec<JModelSelect> CODEC = Codec.of(
			// Encoder
			new Encoder<>() {
				@Override public <T> DataResult<T> encode(JModelSelect v, DynamicOps<T> ops, T prefix) {
					if (v.property == null) return DataResult.error(() -> "JModelSelect: missing property");
					if (v.cases == null || v.cases.isEmpty()) return DataResult.error(() -> "JModelSelect: empty 'cases'");

					var builder = ops.mapBuilder();

					// --- base ---
					if (v.tints != null && !v.tints.isEmpty()) {
						var tintsRes = JTint.CODEC.listOf().encode(v.tints, ops, ops.empty());
						if (tintsRes.result().isEmpty()) return tintsRes;
						builder.add(ops.createString("tints"), tintsRes.result().get());
					}
					if (v.fallback != null) {
						var fbRes = JItemModel.CODEC.encode(v.fallback, ops, ops.empty());
						if (fbRes.result().isEmpty()) return fbRes;
						builder.add(ops.createString("fallback"), fbRes.result().get());
					}

					// inline property
					var propRes = JProperty.CODEC.encode(v.property, ops, ops.empty());
					if (propRes.result().isEmpty()) return propRes;
					var mapRes = ops.getMap(propRes.result().get());
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "JModelSelect: property didn't encode to an object");
					for (var e : mapRes.result().get().entries().toList()) builder.add(e.getFirst(), e.getSecond());

					// cases[]
					var casesRes = JSelectCase.CODEC.listOf().encode(v.cases, ops, ops.empty());
					if (casesRes.result().isEmpty()) return casesRes;
					builder.add(ops.createString("cases"), casesRes.result().get());

					return builder.build(prefix);
				}
			},
			// Decoder
			new Decoder<>() {
				@Override public <T> DataResult<Pair<JModelSelect, T>> decode(DynamicOps<T> ops, T input) {
					var mapRes = ops.getMap(input);
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "JModelSelect: expected object");
					var map = mapRes.result().get();

					// --- base ---
					java.util.List<JTint> tints = null;
					var tNode = map.get("tints");
					if (tNode != null) {
						var tRes = JTint.CODEC.listOf().decode(ops, tNode);
						if (tRes.result().isEmpty()) return DataResult.error(() -> "JModelSelect: invalid tints");
						tints = tRes.result().get().getFirst();
					}
					JItemModel fallback = null;
					var fbNode = map.get("fallback");
					if (fbNode != null) {
						var fbRes = JItemModel.CODEC.decode(ops, fbNode);
						if (fbRes.result().isEmpty()) return DataResult.error(() -> "JModelSelect: invalid fallback");
						fallback = fbRes.result().get().getFirst();
					}

					// inline property
					var propRes = JProperty.CODEC.decode(ops, input);
					if (propRes.result().isEmpty()) return DataResult.error(() -> "JModelSelect: invalid/missing inlined property");

					var casesNode = map.get("cases");
					if (casesNode == null) return DataResult.error(() -> "JModelSelect: missing 'cases'");
					var casesRes = JSelectCase.CODEC.listOf().decode(ops, casesNode);
					if (casesRes.result().isEmpty()) return DataResult.error(() -> "JModelSelect: invalid 'cases'");

					var m = new JModelSelect();
					m.type = TYPE;
					m.property = propRes.result().get().getFirst();
					m.cases = casesRes.result().get().getFirst();
					if (tints != null && !tints.isEmpty()) for (var t : tints) m.tint(t);
					if (fallback != null) m.fallback(fallback);
					return DataResult.success(Pair.of(m, input));
				}
			}
	);

	// ---- Registry hook (do this once, e.g., in a static block) ----
	static {
		JItemModel.register(TYPE, MapCodec.assumeMapUnsafe(CODEC).xmap(m -> { m.type = TYPE; return m; }, m -> m));
	}

	private JProperty property;
	private List<JSelectCase> cases;

	public JModelSelect() {
		super(TYPE);
		this.cases = new ArrayList<>();
	}

	// Getters and Setters
	public JProperty getProperty() {
		return property;
	}

	public JModelSelect property(JProperty property) {
		this.property = property;
		return this;
	}

	public List<JSelectCase> getCases() {
		return cases;
	}

	public JModelSelect cases(List<JSelectCase> cases) {
		this.cases = cases;
		return this;
	}

	public JModelSelect addCase(JSelectCase selectCase) {
		this.cases.add(selectCase);
		return this;
	}
}
