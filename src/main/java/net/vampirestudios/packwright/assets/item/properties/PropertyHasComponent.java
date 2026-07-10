package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:has_component" boolean property.
 */
public class PropertyHasComponent extends Property {
	public static final String TYPE = "minecraft:has_component";
	public static final MapCodec<PropertyHasComponent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("component").forGetter(PropertyHasComponent::getComponent),
			Codec.BOOL.optionalFieldOf("ignore_default", false).forGetter(PropertyHasComponent::shouldIgnoreDefault)
	).apply(i, PropertyHasComponent::of));

	static {
		Property.register(TYPE, CODEC);
	}

	private final String component;
	private boolean ignoreDefault = false; // Default: false

	public PropertyHasComponent(String component) {
		super(TYPE);
		this.component = component;
	}

	public static PropertyHasComponent of(String component) {
		return new PropertyHasComponent(component);
	}

	public static PropertyHasComponent of(String component, boolean ignoreDefault) {
		return new PropertyHasComponent(component).ignoreDefault(ignoreDefault);
	}

	// Getter and Setter
	public String getComponent() {
		return component;
	}

	public boolean shouldIgnoreDefault() {
		return ignoreDefault;
	}

	public PropertyHasComponent ignoreDefault(boolean ignoreDefault) {
		this.ignoreDefault = ignoreDefault;
		return this;
	}

}
