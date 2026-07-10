package net.vampirestudios.packwright.api;

import com.mojang.serialization.Codec;
import net.minecraft.server.packs.PackType;

/**
 * Describes a kind of codec-serialized resource: which side of the pack it lives on
 * ({@code assets/} or {@code data/}), the directory inside the namespace, the file
 * extension, and the codec used to serialize it.
 * <p>
 * Well-known types are catalogued in {@link ResourceTypes}. Custom types (e.g. for
 * content added by other mods) can be created with {@link #asset(String, Codec)} and
 * {@link #data(String, Codec)} and passed to
 * {@link RuntimeResourcePack#add(ResourceType, net.minecraft.resources.Identifier, Object)}.
 *
 * @param packType  whether this resource is a client asset or server data
 * @param directory the directory inside the namespace, e.g. {@code "loot_table"} or {@code "worldgen/biome"}
 * @param extension the file extension without the leading dot, usually {@code "json"}
 * @param codec     the codec used to serialize values of this type
 */
public record ResourceType<T>(PackType packType, String directory, String extension, Codec<T> codec) {

	/**
	 * a JSON resource under {@code assets/<namespace>/<directory>/}
	 */
	public static <T> ResourceType<T> asset(String directory, Codec<T> codec) {
		return new ResourceType<>(PackType.CLIENT_RESOURCES, directory, "json", codec);
	}

	/**
	 * a JSON resource under {@code data/<namespace>/<directory>/}
	 */
	public static <T> ResourceType<T> data(String directory, Codec<T> codec) {
		return new ResourceType<>(PackType.SERVER_DATA, directory, "json", codec);
	}
}
