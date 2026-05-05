package net.vampirestudios.arrp.api;

import java.util.List;
import java.util.function.Function;

import net.vampirestudios.arrp.util.IrremovableList;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.util.Util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface SidedRRPCallback {
	Function<SidedRRPCallback[], SidedRRPCallback> CALLBACK_FUNCTION = r -> (type, rs) -> {
		IrremovableList<PackResources> packs = new IrremovableList<>(rs, $ -> {
		});
		for (SidedRRPCallback callback : r) {
			callback.insert(type, packs);
		}
	};

	/**
	 * Register your resource pack at a higher priority than minecraft and mod resources, this is actually done by
	 * passing a reversed view of the resource pack list, such that List#add will actually add to the beginning of the
	 * list instead of the end.
	 */
	Event<SidedRRPCallback> BEFORE_VANILLA = EventFactory.createArrayBacked(SidedRRPCallback.class, CALLBACK_FUNCTION);


	/**
	 * Register your resource pack between minecraft and mod resources.
	 * <p>
	 * If you want to override a vanilla resource but allow resource packs and mods to override it
	 */
	Event<SidedRRPCallback> BETWEEN_VANILLA_AND_MODS = EventFactory.createArrayBacked(SidedRRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * Register your resource pack between mod resources and user resources.
	 * <p>
	 * If you want to override vanilla and mod resources but allow resource packs to override it
	 */
	Event<SidedRRPCallback> BETWEEN_MODS_AND_USER = EventFactory.createArrayBacked(SidedRRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * Register your resource pack at a lower priority than minecraft and mod resources. This is actually done by
	 * passing a view of the resource pack list, such that List#add will add to the end of the list, after default
	 * resource packs.
	 * <p>
	 * If you want to override a vanilla resource
	 */
	Event<SidedRRPCallback> AFTER_VANILLA = EventFactory.createArrayBacked(SidedRRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * @see MultiPackResourceManager#MultiPackResourceManager(PackType, List)
	 */
	void insert(PackType type, List<PackResources> resources);

	static Void INIT_ = Util.make(() -> {
		BEFORE_VANILLA.register((type, resources) -> RRPCallback.BEFORE_VANILLA.invoker().insert(resources));
		BETWEEN_VANILLA_AND_MODS.register((type, resources) -> RRPCallback.BETWEEN_VANILLA_AND_MODS.invoker().insert(resources));
		BETWEEN_MODS_AND_USER.register((type, resources) -> RRPCallback.BETWEEN_MODS_AND_USER.invoker().insert(resources));
		AFTER_VANILLA.register((type, resources) -> RRPCallback.AFTER_VANILLA.invoker().insert(resources));
		return null;
	});
}
