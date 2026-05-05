package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:keybind_down" boolean property.
 */
public class JPropertyKeybindDown extends JProperty {
	public static final String TYPE = "minecraft:keybind_down";
	public static final MapCodec<JPropertyKeybindDown> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("keybind").forGetter(JPropertyKeybindDown::getKeybind)
	).apply(instance, JPropertyKeybindDown::new));

	static {
		JProperty.register(TYPE, CODEC);
	}

	private final String keybind;

	public JPropertyKeybindDown(String keybind) {
		super("minecraft:keybind_down");
		this.keybind = keybind;
	}

	// Getter and Setter
	public String getKeybind() {
		return keybind;
	}
}
