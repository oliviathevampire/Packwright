package net.vampirestudios.packwright.mixin;

import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Metadata;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier;
import net.vampirestudios.packwright.Packwright;
import net.vampirestudios.packwright.api.PackwrightCallback;
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
	private static final Logger LOGGER = LogManager.getLogger("Packwright/FileResourcePackProviderMixin");

	@Inject(method = "loadPacks", at = @At("HEAD"))
	public void register(
		Consumer<Pack> result,
		CallbackInfo ci
	) throws ExecutionException, InterruptedException {
		List<PackResources> list = new ArrayList<>();
		Packwright.waitForPregen();
		LOGGER.info("Packwright register - before user");
		PackwrightCallback.BEFORE_USER.invoker().insert(list);

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
