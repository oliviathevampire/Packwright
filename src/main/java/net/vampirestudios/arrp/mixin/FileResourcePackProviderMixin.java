package net.vampirestudios.arrp.mixin;

import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Metadata;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier;
import net.vampirestudios.arrp.ARRP;
import net.vampirestudios.arrp.api.RRPCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(FolderRepositorySource.class)
public class FileResourcePackProviderMixin {
	@Shadow @Final
	private PackType packType;

	@Unique
	private static final Logger ARRP_LOGGER = LogManager.getLogger("ARRP/FileResourcePackProviderMixin");

	@Inject(method = "loadPacks", at = @At("HEAD"))
	public void register(
		Consumer<Pack> result,
		CallbackInfo ci
	) throws ExecutionException, InterruptedException {
		List<PackResources> list = new ArrayList<>();
		ARRP.waitForPregen();
		ARRP_LOGGER.info("ARRP register - before user");
		RRPCallback.BEFORE_USER.invoker().insert(list);

		for (PackResources pack : list) {
			result.accept(Pack.readMetaAndCreate(
					pack.location(),
					new ResourcesSupplier() {
						@Override
						public PackMetadataResources openMetadata(PackLocationInfo location) {
							return pack;
						}

						@Override
						public Stream<PackResources> openResources(PackLocationInfo location, Metadata metadata) {
							return Stream.of(pack);
						}
					},
					this.packType,
					new PackSelectionConfig(true, Position.TOP, false)
			));
		}
	}
}
