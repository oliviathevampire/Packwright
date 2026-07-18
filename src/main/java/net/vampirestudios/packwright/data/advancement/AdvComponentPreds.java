package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.JavaOps;
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

  /** the component's value encoded as a plain Java value (String/Number/Boolean/Map/List) */
  public static <V> Object encodeValue(DataComponentType<V> type, V value) {
    return type.codec().encodeStart(JavaOps.INSTANCE, value).getOrThrow();
  }
}
