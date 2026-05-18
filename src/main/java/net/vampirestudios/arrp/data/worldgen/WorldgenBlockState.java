package net.vampirestudios.arrp.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class WorldgenBlockState {
	public static final Codec<WorldgenBlockState> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("Name").forGetter(x -> x.name),
			Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("Properties", Map.of()).forGetter(x -> x.properties)
	).apply(i, (name, properties) -> new WorldgenBlockState().name(name).properties(properties)));

	private Identifier name;
	private Map<String, String> properties = new LinkedHashMap<>();

	public static WorldgenBlockState blockState(Identifier name) {
		return new WorldgenBlockState().name(name);
	}

	public WorldgenBlockState name(Identifier name) {
		this.name = name;
		return this;
	}

	public WorldgenBlockState properties(Map<String, String> properties) {
		this.properties = new LinkedHashMap<>(properties);
		return this;
	}

	public WorldgenBlockState property(String key, String value) {
		this.properties.put(key, value);
		return this;
	}

	public WorldgenBlockState property(String key, boolean value) {
		return property(key, Boolean.toString(value));
	}

	public WorldgenBlockState property(String key, int value) {
		return property(key, Integer.toString(value));
	}
}