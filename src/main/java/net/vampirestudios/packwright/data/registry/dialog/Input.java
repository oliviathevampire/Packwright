package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

/**
 * A dialog input control. Vanilla's {@code Input(key, control)} flattens the control's fields
 * (including its {@code type} discriminator) alongside {@code key} into a single JSON object, so
 * each variant here carries {@code key} itself rather than wrapping a separate control object.
 * Vanilla defines four types (bootstrapped by {@code InputControlTypes}): {@link BooleanInput},
 * {@link NumberRangeInput}, {@link SingleOptionInput} and {@link TextInput}.
 */
public sealed interface Input permits Input.BooleanInput, Input.NumberRangeInput, Input.SingleOptionInput, Input.TextInput {
	Codec<Input> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<Input, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "boolean" -> BooleanInput.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "number_range" -> NumberRangeInput.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "single_option" -> SingleOptionInput.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "text" -> TextInput.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported dialog input type");
			});
		}

		@Override
		public <T> DataResult<T> encode(Input input, DynamicOps<T> ops, T prefix) {
			if (input instanceof BooleanInput in) return BooleanInput.CODEC.codec().encode(in, ops, prefix);
			if (input instanceof NumberRangeInput in) return NumberRangeInput.CODEC.codec().encode(in, ops, prefix);
			if (input instanceof SingleOptionInput in) return SingleOptionInput.CODEC.codec().encode(in, ops, prefix);
			if (input instanceof TextInput in) return TextInput.CODEC.codec().encode(in, ops, prefix);
			return DataResult.error(() -> "Unsupported dialog input: " + input.getClass().getSimpleName());
		}
	};

	String key();

	static BooleanInput bool(String key, String label) { return new BooleanInput(key, label, false, "true", "false"); }
	static NumberRangeInput numberRange(String key, String label, float start, float end) {
		return new NumberRangeInput(key, 200, label, "options.generic_value", new NumberRangeInput.RangeInfo(start, end, Optional.empty(), Optional.empty()));
	}
	static SingleOptionInput singleOption(String key, String label, SingleOptionInput.Entry... options) {
		return new SingleOptionInput(key, 200, List.of(options), label, true);
	}
	static TextInput text(String key, String label) { return new TextInput(key, 200, label, true, "", 32, Optional.empty()); }

	/** a toggle, vanilla's {@code minecraft:boolean} input control */
	record BooleanInput(String key, String label, boolean initial, String onTrue, String onFalse) implements Input {
		public static final MapCodec<BooleanInput> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:boolean"),
				Codec.STRING.fieldOf("key").forGetter(BooleanInput::key),
				Codec.STRING.fieldOf("label").forGetter(BooleanInput::label),
				Codec.BOOL.fieldOf("initial").orElse(false).forGetter(BooleanInput::initial),
				Codec.STRING.fieldOf("on_true").orElse("true").forGetter(BooleanInput::onTrue),
				Codec.STRING.fieldOf("on_false").orElse("false").forGetter(BooleanInput::onFalse)
		).apply(i, (type, key, label, initial, onTrue, onFalse) -> new BooleanInput(key, label, initial, onTrue, onFalse)));

		public BooleanInput initial(boolean initial) { return new BooleanInput(key, label, initial, onTrue, onFalse); }
		public BooleanInput onTrue(String onTrue) { return new BooleanInput(key, label, initial, onTrue, onFalse); }
		public BooleanInput onFalse(String onFalse) { return new BooleanInput(key, label, initial, onTrue, onFalse); }
	}

	/** a slider, vanilla's {@code minecraft:number_range} input control */
	record NumberRangeInput(String key, int width, String label, String labelFormat, RangeInfo rangeInfo) implements Input {
		public static final MapCodec<NumberRangeInput> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:number_range"),
				Codec.STRING.fieldOf("key").forGetter(NumberRangeInput::key),
				Codec.INT.fieldOf("width").orElse(200).forGetter(NumberRangeInput::width),
				Codec.STRING.fieldOf("label").forGetter(NumberRangeInput::label),
				Codec.STRING.fieldOf("label_format").orElse("options.generic_value").forGetter(NumberRangeInput::labelFormat),
				RangeInfo.MAP_CODEC.forGetter(NumberRangeInput::rangeInfo)
		).apply(i, (type, key, width, label, labelFormat, rangeInfo) -> new NumberRangeInput(key, width, label, labelFormat, rangeInfo)));

		public NumberRangeInput width(int width) { return new NumberRangeInput(key, width, label, labelFormat, rangeInfo); }
		public NumberRangeInput labelFormat(String labelFormat) { return new NumberRangeInput(key, width, label, labelFormat, rangeInfo); }
		public NumberRangeInput initial(float initial) { return new NumberRangeInput(key, width, label, labelFormat, rangeInfo.initial(initial)); }
		public NumberRangeInput step(float step) { return new NumberRangeInput(key, width, label, labelFormat, rangeInfo.step(step)); }

		public record RangeInfo(float start, float end, Optional<Float> initial, Optional<Float> step) {
			public static final MapCodec<RangeInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
					Codec.FLOAT.fieldOf("start").forGetter(RangeInfo::start),
					Codec.FLOAT.fieldOf("end").forGetter(RangeInfo::end),
					Codec.FLOAT.optionalFieldOf("initial").forGetter(RangeInfo::initial),
					Codec.FLOAT.optionalFieldOf("step").forGetter(RangeInfo::step)
			).apply(i, RangeInfo::new));

			public RangeInfo initial(float initial) { return new RangeInfo(start, end, Optional.of(initial), step); }
			public RangeInfo step(float step) { return new RangeInfo(start, end, initial, Optional.of(step)); }
		}
	}

	/** a dropdown, vanilla's {@code minecraft:single_option} input control */
	record SingleOptionInput(String key, int width, List<Entry> options, String label, boolean labelVisible) implements Input {
		public SingleOptionInput {
			if (options == null || options.isEmpty()) throw new IllegalArgumentException("single_option input options cannot be empty");
			options = List.copyOf(options);
		}

		public static final MapCodec<SingleOptionInput> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:single_option"),
				Codec.STRING.fieldOf("key").forGetter(SingleOptionInput::key),
				Codec.INT.fieldOf("width").orElse(200).forGetter(SingleOptionInput::width),
				Entry.CODEC.listOf().fieldOf("options").forGetter(SingleOptionInput::options),
				Codec.STRING.fieldOf("label").forGetter(SingleOptionInput::label),
				Codec.BOOL.fieldOf("label_visible").orElse(true).forGetter(SingleOptionInput::labelVisible)
		).apply(i, (type, key, width, options, label, labelVisible) -> new SingleOptionInput(key, width, options, label, labelVisible)));

		public SingleOptionInput width(int width) { return new SingleOptionInput(key, width, options, label, labelVisible); }
		public SingleOptionInput labelVisible(boolean labelVisible) { return new SingleOptionInput(key, width, options, label, labelVisible); }

		public record Entry(String id, Optional<String> display, boolean initial) {
			public static final Codec<Entry> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(
					Codec.STRING.fieldOf("id").forGetter(Entry::id),
					Codec.STRING.optionalFieldOf("display").forGetter(Entry::display),
					Codec.BOOL.fieldOf("initial").orElse(false).forGetter(Entry::initial)
			).apply(i, Entry::new));
			public static final Codec<Entry> CODEC = Codec.withAlternative(FULL_CODEC, Codec.STRING, id -> new Entry(id, Optional.empty(), false));

			public static Entry of(String id) { return new Entry(id, Optional.empty(), false); }
			public Entry display(String display) { return new Entry(id, Optional.of(display), initial); }
			public Entry initial(boolean initial) { return new Entry(id, display, initial); }
		}
	}

	/** a text box, vanilla's {@code minecraft:text} input control */
	record TextInput(String key, int width, String label, boolean labelVisible, String initial, int maxLength, Optional<MultilineOptions> multiline) implements Input {
		public static final MapCodec<TextInput> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:text"),
				Codec.STRING.fieldOf("key").forGetter(TextInput::key),
				Codec.INT.fieldOf("width").orElse(200).forGetter(TextInput::width),
				Codec.STRING.fieldOf("label").forGetter(TextInput::label),
				Codec.BOOL.fieldOf("label_visible").orElse(true).forGetter(TextInput::labelVisible),
				Codec.STRING.fieldOf("initial").orElse("").forGetter(TextInput::initial),
				Codec.INT.fieldOf("max_length").orElse(32).forGetter(TextInput::maxLength),
				MultilineOptions.CODEC.optionalFieldOf("multiline").forGetter(TextInput::multiline)
		).apply(i, (type, key, width, label, labelVisible, initial, maxLength, multiline) ->
				new TextInput(key, width, label, labelVisible, initial, maxLength, multiline)));

		public TextInput width(int width) { return new TextInput(key, width, label, labelVisible, initial, maxLength, multiline); }
		public TextInput labelVisible(boolean labelVisible) { return new TextInput(key, width, label, labelVisible, initial, maxLength, multiline); }
		public TextInput initial(String initial) { return new TextInput(key, width, label, labelVisible, initial, maxLength, multiline); }
		public TextInput maxLength(int maxLength) { return new TextInput(key, width, label, labelVisible, initial, maxLength, multiline); }
		public TextInput multiline(MultilineOptions multiline) { return new TextInput(key, width, label, labelVisible, initial, maxLength, Optional.of(multiline)); }

		public record MultilineOptions(Optional<Integer> maxLines, Optional<Integer> height) {
			public static final Codec<MultilineOptions> CODEC = RecordCodecBuilder.create(i -> i.group(
					Codec.INT.optionalFieldOf("max_lines").forGetter(MultilineOptions::maxLines),
					Codec.INT.optionalFieldOf("height").forGetter(MultilineOptions::height)
			).apply(i, MultilineOptions::new));

			public static MultilineOptions of() { return new MultilineOptions(Optional.empty(), Optional.empty()); }
			public MultilineOptions maxLines(int maxLines) { return new MultilineOptions(Optional.of(maxLines), height); }
			public MultilineOptions height(int height) { return new MultilineOptions(maxLines, Optional.of(height)); }
		}
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
