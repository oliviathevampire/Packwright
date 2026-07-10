package net.vampirestudios.packwright.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Util;
import net.vampirestudios.packwright.util.AppendOnlyList;

import java.util.List;

/**
 * The side-aware twin of {@link PackwrightCallback}: fired once per {@link PackType}
 * while that side's resource manager is built, so you can register client-only or
 * data-only packs, or different packs per side.
 * <p>
 * Every plain {@link PackwrightCallback} event is bridged onto these, so registering
 * there is equivalent to registering here and ignoring the type.
 */
@FunctionalInterface
public interface SidedPackwrightCallback {
	/**
	 * your packs lose to everything: vanilla, mods and the user can all override them
	 */
	Event<SidedPackwrightCallback> BEFORE_VANILLA = event();

	/**
	 * your packs override vanilla, but mods and the user override you — the slot for
	 * overriding a vanilla resource while staying overridable yourself
	 */
	Event<SidedPackwrightCallback> BETWEEN_VANILLA_AND_MODS = event();

	/**
	 * your packs override vanilla and mods, but the user's own packs override you
	 */
	Event<SidedPackwrightCallback> BETWEEN_MODS_AND_USER = event();

	/**
	 * your packs override vanilla and mod resources
	 */
	Event<SidedPackwrightCallback> AFTER_VANILLA = event();

	/**
	 * add your packs for the given side; the view is append-only, you cannot remove
	 * or reorder anyone else's packs
	 */
	void insert(PackType type, List<PackResources> resources);

	private static Event<SidedPackwrightCallback> event() {
		return EventFactory.createArrayBacked(SidedPackwrightCallback.class, listeners -> (type, resources) -> {
			List<PackResources> view = new AppendOnlyList<>(resources);
			for (SidedPackwrightCallback listener : listeners) {
				listener.insert(type, view);
			}
		});
	}

	// interfaces cannot have static initializer blocks, so the bridge from the
	// side-agnostic events is hung off a dummy constant instead
	Void BRIDGE = Util.make(() -> {
		BEFORE_VANILLA.register((type, resources) -> PackwrightCallback.BEFORE_VANILLA.invoker().insert(resources));
		BETWEEN_VANILLA_AND_MODS.register((type, resources) -> PackwrightCallback.BETWEEN_VANILLA_AND_MODS.invoker().insert(resources));
		BETWEEN_MODS_AND_USER.register((type, resources) -> PackwrightCallback.BETWEEN_MODS_AND_USER.invoker().insert(resources));
		AFTER_VANILLA.register((type, resources) -> PackwrightCallback.AFTER_VANILLA.invoker().insert(resources));
		return null;
	});
}
