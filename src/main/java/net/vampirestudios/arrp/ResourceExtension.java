package net.vampirestudios.arrp;

import net.vampirestudios.arrp.api.ImmediateInputSupplier;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.mixin.ResourceAccessor;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public interface ResourceExtension {
  static Map<Identifier, Object> findExtendedResources(ResourceManager resourceManager, String dataType) {
    final Map<Identifier, Object> map = new HashMap<>();
    FileToIdConverter resourceFinder = FileToIdConverter.json(dataType);

    for (Map.Entry<Identifier, Resource> entry : resourceFinder.listMatchingResources(resourceManager).entrySet()) {
      Identifier identifier = entry.getKey();

      final Resource resource = entry.getValue();
      final IoSupplier<InputStream> provider = ((ResourceAccessor) resource).getStreamSupplier();
      if (provider instanceof ImmediateInputSupplier<?> im) {
        RuntimeResourcePackImpl.LOGGER.debug("ImmediateInputSupplier found: {}", identifier);
        map.put(resourceFinder.fileToId(identifier), im.resource());
      }
    }

    return map;
  }
}