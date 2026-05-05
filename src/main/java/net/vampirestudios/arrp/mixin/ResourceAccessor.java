package net.vampirestudios.arrp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.InputStream;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;

@Mixin(Resource.class)
public interface ResourceAccessor {
  @Accessor
  IoSupplier<InputStream> getStreamSupplier();
}