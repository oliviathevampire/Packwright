package net.vampirestudios.packwright.data.advancement;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;

/** conditions for {@code minecraft:location} */
public final class LocationConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("location");

	static {
		// the location predicate's fields are inlined directly into the conditions object
		// in vanilla, so reuse its codec rather than nesting it under a key
		CriterionConditions.register(TYPE.toString(),
				LocationPredicate.CODEC.xmap(LocationConditions::new, LocationConditions::getLocation));
	}

	private final LocationPredicate location;

	private LocationConditions(LocationPredicate location) {
		super(TYPE.toString());
		this.location = location;
	}

	public static LocationConditions location(LocationPredicate predicate) {
		return new LocationConditions(predicate);
	}

	public LocationPredicate getLocation() {
		return location;
	}
}
