package net.vampirestudios.arrp.assets.models;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Textures {
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

	public Textures layer0(String val) {
		this.textures.put("layer0", val);
		return this;
	}

	public Textures layer1(String val) {
		this.textures.put("layer1", val);
		return this;
	}

	public Textures layer2(String val) {
		this.textures.put("layer2", val);
		return this;
	}

	public Textures layer3(String val) {
		this.textures.put("layer3", val);
		return this;
	}

	public Textures layer4(String val) {
		this.textures.put("layer4", val);
		return this;
	}

	@Override
	public Textures clone() {
		try {
			return (Textures) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	public static class Serializer implements JsonSerializer<Textures> {
		@Override
		public JsonElement serialize(Textures src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			src.textures.forEach(json::addProperty);
			return json;
		}
	}
}
