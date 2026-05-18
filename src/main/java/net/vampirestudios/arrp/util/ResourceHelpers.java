package net.vampirestudios.arrp.util;

import net.minecraft.resources.Identifier;

public class ResourceHelpers {
	public static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	public static String vanillaTagId(String path) {
		return "#" + vanillaId(path);
	}

	public static Identifier customId(String modId, String path) {
		return Identifier.fromNamespaceAndPath(modId, path);
	}
}
