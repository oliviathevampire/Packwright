package net.vampirestudios.arrp.data.advancement;// AdvComponentPreds.java

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class AdvComponentPreds {
  private AdvComponentPreds() {}

  public static Identifier id(DataComponentType<?> type) {
    Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
    if (id == null) throw new IllegalStateException("Unregistered component type: " + type);
    return id;
  }

  public static <V> JsonElement encodeValue(DataComponentType<V> type, V value) {
    return type.codec().encodeStart(JsonOps.INSTANCE, value).getOrThrow();
  }
}
