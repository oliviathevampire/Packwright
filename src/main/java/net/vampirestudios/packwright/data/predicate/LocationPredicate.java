package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

/**
 * A location predicate, as used by {@code minecraft:location_check}, entity location checks,
 * and the {@code minecraft:location} advancement criterion.
 */
public class LocationPredicate extends PredicateBuilder<LocationPredicate> {
	public static final Codec<LocationPredicate> CODEC = codecOf(LocationPredicate::new, null, "Location predicate");

	public static LocationPredicate of() {
		return new LocationPredicate();
	}

	/**
	 * matches any of the given biome ids or {@code #tag}s
	 */
	public LocationPredicate biomes(String... biomesOrTag) {
		return parameter("biomes", List.of(biomesOrTag));
	}

	/**
	 * matches any of the given structure ids or {@code #tag}s
	 */
	public LocationPredicate structures(String... structuresOrTag) {
		return parameter("structures", List.of(structuresOrTag));
	}

	public LocationPredicate dimension(String dimension) {
		return parameter("dimension", dimension);
	}

	public LocationPredicate dimension(Identifier dimension) {
		return parameter("dimension", dimension);
	}

	public LocationPredicate x(Range range) {
		subMap("position").put("x", range.value());
		return this;
	}

	public LocationPredicate y(Range range) {
		subMap("position").put("y", range.value());
		return this;
	}

	public LocationPredicate z(Range range) {
		subMap("position").put("z", range.value());
		return this;
	}

	public LocationPredicate block(BlockPredicate block) {
		return parameter("block", block);
	}

	public LocationPredicate fluid(Map<String, ?> fluid) {
		return parameter("fluid", fluid);
	}

	public LocationPredicate light(Range range) {
		subMap("light").put("light", range.value());
		return this;
	}

	public LocationPredicate smokey(boolean smokey) {
		return parameter("smokey", smokey);
	}

	public LocationPredicate canSeeSky(boolean canSeeSky) {
		return parameter("can_see_sky", canSeeSky);
	}
}
