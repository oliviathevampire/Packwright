package net.vampirestudios.arrp.assets.blockstates;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.impl.Codecs;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import java.util.*;
import java.util.stream.Collectors;

public final class Variant implements Cloneable {
	private static final Codec<java.util.List<BlockModel>> MODELS = Codecs.oneOrList(BlockModel.CODEC);
	public static final Codec<Variant> CODEC = Codec.unboundedMap(Codec.STRING, MODELS)
			.xmap(Variant::fromMap, v -> v.models);

	private final Map<String, List<BlockModel>> models = new HashMap<>();

	public static Variant variant() {
		return new Variant();
	}

	/**
	 * @see BlockState#variant()
	 */
	public Variant() {
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

	private static Variant fromMap(Map<String, List<BlockModel>> m) {
		Variant v = new Variant();
		m.forEach((k, list) -> v.models.put(k, new ArrayList<>(list)));
		return v;
	}

	public Variant put(String key, BlockModel model) {
		List<BlockModel> models = this.models.getOrDefault(key, new ArrayList<>());

		models.add(model);

		this.models.put(key, models);

		return this;
	}

	/**
	 * boolean block properties
	 */
	public Variant put(String property, boolean value, BlockModel model) {
		return this.put(property + '=' + value, model);
	}

	/**
	 * int block properties
	 */
	public Variant put(String property, int value, BlockModel model) {
		return this.put(property + '=' + value, model);
	}

	/**
	 * other block properties
	 *
	 * @see Direction
	 */
	public Variant put(String property, StringRepresentable value, BlockModel model) {
		return this.put(property + '=' + value.getSerializedName(), model);
	}

	/**
	 * everything else
	 */
	public Variant put(String property, String value, BlockModel model) {
		return this.put(property + '=' + value, model);
	}

	/** Join a map of properties into one variant key and put a single model. */
	public Variant put(Map<String, ?> props, BlockModel model) {
		return this.put(join(props), model);
	}

	public Variant put(String key, BlockModel... models) {
		// allows passing multiple models at once
		List<BlockModel> list = this.models.computeIfAbsent(key, k -> new ArrayList<>());
		Collections.addAll(list, models);
		return this;
	}

	/** Weighted by duplication: weight=N emits N copies of the same model. */
	public Variant putWeighted(String key, BlockModel model, int weight) {
		int w = Math.max(1, weight);
		List<BlockModel> list = this.models.computeIfAbsent(key, k -> new ArrayList<>());
		for (int i = 0; i < w; i++) list.add(model);
		return this;
	}

	/** Convenience: composite-key + weights. */
	public Variant putWeighted(Map<String, ?> props, BlockModel model, int weight) {
		return putWeighted(join(props), model, weight);
	}

	/** Convenience: composite-key + multiple models (each weight=1). */
	public Variant put(Map<String, ?> props, BlockModel... models) {
		return put(join(props), models);
	}

	// ---------- Codec helpers ----------

	@Override
	public Variant clone() {
		try {
			return (Variant) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
