package net.vampirestudios.packwright.assets.models;

import com.mojang.serialization.Codec;

import java.util.HashMap;
import java.util.Map;

public class Textures {
	public static final Codec<Textures> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING)
			.xmap(map -> {
				Textures t = new Textures();
				t.textures.putAll(map);
				return t;
			}, t -> Map.copyOf(t.textures));

	private final Map<String, String> textures = new HashMap<>();

	/**
	 * @see Model#textures()
	 */
	public Textures() {}

	public Textures var(String name, String val) {
		this.textures.put(name, val);
		return this;
	}

	public Textures particle(String val) {
		this.textures.put("particle", val);
		return this;
	}

	public Textures layer0(String val) { this.textures.put("layer0", val); return this; }
	public Textures layer1(String val) { this.textures.put("layer1", val); return this; }
	public Textures layer2(String val) { this.textures.put("layer2", val); return this; }
	public Textures layer3(String val) { this.textures.put("layer3", val); return this; }
	public Textures layer4(String val) { this.textures.put("layer4", val); return this; }
}
