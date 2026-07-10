package net.vampirestudios.arrp.assets.timeline;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.EasingType;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.vampirestudios.arrp.data.worldgen.EnvironmentAttributeValue;

import java.util.*;
import java.util.function.Consumer;

/**
 * Builder for datapack timeline definitions.
 *
 * <p>Normal tracks (override semantics, no modifier in JSON):
 * <pre>{@code
 * timeline.addTrack(EnvironmentAttributes.SUN_ANGLE, t -> t
 *     .ease(EasingType.symmetricCubicBezier(0.362f, 0.241f))
 *     .addKeyframe(6000, 360.0f)
 *     .addKeyframe(6000, 0.0f)
 * );
 * }</pre>
 *
 * <p>Modifier tracks (explicit modifier in JSON):
 * <pre>{@code
 * timeline.addModifierTrack(EnvironmentAttributes.MONSTERS_BURN, BooleanModifier.OR, t -> t
 *     .addKeyframe(12542, false)
 *     .addKeyframe(23460, true)
 * );
 * }</pre>
 */
public class Timeline {
	public static final Codec<Timeline> CODEC = RecordCodecBuilder.create(i -> i.group(
			// clock is required by the game; overworld matches pre-World-Clock behavior
			Identifier.CODEC.fieldOf("clock").orElse(Identifier.withDefaultNamespace("overworld"))
					.forGetter(t -> t.clock),
			Codec.INT.optionalFieldOf("period_ticks").forGetter(t -> Optional.ofNullable(t.periodTicks)),
			Codec.unboundedMap(Identifier.CODEC, TimeMarkerInfo.CODEC).optionalFieldOf("time_markers")
					.forGetter(t -> t.timeMarkers.isEmpty() ? Optional.empty() : Optional.of(t.timeMarkers)),
			Codec.unboundedMap(Identifier.CODEC, KeyframeTrack.CODEC).fieldOf("tracks").forGetter(t -> t.tracks)
	).apply(i, (clock, period, markers, tracks) -> {
		Timeline timeline = new Timeline();
		timeline.clock = clock;
		period.ifPresent(p -> timeline.periodTicks = p);
		markers.ifPresent(timeline.timeMarkers::putAll);
		timeline.tracks.putAll(tracks);
		return timeline;
	}));
	private final Map<Identifier, TimeMarkerInfo> timeMarkers = new LinkedHashMap<>();
	private final Map<Identifier, KeyframeTrack> tracks = new LinkedHashMap<>();
	private Identifier clock = Identifier.withDefaultNamespace("overworld");
	private Integer periodTicks;

	public Timeline() {
	}

	@SuppressWarnings("rawtypes")
	private static AttributeModifier.OperationId resolveOperationId(AttributeModifier<?, ?> modifier) {
		Map[] libraries = {
				AttributeModifier.BOOLEAN_LIBRARY,
				AttributeModifier.FLOAT_LIBRARY,
				AttributeModifier.RGB_COLOR_LIBRARY,
				AttributeModifier.ARGB_COLOR_LIBRARY,
				AttributeModifier.INTEGER_LIBRARY
		};
		for (Map lib : libraries) {
			for (Object entry : lib.entrySet()) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) entry;
				if (e.getValue() == modifier) return (AttributeModifier.OperationId) e.getKey();
			}
		}
		throw new IllegalArgumentException("Modifier not found in any built-in library: " + modifier);
	}

	public Timeline addTimerMarker(Identifier id, int ticks) {
		if (id != null) timeMarkers.put(id, new TimeMarkerInfo(ticks, false));
		return this;
	}

	public Timeline addTimerMarker(Identifier id, int ticks, boolean showInCommands) {
		if (id != null) timeMarkers.put(id, new TimeMarkerInfo(ticks, showInCommands));
		return this;
	}

	public Timeline addTrack(Identifier attributeId, Consumer<KeyframeTrack> config) {
		if (attributeId == null || config == null) return this;
		KeyframeTrack track = new KeyframeTrack();
		config.accept(track);
		tracks.put(attributeId, track);
		return this;
	}

	public Timeline addModifierTrack(Identifier attributeId, AttributeModifier<?, ?> modifier, Consumer<KeyframeTrack> config) {
		if (attributeId == null || modifier == null || config == null) return this;
		KeyframeTrack track = new KeyframeTrack();
		track.modifier = resolveOperationId(modifier);
		config.accept(track);
		tracks.put(attributeId, track);
		return this;
	}

	public Timeline addModifierTrack(Identifier attributeId, AttributeModifier.OperationId modifier, Consumer<KeyframeTrack> config) {
		if (attributeId == null || modifier == null || config == null) return this;
		KeyframeTrack track = new KeyframeTrack();
		track.modifier = modifier;
		config.accept(track);
		tracks.put(attributeId, track);
		return this;
	}

	/** the World Clock this timeline is tied to (required by the game; defaults to {@code minecraft:overworld}) */
	public Timeline clock(Identifier clock) {
		if (clock != null) this.clock = clock;
		return this;
	}

	public Identifier getClock() {
		return clock;
	}

	public Integer getPeriodTicks() {
		return periodTicks;
	}

	public Timeline setPeriodTicks(int periodTicks) {
		this.periodTicks = periodTicks;
		return this;
	}

	public Map<Identifier, TimeMarkerInfo> getTimeMarkers() {
		return timeMarkers;
	}

	public Map<Identifier, KeyframeTrack> getTracks() {
		return tracks;
	}

	// ========================================================================
	// TimeMarkerInfo
	// ========================================================================

	/**
	 * Encodes as a plain int when {@code showInCommands} is false,
	 * or as {@code { "ticks": N, "show_in_commands": true }} when true.
	 */
	public record TimeMarkerInfo(int ticks, boolean showInCommands) {
		public static final Codec<TimeMarkerInfo> CODEC = Codec.either(
				Codec.INT.xmap(t -> new TimeMarkerInfo(t, false), TimeMarkerInfo::ticks),
				RecordCodecBuilder.<TimeMarkerInfo>create(i -> i.group(
						Codec.INT.fieldOf("ticks").forGetter(TimeMarkerInfo::ticks),
						Codec.BOOL.fieldOf("show_in_commands").forGetter(TimeMarkerInfo::showInCommands)
				).apply(i, TimeMarkerInfo::new))
		).xmap(
				e -> e.map(t -> t, t -> t),
				t -> t.showInCommands() ? Either.right(t) : Either.left(t)
		);
	}

	// ========================================================================
	// KeyframeTrack
	// ========================================================================

	/**
	 * Holds the ease and keyframes for a single timeline track.
	 * Created via {@link Timeline#addTrack} or {@link Timeline#addModifierTrack} —
	 * do not set the modifier directly.
	 */
	public static final class KeyframeTrack {
		public static final Codec<KeyframeTrack> CODEC = RecordCodecBuilder.create(i -> i.group(
				EasingType.CODEC.optionalFieldOf("ease")
						.forGetter(t -> Optional.ofNullable(t.ease)),
				AttributeModifier.OperationId.CODEC.optionalFieldOf("modifier")
						.forGetter(t -> Optional.ofNullable(t.modifier)),
				Keyframe.CODEC.listOf().fieldOf("keyframes")
						.forGetter(t -> List.copyOf(t.keyframes))
		).apply(i, (ease, modifier, keyframes) -> {
			KeyframeTrack track = new KeyframeTrack();
			ease.ifPresent(e -> track.ease = e);
			modifier.ifPresent(m -> track.modifier = m);
			track.keyframes.addAll(keyframes);
			return track;
		}));
		private final List<Keyframe> keyframes = new ArrayList<>();
		AttributeModifier.OperationId modifier; // set by Timeline, not the user
		private EasingType ease;

		KeyframeTrack() {
		}

		public KeyframeTrack ease(EasingType ease) {
			this.ease = ease;
			return this;
		}

		public KeyframeTrack addKeyframe(Keyframe frame) {
			if (frame != null) keyframes.add(frame);
			return this;
		}

		public KeyframeTrack addKeyframe(int ticks, boolean value) {
			return addKeyframe(new Keyframe(ticks, value));
		}

		public KeyframeTrack addKeyframe(int ticks, float value) {
			return addKeyframe(new Keyframe(ticks, value));
		}

		public KeyframeTrack addKeyframe(int ticks, double value) {
			return addKeyframe(new Keyframe(ticks, value));
		}

		public KeyframeTrack addKeyframe(int ticks, String value) {
			return addKeyframe(new Keyframe(ticks, value));
		}

		public KeyframeTrack addKeyframe(int ticks, Identifier value) {
			return addKeyframe(new Keyframe(ticks, value));
		}

		public EasingType getEase() {
			return ease;
		}

		public AttributeModifier.OperationId getModifier() {
			return modifier;
		}

		public List<Keyframe> getKeyframes() {
			return keyframes;
		}
	}

	public record Keyframe(int ticks, EnvironmentAttributeValue value) {
		public static final Codec<Keyframe> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("ticks").forGetter(Keyframe::ticks),
				EnvironmentAttributeValue.CODEC.fieldOf("value").forGetter(Keyframe::value)
		).apply(i, Keyframe::new));

		public Keyframe(int ticks, boolean value) {
			this(ticks, EnvironmentAttributeValue.ofBoolean(value));
		}

		public Keyframe(int ticks, double value) {
			this(ticks, EnvironmentAttributeValue.ofNumber(value));
		}

		public Keyframe(int ticks, float value) {
			this(ticks, EnvironmentAttributeValue.ofNumber(value));
		}

		public Keyframe(int ticks, String value) {
			this(ticks, EnvironmentAttributeValue.ofString(value));
		}

		public Keyframe(int ticks, Identifier value) {
			this(ticks, EnvironmentAttributeValue.ofString(value.toString()));
		}
	}
}
