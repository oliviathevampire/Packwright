package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

import java.util.ArrayList;
import java.util.List;

public final class JModelComposite extends JItemModel {
	public static final String TYPE = "minecraft:composite";
	public static final MapCodec<JModelComposite> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			// base fields first
			JTint.CODEC.listOf().optionalFieldOf("tints").forGetter(JModelComposite::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(JModelComposite::codecGetFallback),
			// subtype
			LAZY_SELF.listOf().fieldOf("parts").forGetter(JModelComposite::getParts)
	).apply(i, (tints, fallback, parts) -> {
		JModelComposite m = new JModelComposite();
		m.parts = parts;
		JItemModel.applyBase(m, tints, fallback);
		return m;
	}));

	static {
		JItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private List<JItemModel> parts;

	public JModelComposite() {
		super(TYPE);
		this.parts = new ArrayList<>();
	}

	// fluent adders
	public JModelComposite model(JItemModel child) {
		this.parts.add(child);
		return this;
	}

	public JModelComposite models(java.util.Collection<? extends JItemModel> children) {
		this.parts.addAll(children);
		return this;
	}

	// getter for codec
	public List<JItemModel> getParts() {
		return parts;
	}
}
