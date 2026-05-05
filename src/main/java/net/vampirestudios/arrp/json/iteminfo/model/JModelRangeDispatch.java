package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.json.iteminfo.property.JProperty;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a range dispatch model type "minecraft:range_dispatch".
 */
public class JModelRangeDispatch extends JItemModel {
	public static final String TYPE = "minecraft:range_dispatch";

	public static final Codec<JModelRangeDispatch> CODEC = Codec.of(
			// Encoder
			new Encoder<>() {
				@Override
				public <T> DataResult<T> encode(JModelRangeDispatch v, DynamicOps<T> ops, T prefix) {
					if (v.property == null) return DataResult.error(() -> "range_dispatch: missing property");
					if (v.entries == null || v.entries.isEmpty())
						return DataResult.error(() -> "range_dispatch: empty 'entries'");
					var b = ops.mapBuilder();

					// --- base ---
					if (v.tints != null && !v.tints.isEmpty()) {
						var tintsEl = JTint.CODEC.listOf().encodeStart(ops, v.tints);
						if (tintsEl.result().isEmpty()) return tintsEl;
						b.add(ops.createString("tints"), tintsEl.result().get());
					}
					if (v.fallback != null) {
						var fbEl = JItemModel.CODEC.encodeStart(ops, v.fallback);
						if (fbEl.result().isEmpty()) return fbEl;
						b.add(ops.createString("fallback"), fbEl.result().get());
					}

					// inline property
					var propEl = JProperty.CODEC.encodeStart(ops, v.property);
					if (propEl.result().isEmpty()) return propEl;
					var propMap = ops.getMap(propEl.result().get());
					if (propMap.result().isEmpty())
						return DataResult.error(() -> "range_dispatch: property didn't encode to object");
					for (var e : propMap.result().get().entries().toList()) b.add(e.getFirst(), e.getSecond());

					// scale (omit if 1.0)
					if (v.scale != 1.0) b.add(ops.createString("scale"), ops.createDouble(v.scale));

					// entries[]
					var entriesEl = JRangeEntry.CODEC.listOf().encodeStart(ops, v.entries);
					if (entriesEl.result().isEmpty()) return entriesEl;
					b.add(ops.createString("entries"), entriesEl.result().get());

					return b.build(prefix);
				}
			},
			// Decoder
			new Decoder<>() {
				@Override
				public <T> DataResult<Pair<JModelRangeDispatch, T>> decode(DynamicOps<T> ops, T input) {
					var mapRes = ops.getMap(input);
					if (mapRes.result().isEmpty()) return DataResult.error(() -> "range_dispatch: expected object");
					var map = mapRes.result().get();

					// --- base ---
					java.util.List<JTint> tints = null;
					var tNode = map.get("tints");
					if (tNode != null) {
						var tRes = JTint.CODEC.listOf().decode(ops, tNode);
						if (tRes.result().isEmpty()) return DataResult.error(() -> "range_dispatch: invalid tints");
						tints = tRes.result().get().getFirst();
					}
					JItemModel fallback = null;
					var fbNode = map.get("fallback");
					if (fbNode != null) {
						var fbRes = JItemModel.CODEC.decode(ops, fbNode);
						if (fbRes.result().isEmpty()) return DataResult.error(() -> "range_dispatch: invalid fallback");
						fallback = fbRes.result().get().getFirst();
					}

					// inline property
					var propRes = JProperty.CODEC.decode(ops, input);
					if (propRes.result().isEmpty())
						return DataResult.error(() -> "range_dispatch: invalid/missing property");

					// scale
					Double scale = null;
					var scaleNum = ops.getNumberValue(map.get("scale"));
					if (scaleNum.result().isPresent()) scale = scaleNum.result().get().doubleValue();

					// entries[]
					var entriesNode = map.get("entries");
					if (entriesNode == null) return DataResult.error(() -> "range_dispatch: missing 'entries'");
					var entriesRes = JRangeEntry.CODEC.listOf().decode(ops, entriesNode);
					if (entriesRes.result().isEmpty())
						return DataResult.error(() -> "range_dispatch: invalid 'entries'");

					var m = new JModelRangeDispatch();
					m.type = TYPE;
					m.property = propRes.result().get().getFirst();
					m.scale = (scale == null) ? 1.0 : scale;
					m.entries = entriesRes.result().get().getFirst();
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

	private JProperty property;
	private double scale = 1.0;
	private List<JRangeEntry> entries;

	public JModelRangeDispatch() {
		super("minecraft:range_dispatch");
		this.entries = new ArrayList<>();
	}

	public JModelRangeDispatch(JProperty property, double scale, List<JRangeEntry> entries) {
		this();
		this.property = property;
		this.scale = scale;
		this.entries = entries;
	}

	// Getters and Setters
	public JProperty getProperty() {
		return property;
	}

	public JModelRangeDispatch property(JProperty property) {
		this.property = property;
		return this;
	}

	public double getScale() {
		return scale;
	}

	public JModelRangeDispatch scale(double scale) {
		this.scale = scale;
		return this;
	}

	public List<JRangeEntry> getEntries() {
		return entries;
	}

	public JModelRangeDispatch entries(List<JRangeEntry> entries) {
		this.entries = entries;
		return this;
	}

	public JModelRangeDispatch entry(JRangeEntry entry) {
		this.entries.add(entry);
		return this;
	}
}
