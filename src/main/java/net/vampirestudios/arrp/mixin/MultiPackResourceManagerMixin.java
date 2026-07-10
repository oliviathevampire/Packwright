package net.vampirestudios.arrp.mixin;

import com.google.common.collect.Lists;
import net.vampirestudios.arrp.ARRP;
import net.vampirestudios.arrp.api.SidedRRPCallback;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Mixin(MultiPackResourceManager.class)
public abstract class MultiPackResourceManagerMixin {
	@Unique
	private static final Logger ARRP_LOGGER = LogManager.getLogger("ARRP/MultiPackResourceManagerMixin");

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, name = "packs")
	private static List<PackResources> registerARRPs(List<PackResources> packs, PackType type, List<PackResources> packs0) throws ExecutionException, InterruptedException {
		List<PackResources> copy = new ArrayList<>(packs);
		ARRP.waitForPregen();
		ARRP_LOGGER.info("ARRP register - before vanilla");
		SidedRRPCallback.BEFORE_VANILLA.invoker().insert(type, Lists.reverse(copy));

		OptionalInt optionalInt = IntStream.range(0, copy.size()).filter(i -> copy.get(i).location().id().equals("fabric")).findFirst();

		if (optionalInt.isPresent()) {
			ARRP_LOGGER.info("ARRP register - between vanilla and mods");
			int initialCopyLength = copy.size();
			SidedRRPCallback.BETWEEN_VANILLA_AND_MODS.invoker().insert(type, copy.subList(0, optionalInt.getAsInt()));
			ARRP_LOGGER.info("ARRP register - between mods and user");
			int finalCopyLength = copy.size();
			SidedRRPCallback.BETWEEN_MODS_AND_USER.invoker().insert(type, copy.subList(0, optionalInt.getAsInt() + 1 + (finalCopyLength - initialCopyLength)));
		}

		ARRP_LOGGER.info("ARRP register - after vanilla");
		SidedRRPCallback.AFTER_VANILLA.invoker().insert(type, copy);
		return copy;
	}
}