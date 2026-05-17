package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:has_component" boolean property.
 */
public class JPropertyHasComponent extends JProperty {
	public static final String TYPE = "minecraft:has_component";
	public static final MapCodec<JPropertyHasComponent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("component").forGetter(JPropertyHasComponent::getComponent),
			Codec.BOOL.optionalFieldOf("ignore_default", false).forGetter(JPropertyHasComponent::shouldIgnoreDefault)
	).apply(i, JPropertyHasComponent::of));

	static {
		JProperty.register(TYPE, CODEC);
	}

	private final String component;
	private boolean ignoreDefault = false; // Default: false

	public JPropertyHasComponent(String component) {
		super(TYPE);
		this.component = component;
	}

	public static JPropertyHasComponent of(String component) {
		return new JPropertyHasComponent(component);
	}

	public static JPropertyHasComponent of(String component, boolean ignoreDefault) {
		return new JPropertyHasComponent(component).ignoreDefault(ignoreDefault);
	}

	// Getter and Setter
	public String getComponent() {
		return component;
	}

	public boolean shouldIgnoreDefault() {
		return ignoreDefault;
	}

	public JPropertyHasComponent ignoreDefault(boolean ignoreDefault) {
		this.ignoreDefault = ignoreDefault;
		return this;
	}

}
