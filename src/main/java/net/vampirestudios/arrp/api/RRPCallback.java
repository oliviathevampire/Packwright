package net.vampirestudios.arrp.api;

import net.vampirestudios.arrp.util.IrremovableList;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.packs.PackResources;
import java.util.List;
import java.util.function.Function;

public interface RRPCallback {
	Function<RRPCallback[], RRPCallback> CALLBACK_FUNCTION = r -> rs -> {
		IrremovableList<PackResources> packs = new IrremovableList<>(rs, $ -> {});
		for (RRPCallback callback : r) {
			callback.insert(packs);
		}
	};

	/**
	 * Register your resource pack at a lower priority than minecraft and mod resources
	 */
	Event<RRPCallback> BEFORE_VANILLA = EventFactory.createArrayBacked(RRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * Register your resource pack between minecraft and mod resources
	 */
	Event<RRPCallback> BETWEEN_VANILLA_AND_MODS = EventFactory.createArrayBacked(RRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * Register your resource pack between mod resources and user resources. This is similar to the BEFORE_USER event,
	 * but is always enabled and not visible in the resource pack selection screen.
	 */
	Event<RRPCallback> BETWEEN_MODS_AND_USER = EventFactory.createArrayBacked(RRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * Register your resource pack at a higher priority than minecraft and mod resources, but lower priority than user resources.
	 */
	Event<RRPCallback> BEFORE_USER = EventFactory.createArrayBacked(RRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * Register your resource pack at a higher priority than minecraft and mod resources
	 */
	Event<RRPCallback> AFTER_VANILLA = EventFactory.createArrayBacked(RRPCallback.class, CALLBACK_FUNCTION);

	/**
	 * you can only add resource packs to this list, you may not remove them
	 */
	void insert(List<PackResources> resources);
}
