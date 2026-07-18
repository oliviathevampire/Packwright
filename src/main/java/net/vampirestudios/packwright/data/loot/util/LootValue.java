package net.vampirestudios.packwright.data.loot.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JavaOps;

public final class LootValue {
	private LootValue() {
	}

	public static <T> Object encode(Codec<T> codec, T value) {
		return codec.encodeStart(JavaOps.INSTANCE, value).getOrThrow();
	}
}
