package net.vampirestudios.arrp.assets.blockstates;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import net.minecraft.world.level.block.state.properties.Property;

public class When implements Cloneable {// AND-shape: { "prop": "a|b|c", ... }
	private static final Codec<Map<String, String>> AND_OBJECT =
			Codec.unboundedMap(Codec.STRING, Codec.STRING);

	// Proper Encoder: (value, ops, prefix)
	private static final Encoder<When> AND_ENCODER = new Encoder<>() {
		@Override
		public <T> DataResult<T> encode(When value, DynamicOps<T> ops, T prefix) {
			if (value.state.size() != 1) {
				return DataResult.error(() -> "AND encoder requires exactly one AND-clause, got " + value.state.size());
			}
			Map<String, String> map = new LinkedHashMap<>();
			for (Pair<String, String[]> p : value.state.getFirst()) {
				map.put(p.getFirst(), String.join("|", p.getSecond()));
			}
			return AND_OBJECT.encode(map, ops, prefix);
		}
	};

	// Proper Decoder: (ops, input)
	private static final Decoder<When> AND_DECODER = new Decoder<>() {
		@Override
		public <T> DataResult<Pair<When, T>> decode(DynamicOps<T> ops, T input) {
			return AND_OBJECT.decode(ops, input).map(result -> {
				Map<String, String> m = result.getFirst();
				When w = new When();
				List<Pair<String, String[]>> clause = new ArrayList<>();
				for (Map.Entry<String, String> e : m.entrySet()) {
					String[] vals = e.getValue().isEmpty() ? new String[]{"*"} : e.getValue().split("\\|");
					clause.add(new Pair<>(e.getKey(), vals));
				}
				w.state.add(clause);
				return Pair.of(w, result.getSecond());
			});
		}
	};


	private static final Codec<When> AND_CODEC = Codec.of(AND_ENCODER, AND_DECODER);

	// OR-shape: { "OR": [ { ... }, { ... } ] }
	private static final Codec<List<Map<String, String>>> OR_ARRAY_INNER =
			Codec.list(AND_OBJECT);
	private static final Codec<When> OR_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			OR_ARRAY_INNER.fieldOf("OR").forGetter(w -> {
				List<Map<String, String>> out = new ArrayList<>();
				for (List<Pair<String, String[]>> clause : w.state) {
					Map<String, String> m = new LinkedHashMap<>();
					for (Pair<String, String[]> p : clause) m.put(p.getFirst(), String.join("|", p.getSecond()));
					out.add(m);
				}
				return out;
			})
	).apply(inst, (list) -> {
		When w = new When();
		for (Map<String, String> m : list) {
			List<Pair<String, String[]>> clause = new ArrayList<>();
			for (Map.Entry<String, String> e : m.entrySet()) {
				String[] vals = e.getValue().isEmpty() ? new String[] { "*" } : e.getValue().split("\\|");
				clause.add(new Pair<>(e.getKey(), vals));
			}
			w.state.add(clause);
		}
		return w;
	}));

	public static final Codec<When> CODEC =
			Codec.either(AND_CODEC, OR_CODEC)
					.xmap(
							e -> e.map(a -> a, o -> o),
							w -> (w.state.size() == 1 ? com.mojang.datafixers.util.Either.left(w) : com.mojang.datafixers.util.Either.right(w))
					);
	private final List<List<Pair<String, String[]>>> state = new ArrayList<>();

	/**
	 * @see BlockState#when()
	 */
	public When() {}

	@SafeVarargs
	public final <T extends Comparable<T>> When add(Property<T> property, T... values) {
		String[] states = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			states[i] = property.getName(values[i]);
		}
		return this.add(property.getName(), states);
	}

	public When add(String condition, String... states) {
		this.state.add(List.of(new Pair<>(condition, states)));
		return this;
	}

	public When add(StateBuilder builder) {
		this.state.add(List.copyOf(builder.state));
		return this;
	}

	/** Add a single AND-clause built from a map of property -> accepted values. */
	public When add(Map<String, ?> props) {
		List<Pair<String, String[]>> clause = new ArrayList<>();
		for (Map.Entry<String, ?> e : props.entrySet()) {
			clause.add(new Pair<>(e.getKey(), toStates(e.getValue())));
		}
		this.state.add(clause);
		return this;
	}

	/** Factory: one AND-clause from props. */
	public static When of(Map<String, ?> props) {
		return new When().add(props);
	}


	// --- Helpers ---

	private static String[] toStates(Object v) {
		if (v == null) return new String[]{"null"};
		if (v instanceof String s) return new String[]{s};
		if (v instanceof Boolean b) return new String[]{b.toString()};
		if (v instanceof Enum<?> en) return new String[]{en.name().toLowerCase(java.util.Locale.ROOT)};
		if (v instanceof String[] arr) return arr.length == 0 ? new String[]{"*"} : arr;
		if (v instanceof Collection<?> col) {
			List<String> out = new ArrayList<>(col.size());
			for (Object o : col) out.add(first(toStates(o)));
			return out.isEmpty() ? new String[]{"*"} : out.toArray(String[]::new);
		}
		// numbers or other types → toString
		return new String[]{v.toString()};
	}

	private static String first(String[] a) { return a.length == 0 ? "*" : a[0]; }

	// --- NEW sugar for boolean/string conditions ---

	/** Equivalent to add(prop,"true"). */
	public When isTrue(String prop) { return this.add(prop, "true"); }

	/** Equivalent to add(prop,"false"). */
	public When isFalse(String prop) { return this.add(prop, "false"); }

	/** Alias for add(prop,states). */
	public When whenProp(String prop, String... states) { return this.add(prop, states); }

	/**
	 * @see BlockState#whenStateBuilder()
	 */
	public static class StateBuilder implements Cloneable {
		final List<Pair<String, String[]>> state = new ArrayList<>();

		@SafeVarargs
		public final <T extends Comparable<T>> StateBuilder add(Property<T> property, T... values) {
			String[] states = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				states[i] = property.getName(values[i]);
			}
			return this.add(property.getName(), states);
		}

		/** Add multiple (prop -> states) entries to this AND-clause. */
		public StateBuilder add(Map<String, ?> props) {
			for (Map.Entry<String, ?> e : props.entrySet()) {
				this.add(e.getKey(), toStates(e.getValue()));
			}
			return this;
		}

		public StateBuilder add(String condition, String... states) {
			this.state.add(new Pair<>(condition, states));
			return this;
		}

		// --- NEW sugar here too ---

		public StateBuilder isTrue(String prop) { return this.add(prop, "true"); }
		public StateBuilder isFalse(String prop) { return this.add(prop, "false"); }
		public StateBuilder whenProp(String prop, String... states) { return this.add(prop, states); }

		@Override
		protected StateBuilder clone() {
			try {
				return (StateBuilder) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new InternalError(e);
			}
		}
	}

	@Override
	public When clone() {
		try {
			return (When) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
