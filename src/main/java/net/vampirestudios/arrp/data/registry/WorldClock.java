package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class WorldClock {
	public static final Codec<WorldClock> CODEC = MapCodec.unit(WorldClock::new).codec();

	public static WorldClock worldClock() {
		return new WorldClock();
	}
}
