package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

/**
 * Represents a basic model type "minecraft:model".
 */
public class JModelBasic extends JItemModel {
	public static final String TYPE = "minecraft:model";
	public static final MapCodec<JModelBasic> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			// base fields first
			JTint.CODEC.listOf().optionalFieldOf("tints").forGetter(JModelBasic::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(JModelBasic::codecGetFallback),
			Codec.STRING.fieldOf("model").forGetter(JModelBasic::getModel)
	).apply(i, (tint, fallback, model) -> {
		JModelBasic m = new JModelBasic(model);
		JItemModel.applyBase(m, tint, fallback);
		return m;
	}));

	static {
		JItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private final String model;

	public JModelBasic(String model) {
		super(TYPE);
		this.model = model;
	}

	// factories (unchanged)
	public static JModelBasic of(String model) {
		return new JModelBasic(model);
	}

	// Getters and Setters
	public String getModel() {
		return model;
	}
}
