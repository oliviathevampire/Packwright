package net.vampirestudios.arrp.data.worldgen;

import net.minecraft.resources.Identifier;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public interface ClockTimeMarkers {
	Identifier DAY = vanillaId("day");
	Identifier NOON = vanillaId("noon");
	Identifier NIGHT = vanillaId("night");
	Identifier MIDNIGHT = vanillaId("midnight");
	Identifier WAKE_UP_FROM_SLEEP = vanillaId("wake_up_from_sleep");
	Identifier ROLL_VILLAGE_SIEGE = vanillaId("roll_village_siege");
}
