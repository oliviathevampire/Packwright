package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class JWorldClock {
	public static final Codec<JWorldClock> CODEC = MapCodec.unit(JWorldClock::new).codec();

	public static JWorldClock worldClock() {
		return new JWorldClock();
	}
}
