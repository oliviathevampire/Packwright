package net.vampirestudios.packwright.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.packs.PackResources;
import net.vampirestudios.packwright.util.AppendOnlyList;

import java.util.List;

/**
 * Fired while the game assembles its resource pack list, on both sides. Register on the
 * event matching the priority you want your pack to have; add your packs to the list you
 * are handed in {@link #insert(List)}.
 * <p>
 * For registering on only one side, or to know which side is being built,
 * see {@link SidedPackwrightCallback}.
 */
@FunctionalInterface
public interface PackwrightCallback {
	/**
	 * your packs lose to everything: vanilla, mods and the user can all override them
	 */
	Event<PackwrightCallback> BEFORE_VANILLA = event();

	/**
	 * your packs override vanilla, but mods and the user override you
	 */
	Event<PackwrightCallback> BETWEEN_VANILLA_AND_MODS = event();

	/**
	 * your packs override vanilla and mods, but the user's resource packs override you;
	 * unlike {@link #BEFORE_USER} this slot is always active and never shows up in the
	 * pack selection screen
	 */
	Event<PackwrightCallback> BETWEEN_MODS_AND_USER = event();

	/**
	 * your packs override vanilla and mods, losing only to the user's own packs
	 */
	Event<PackwrightCallback> BEFORE_USER = event();

	/**
	 * your packs override vanilla and mod resources
	 */
	Event<PackwrightCallback> AFTER_VANILLA = event();

	/**
	 * add your packs to the list; the view is append-only, you cannot remove or
	 * reorder anyone else's packs
	 */
	void insert(List<PackResources> resources);

	private static Event<PackwrightCallback> event() {
		return EventFactory.createArrayBacked(PackwrightCallback.class, listeners -> resources -> {
			List<PackResources> view = new AppendOnlyList<>(resources);
			for (PackwrightCallback listener : listeners) {
				listener.insert(view);
			}
		});
	}
}
