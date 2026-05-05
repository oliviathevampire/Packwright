package net.vampirestudios.arrp.mixin;

import net.vampirestudios.arrp.ARRP;
import net.vampirestudios.arrp.api.RRPCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Metadata;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Mixin(FolderRepositorySource.class)
public class FileResourcePackProviderMixin {
	@Shadow @Final private PackType packType;
	private static final Logger ARRP_LOGGER = LogManager.getLogger("ARRP/FileResourcePackProviderMixin");

	private static UnaryOperator<Component> getSourceTextSupplier() {
		Component text = Component.translatable("pack.source.runtime");
		return name -> Component.translatable("pack.nameAndSource", name, text).withStyle(ChatFormatting.GRAY);
	}

	@Inject(method = "loadPacks", at = @At("HEAD"))
	public void register(
		Consumer<Pack> adder,
		CallbackInfo ci
	) throws ExecutionException, InterruptedException {
		List<PackResources> list = new ArrayList<>();
		ARRP.waitForPregen();
		ARRP_LOGGER.info("ARRP register - before user");
		RRPCallback.BEFORE_USER.invoker().insert(list);

		for (PackResources pack : list) {
			adder.accept(Pack.readMetaAndCreate(
					pack.location(),
					new ResourcesSupplier() {
						@Override
						public PackResources openFull(PackLocationInfo var1, Metadata var2) {
							return pack;
						}

						@Override
						public PackResources openPrimary(PackLocationInfo var1) {
							return pack;
						}
					},
					this.packType,
					new PackSelectionConfig(true, Position.TOP, false)
			));
		}
	}
}
