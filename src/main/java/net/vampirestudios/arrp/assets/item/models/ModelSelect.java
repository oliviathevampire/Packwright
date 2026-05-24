package net.vampirestudios.arrp.assets.item.models;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.assets.item.ItemModel;
import net.vampirestudios.arrp.assets.item.properties.Property;
import net.vampirestudios.arrp.assets.item.SelectCase;
import net.vampirestudios.arrp.assets.item.tints.Tint;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a select model type "minecraft:select".
 */
public class ModelSelect extends ItemModel {
	public static final String TYPE = "minecraft:select";
	public static final Codec<ModelSelect> CODEC = Codec.of(
			// Encoder
			new Encoder<>() {
				@Override public <T> DataResult<T> encode(ModelSelect v, DynamicOps<T> ops, T prefix) {
					if (v.property == null) return DataResult.error(() -> "ModelSelect: missing property");
					if (v.cases == null || v.cases.isEmpty()) return DataResult.error(() -> "ModelSelect: empty 'cases'");

					var builder = ops.mapBuilder();

					// --- base ---
					if (v.tints != null && !v.tints.isEmpty()) {
						var tintsRes = Tint.CODEC.listOf().encode(v.tints, ops, ops.empty());
						if (tintsRes.result().isEmpty()) return tintsRes;
						builder.add(ops.createString("tints"), tintsRes.result().get());
					}
					if (v.fallback != null) {
						var fbRes = ItemModel.CODEC.encode(v.fallback, ops, ops.empty());
						if (fbRes.result().isEmpty()) return fbRes;
						builder.add(ops.createString("fallback"), fbRes.result().get());
					}

					// inline property
					var propRes = Property.CODEC.encode(v.property, ops, ops.empty());
					if (propRes.result().isEmpty()) return propRes;
					var mapRes = ops.getMap(propRes.result().get());
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "ModelSelect: property didn't encode to an object");
					for (var e : mapRes.result().get().entries().toList()) builder.add(e.getFirst(), e.getSecond());

					// cases[]
					var casesRes = SelectCase.CODEC.listOf().encode(v.cases, ops, ops.empty());
					if (casesRes.result().isEmpty()) return casesRes;
					builder.add(ops.createString("cases"), casesRes.result().get());

					return builder.build(prefix);
				}
			},
			// Decoder
			new Decoder<>() {
				@Override public <T> DataResult<Pair<ModelSelect, T>> decode(DynamicOps<T> ops, T input) {
					var mapRes = ops.getMap(input);
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "ModelSelect: expected object");
					var map = mapRes.result().get();

					// --- base ---
					java.util.List<Tint> tints = null;
					var tNode = map.get("tints");
					if (tNode != null) {
						var tRes = Tint.CODEC.listOf().decode(ops, tNode);
						if (tRes.result().isEmpty()) return DataResult.error(() -> "ModelSelect: invalid tints");
						tints = tRes.result().get().getFirst();
					}
					ItemModel fallback = null;
					var fbNode = map.get("fallback");
					if (fbNode != null) {
						var fbRes = ItemModel.CODEC.decode(ops, fbNode);
						if (fbRes.result().isEmpty()) return DataResult.error(() -> "ModelSelect: invalid fallback");
						fallback = fbRes.result().get().getFirst();
					}

					// inline property
					var propRes = Property.CODEC.decode(ops, input);
					if (propRes.result().isEmpty()) return DataResult.error(() -> "ModelSelect: invalid/missing inlined property");

					var casesNode = map.get("cases");
					if (casesNode == null) return DataResult.error(() -> "ModelSelect: missing 'cases'");
					var casesRes = SelectCase.CODEC.listOf().decode(ops, casesNode);
					if (casesRes.result().isEmpty()) return DataResult.error(() -> "ModelSelect: invalid 'cases'");

					var m = new ModelSelect();
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
		ItemModel.register(TYPE, MapCodec.assumeMapUnsafe(CODEC).xmap(m -> { m.type = TYPE; return m; }, m -> m));
	}

	private Property property;
	private List<SelectCase> cases;

	public ModelSelect() {
		super(TYPE);
		this.cases = new ArrayList<>();
	}

	// Getters and Setters
	public Property getProperty() {
		return property;
	}

	public ModelSelect property(Property property) {
		this.property = property;
		return this;
	}

	public List<SelectCase> getCases() {
		return cases;
	}

	public ModelSelect cases(List<SelectCase> cases) {
		this.cases = cases;
		return this;
	}

	public ModelSelect addCase(SelectCase selectCase) {
		this.cases.add(selectCase);
		return this;
	}
}
