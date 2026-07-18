package net.vampirestudios.packwright.api;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.util.JsonBytes;

@FunctionalInterface
public interface ResourceEncoder<T> {

    byte[] encode(T value);

    static <T> ResourceEncoder<T> codec(Codec<T> codec) {
        return value -> JsonBytes.encodeToPrettyBytes(codec, value);
    }
}