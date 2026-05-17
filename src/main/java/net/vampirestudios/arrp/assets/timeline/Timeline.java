package net.vampirestudios.arrp.assets.timeline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for datapack timeline definitions, matching the Timeline format:
 * <p>
 * {
 *   "period_ticks": 24000,
 *   "tracks": {
 *     "minecraft:visual/sky_color": {
 *       "ease": "linear",
 *       "modifier": "override",
 *       "keyframes": [
 *         { "ticks": 0, "value": "#ff0000" },
 *         { "ticks": 6000, "value": "#ff00ff" }
 *       ]
 *     }
 *   }
 * }
 */
public class Timeline implements Cloneable {
	public static final Codec<Timeline> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(Timeline timeline, DynamicOps<T> ops, T prefix) {
			JsonObject json = timeline.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<Timeline, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Timeline definition must be an object");
			}

			try {
				return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
			} catch (IllegalArgumentException e) {
				return DataResult.error(e::getMessage);
			}
		}
	};

	/** Optional period in ticks over which the timeline repeats. If null, it does not repeat. */
	private Integer periodTicks;

	/**
	 * Tracks, keyed by Environment Attribute ID, e.g. "minecraft:visual/sky_color".
	 * In JSON this becomes:
	 *
	 * "tracks": {
	 *   "minecraft:visual/sky_color": { ...track json... }
	 * }
	 */
	private final Map<String, Track> tracks = new LinkedHashMap<>();

	public Timeline() {
	}

	public static Timeline fromJson(JsonObject json) {
		return new Timeline().json(json);
	}

	public Timeline json(JsonObject json) {
		this.tracks.clear();
		this.periodTicks = null;

		if (json == null) {
			return this;
		}

		if (json.has("period_ticks") && json.get("period_ticks").isJsonPrimitive()
				&& json.get("period_ticks").getAsJsonPrimitive().isNumber()) {
			this.periodTicks = json.get("period_ticks").getAsInt();
		}

		if (json.has("tracks") && json.get("tracks").isJsonObject()) {
			JsonObject tracksObj = json.getAsJsonObject("tracks");
			for (String key : tracksObj.keySet()) {
				JsonElement value = tracksObj.get(key);
				if (value != null && value.isJsonObject()) {
					this.tracks.put(key, Track.fromJson(value.getAsJsonObject()));
				}
			}
		}

		return this;
	}

	@Override
	public Timeline clone() {
		Timeline clone = new Timeline();
		clone.periodTicks = this.periodTicks;

		for (Map.Entry<String, Track> entry : this.tracks.entrySet()) {
			String key = entry.getKey();
			Track value = entry.getValue();
			if (key != null && value != null) {
				clone.tracks.put(key, value.clone());
			}
		}

		return clone;
	}

	// --------- Builder API ---------

	/** Set period_ticks (null to omit). */
	public Timeline period(Integer periodTicks) {
		this.periodTicks = periodTicks;
		return this;
	}

	/** Convenience: set period_ticks as primitive int. */
	public Timeline period(int periodTicks) {
		this.periodTicks = periodTicks;
		return this;
	}

	/** Replace tracks with a deep-cloned map. */
	public Timeline tracks(Map<String, Track> tracks) {
		this.tracks.clear();
		if (tracks != null) {
			for (Map.Entry<String, Track> entry : tracks.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					this.tracks.put(entry.getKey(), entry.getValue().clone());
				}
			}
		}
		return this;
	}

	/** Add or replace a single track for a specific Environment Attribute ID. */
	public Timeline track(String attributeId, Track track) {
		if (attributeId != null && track != null) {
			this.tracks.put(attributeId, track.clone());
		}
		return this;
	}

	/** Get (mutable) track for an attribute, creating an empty one if missing. */
	public Track getOrCreateTrack(String attributeId) {
		return this.tracks.computeIfAbsent(attributeId, k -> new Track());
	}

	public Integer getPeriodTicks() {
		return periodTicks;
	}

	public Map<String, Track> getTracks() {
		return tracks;
	}

	// --------- JSON ---------

	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();

		if (this.periodTicks != null) {
			jsonObject.addProperty("period_ticks", this.periodTicks);
		}

		if (!this.tracks.isEmpty()) {
			JsonObject tracksObj = new JsonObject();
			for (Map.Entry<String, Track> entry : this.tracks.entrySet()) {
				String key = entry.getKey();
				Track value = entry.getValue();
				if (key != null && value != null) {
					tracksObj.add(key, value.toJson());
				}
			}
			jsonObject.add("tracks", tracksObj);
		}

		return jsonObject;
	}

	// ========================================================================
	// Track
	// ========================================================================

	/**
	 * Attribute Track:
	 *
	 * {
	 *   "ease": "linear" | { "cubic_bezier": [ x1, y1, x2, y2 ] },
	 *   "modifier": "override",
	 *   "keyframes": [
	 *     { "ticks": 0, "value": ... },
	 *     { "ticks": 6000, "value": ... }
	 *   ]
	 * }
	 */
	public static final class Track implements Cloneable {
		public static final Codec<Track> CODEC = new Codec<>() {
			@Override
			public <T> DataResult<T> encode(Track track, DynamicOps<T> ops, T prefix) {
				JsonObject json = track.toJson();
				return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
			}

			@Override
			public <T> DataResult<Pair<Track, T>> decode(DynamicOps<T> ops, T input) {
				JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
				if (!el.isJsonObject()) {
					return DataResult.error(() -> "Timeline track must be an object");
				}

				try {
					return DataResult.success(Pair.of(Track.fromJson(el.getAsJsonObject()), input));
				} catch (IllegalArgumentException e) {
					return DataResult.error(e::getMessage);
				}
			}
		};

		/**
		 * "ease" can be:
		 *  - a string (e.g. "linear", "constant", "in_out_sine", ...)
		 *  - an object (e.g. { "cubic_bezier": [ x1, y1, x2, y2 ] })
		 *
		 * We store it as raw JSON for full flexibility.
		 */
		private JsonElement ease;

		/** Optional modifier ID, e.g. "override", "maximum", etc. */
		private String modifier;

		/** Keyframes list, ordered by ticks. */
		private final List<Keyframe> keyframes = new ArrayList<>();

		public Track() {
		}

		public static Track fromJson(JsonObject json) {
			Track track = new Track();

			if (json.has("ease")) {
				JsonElement easeEl = json.get("ease");
				if (easeEl != null && !easeEl.isJsonNull()) {
					track.ease = easeEl.deepCopy();
				}
			}

			if (json.has("modifier") && json.get("modifier").isJsonPrimitive()) {
				track.modifier = json.get("modifier").getAsString();
			}

			if (!json.has("keyframes") || !json.get("keyframes").isJsonArray()) {
				throw new IllegalArgumentException("Timeline track missing 'keyframes' array");
			}

			JsonArray arr = json.getAsJsonArray("keyframes");
			for (JsonElement element : arr) {
				if (element != null && element.isJsonObject()) {
					track.keyframes.add(Keyframe.fromJson(element.getAsJsonObject()));
				}
			}

			return track;
		}

		@Override
		public Track clone() {
			Track clone = new Track();
			clone.ease = this.ease == null ? null : this.ease.deepCopy();
			clone.modifier = this.modifier;

			for (Keyframe kf : this.keyframes) {
				if (kf != null) {
					clone.keyframes.add(kf.clone());
				}
			}

			return clone;
		}

		// ----- Builder-style API -----

		public Track ease(JsonElement ease) {
			this.ease = ease == null ? null : ease.deepCopy();
			return this;
		}

		public Track ease(String easeName) {
			if (easeName == null) {
				this.ease = null;
			} else {
				// Store as simple JSON string
				this.ease = com.google.gson.JsonParser.parseString("\"" + easeName + "\"");
			}
			return this;
		}

		public Track modifier(String modifier) {
			this.modifier = modifier;
			return this;
		}

		public Track keyframes(List<Keyframe> frames) {
			this.keyframes.clear();
			if (frames != null) {
				for (Keyframe frame : frames) {
					if (frame != null) {
						this.keyframes.add(frame.clone());
					}
				}
			}
			return this;
		}

		public Track addKeyframe(Keyframe frame) {
			if (frame != null) {
				this.keyframes.add(frame.clone());
			}
			return this;
		}

		// inside Track (or an utils class)
		public static Track boolToggle(String modifier, int onTick, int offTick, boolean startValue, boolean endValue) {
			Track t = new Track().modifier(modifier);
			t.addKeyframe(new Keyframe(onTick,  new JsonPrimitive(startValue)));
			t.addKeyframe(new Keyframe(offTick, new JsonPrimitive(endValue)));
			return t;
		}

		// numeric curve with constant ease
		public static Track numericCurve(String modifier, float... tickValuePairs) {
			Track t = new Track().modifier(modifier).ease("constant");
			if (tickValuePairs.length % 2 != 0) throw new IllegalArgumentException("pairs needed");
			for (int i = 0; i < tickValuePairs.length; i += 2) {
				int tick = (int) tickValuePairs[i];
				float value = tickValuePairs[i + 1];
				t.addKeyframe(new Keyframe(tick, new JsonPrimitive(value)));
			}
			return t;
		}

		public JsonElement getEase() {
			return ease;
		}

		public String getModifier() {
			return modifier;
		}

		public List<Keyframe> getKeyframes() {
			return keyframes;
		}

		public JsonObject toJson() {
			JsonObject obj = new JsonObject();

			if (this.ease != null) {
				obj.add("ease", this.ease.deepCopy());
			}

			if (this.modifier != null) {
				obj.addProperty("modifier", this.modifier);
			}

			JsonArray keyframesArr = new JsonArray();
			for (Keyframe kf : this.keyframes) {
				keyframesArr.add(kf.toJson());
			}
			obj.add("keyframes", keyframesArr);

			return obj;
		}
	}

	// ========================================================================
	// Keyframe
	// ========================================================================

	/**
	 * Keyframe object:
	 *
	 * {
	 *   "ticks": 0,
	 *   "value": <modifier argument / attribute value>
	 * }
	 */
	public static final class Keyframe implements Cloneable {
		public static final Codec<Keyframe> CODEC = new Codec<>() {
			@Override
			public <T> DataResult<T> encode(Keyframe keyframe, DynamicOps<T> ops, T prefix) {
				JsonObject json = keyframe.toJson();
				return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
			}

			@Override
			public <T> DataResult<Pair<Keyframe, T>> decode(DynamicOps<T> ops, T input) {
				JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
				if (!el.isJsonObject()) {
					return DataResult.error(() -> "Timeline keyframe must be an object");
				}

				try {
					return DataResult.success(Pair.of(Keyframe.fromJson(el.getAsJsonObject()), input));
				} catch (IllegalArgumentException e) {
					return DataResult.error(e::getMessage);
				}
			}
		};

		private final int ticks;
		private final JsonElement value;

		public Keyframe(int ticks, JsonElement value) {
			if (value == null || value.isJsonNull()) {
				throw new IllegalArgumentException("Timeline keyframe missing 'value'");
			}
			this.ticks = ticks;
			this.value = value.deepCopy();
		}

		public static Keyframe fromJson(JsonObject json) {
			if (!json.has("ticks") || !json.get("ticks").isJsonPrimitive()
					|| !json.get("ticks").getAsJsonPrimitive().isNumber()) {
				throw new IllegalArgumentException("Timeline keyframe missing numeric 'ticks'");
			}

			if (!json.has("value")) {
				throw new IllegalArgumentException("Timeline keyframe missing 'value'");
			}

			int ticks = json.get("ticks").getAsInt();
			JsonElement value = json.get("value");
			return new Keyframe(ticks, value);
		}

		public int getTicks() {
			return ticks;
		}

		public JsonElement getValue() {
			return value;
		}

		public JsonObject toJson() {
			JsonObject obj = new JsonObject();
			obj.addProperty("ticks", this.ticks);
			obj.add("value", this.value.deepCopy());
			return obj;
		}

		@Override
		public Keyframe clone() {
			try {
				Keyframe clone = (Keyframe) super.clone();
				return new Keyframe(this.ticks, this.value.deepCopy());
			} catch (CloneNotSupportedException e) {
				throw new InternalError(e);
			}
		}
	}
}