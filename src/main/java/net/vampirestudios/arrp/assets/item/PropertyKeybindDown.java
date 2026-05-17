package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:keybind_down" boolean property.
 */
public class PropertyKeybindDown extends Property {
	public static final String TYPE = "minecraft:keybind_down";
	public static final MapCodec<PropertyKeybindDown> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("keybind").forGetter(PropertyKeybindDown::getKeybind)
	).apply(instance, PropertyKeybindDown::new));

	static {
		Property.register(TYPE, CODEC);
	}

	private final String keybind;

	public PropertyKeybindDown(String keybind) {
		super("minecraft:keybind_down");
		this.keybind = keybind;
	}

	// Getter and Setter
	public String getKeybind() {
		return keybind;
	}
}
