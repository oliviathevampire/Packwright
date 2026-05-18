package net.vampirestudios.arrp.assets.blockstates;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.vampirestudios.arrp.impl.Codecs;

import java.util.*;
import java.util.stream.Collectors;

public final class Variant {
	private static final Codec<java.util.List<SimpleModel>> MODELS = Codecs.oneOrList(SimpleModel.CODEC);
	public static final Codec<Variant> CODEC = Codec.unboundedMap(Codec.STRING, MODELS)
			.xmap(Variant::fromMap, v -> v.models);

	private final Map<String, List<SimpleModel>> models = new HashMap<>();

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

	private static Variant fromMap(Map<String, List<SimpleModel>> m) {
		Variant v = new Variant();
		m.forEach((k, list) -> v.models.put(k, new ArrayList<>(list)));
		return v;
	}

	public Variant put(String key, SimpleModel... models) {
		List<SimpleModel> list = this.models.computeIfAbsent(key, k -> new ArrayList<>());
		Collections.addAll(list, models);
		return this;
	}

	public Variant put(String key, SimpleModel model) {
		return put(key, new SimpleModel[]{model});
	}

	public Variant put(String property, boolean value, SimpleModel model) {
		return put(property + '=' + value, model);
	}

	public Variant put(String property, int value, SimpleModel model) {
		return put(property + '=' + value, model);
	}

	public Variant put(String property, StringRepresentable value, SimpleModel model) {
		return put(property + '=' + value.getSerializedName(), model);
	}

	public Variant put(String property, String value, SimpleModel model) {
		return put(property + '=' + value, model);
	}

	public Variant put(Map<String, ?> props, SimpleModel model) {
		return put(join(props), model);
	}

	public Variant put(Map<String, ?> props, SimpleModel... models) {
		return put(join(props), models);
	}

	public Variant putWeighted(Map<String, ?> props, SimpleModel model, int weight) {
		return putWeighted(join(props), model, weight);
	}

	/** Use {@link SimpleModel#weight(int)} on the model before passing it to {@link #put}. */
	public Variant putWeighted(String key, SimpleModel model, int weight) {
		model.weight(Math.max(1, weight));
		return put(key, model);
	}
}
