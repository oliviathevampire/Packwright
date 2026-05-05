package net.vampirestudios.arrp.json.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class JRoll {
	public static final Codec<JRoll> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("min").forGetter(r -> r.min),
			Codec.INT.fieldOf("max").forGetter(r -> r.max)
	).apply(i, JRoll::new));

	private final int min;
	private final int max;

	/**
	 * @see JLootTable#roll(int, int)
	 */
	public JRoll(int min, int max) {
		this.min = min;
		this.max = max;
	}
}
