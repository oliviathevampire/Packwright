package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * Represents the "minecraft:local_time" property.
 */
public class PropertyLocalTime extends Property {
	public static final MapCodec<PropertyLocalTime> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.optionalFieldOf("locale", "").forGetter(PropertyLocalTime::getLocale),
			Codec.STRING.optionalFieldOf("time_zone").forGetter(p -> Optional.ofNullable(p.getTimeZone())),
			Codec.STRING.optionalFieldOf("pattern").forGetter(p -> Optional.ofNullable(p.getPattern()))
	).apply(i, (loc, tz, pat) -> {
		var p = new PropertyLocalTime();
		p.locale(loc);
		tz.ifPresent(p::timeZone);
		pat.ifPresent(p::pattern);
		return p;
	}));

	static {
		Property.register("minecraft:local_time", CODEC);
	}

	private String locale = "";
	private String timeZone;
	private String pattern;

	public PropertyLocalTime() {
		super("minecraft:local_time");
	}

	// Getters and Setters
	public String getLocale() {
		return locale;
	}

	public void locale(String locale) {
		this.locale = locale;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void timeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPattern() {
		return pattern;
	}

	public void pattern(String pattern) {
		this.pattern = pattern;
	}
}
