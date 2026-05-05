package net.vampirestudios.arrp.json.blockstate;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.json.codec.Codecs;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import java.util.*;
import java.util.stream.Collectors;

public final class JVariant implements Cloneable {
	private static final Codec<java.util.List<JBlockModel>> MODELS = Codecs.oneOrList(JBlockModel.CODEC);
	public static final Codec<JVariant> CODEC = Codec.unboundedMap(Codec.STRING, MODELS)
			.xmap(JVariant::fromMap, v -> v.models);

	private final Map<String, List<JBlockModel>> models = new HashMap<>();

	/**
	 * @see JState#variant()
	 */
	public JVariant() {
	}

	public static String join(Map<String, ?> props) {
		if (props == null || props.isEmpty()) return "";
		return props.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.map(e -> e.getKey() + "=" + stringify(e.getValue()))
				.collect(Collectors.joining(","));
	}

	private static String stringify(Object v) {
		if (v == null) return "null";
		if (v instanceof Enum<?> e) return e.name().toLowerCase(Locale.ROOT);
		return v.toString();
	}

	private static JVariant fromMap(Map<String, List<JBlockModel>> m) {
		JVariant v = new JVariant();
		m.forEach((k, list) -> v.models.put(k, new ArrayList<>(list)));
		return v;
	}

	public JVariant put(String key, JBlockModel model) {
		List<JBlockModel> models = this.models.getOrDefault(key, new ArrayList<>());

		models.add(model);

		this.models.put(key, models);

		return this;
	}

	/**
	 * boolean block properties
	 */
	public JVariant put(String property, boolean value, JBlockModel model) {
		return this.put(property + '=' + value, model);
	}

	/**
	 * int block properties
	 */
	public JVariant put(String property, int value, JBlockModel model) {
		return this.put(property + '=' + value, model);
	}

	/**
	 * other block properties
	 *
	 * @see Direction
	 */
	public JVariant put(String property, StringRepresentable value, JBlockModel model) {
		return this.put(property + '=' + value.getSerializedName(), model);
	}

	/**
	 * everything else
	 */
	public JVariant put(String property, String value, JBlockModel model) {
		return this.put(property + '=' + value, model);
	}

	/** Join a map of properties into one variant key and put a single model. */
	public JVariant put(Map<String, ?> props, JBlockModel model) {
		return this.put(join(props), model);
	}

	public JVariant put(String key, JBlockModel... models) {
		// allows passing multiple models at once
		List<JBlockModel> list = this.models.computeIfAbsent(key, k -> new ArrayList<>());
		Collections.addAll(list, models);
		return this;
	}

	/** Weighted by duplication: weight=N emits N copies of the same model. */
	public JVariant putWeighted(String key, JBlockModel model, int weight) {
		int w = Math.max(1, weight);
		List<JBlockModel> list = this.models.computeIfAbsent(key, k -> new ArrayList<>());
		for (int i = 0; i < w; i++) list.add(model);
		return this;
	}

	/** Convenience: composite-key + weights. */
	public JVariant putWeighted(Map<String, ?> props, JBlockModel model, int weight) {
		return putWeighted(join(props), model, weight);
	}

	/** Convenience: composite-key + multiple models (each weight=1). */
	public JVariant put(Map<String, ?> props, JBlockModel... models) {
		return put(join(props), models);
	}

	// ---------- Codec helpers ----------

	@Override
	public JVariant clone() {
		try {
			return (JVariant) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
