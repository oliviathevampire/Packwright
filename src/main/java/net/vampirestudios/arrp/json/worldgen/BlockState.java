package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockState {
	public static final Codec<BlockState> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("Name").forGetter(x -> x.name),
			Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("Properties", Map.of()).forGetter(x -> x.properties)
	).apply(i, (name, properties) -> new BlockState().name(name).properties(properties)));

	private String name;
	private Map<String, String> properties = new LinkedHashMap<>();

	public static BlockState blockState(String name) {
		return new BlockState().name(name);
	}

	public BlockState name(String name) {
		this.name = name;
		return this;
	}

	public BlockState properties(Map<String, String> properties) {
		this.properties = new LinkedHashMap<>(properties);
		return this;
	}

	public BlockState property(String key, String value) {
		this.properties.put(key, value);
		return this;
	}

	public BlockState property(String key, boolean value) {
		return property(key, Boolean.toString(value));
	}

	public BlockState property(String key, int value) {
		return property(key, Integer.toString(value));
	}
}