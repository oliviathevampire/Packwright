package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

public final class JModelEmpty extends JItemModel {
	public static final String TYPE = "minecraft:empty";

	public static final MapCodec<JModelEmpty> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			// base fields first
			JTint.CODEC.listOf().optionalFieldOf("tints").forGetter(JModelEmpty::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(JModelEmpty::codecGetFallback)
	).apply(i, (tints, fallback) -> {
		JModelEmpty m = new JModelEmpty();
		JItemModel.applyBase(m, tints, fallback);
		return m;
	}));

	static {
		JItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	public JModelEmpty() {
		super(TYPE);
	}
}
