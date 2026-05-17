package net.vampirestudios.arrp.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Roll {
	public static final Codec<Roll> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("min").forGetter(r -> r.min),
			Codec.INT.fieldOf("max").forGetter(r -> r.max)
	).apply(i, Roll::new));

	private final int min;
	private final int max;

	/**
	 * @see LootTable#roll(int, int)
	 */
	public Roll(int min, int max) {
		this.min = min;
		this.max = max;
	}
}
