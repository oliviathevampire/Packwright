package net.vampirestudios.arrp.json.iteminfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.iteminfo.model.JItemModel;

import java.util.Optional;

/**
 * an item model info, static import this class
 */
public class JItemInfo implements Cloneable {
	public static final Codec<JItemInfo> CODEC = RecordCodecBuilder.create(i -> i.group(
			JItemModel.CODEC.optionalFieldOf("model").forGetter(x -> Optional.ofNullable(x.model)),
			Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(JItemInfo::shouldPlayHandAnimationOnSwap),
			Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(JItemInfo::isOversizedInGui)
	).apply(i, (m, swap, gui) -> {
		JItemInfo out = new JItemInfo();
		m.ifPresent(out::model);
		out.handAnimationOnSwap = swap;
		out.oversizedInGui = gui;
		return out;
	}));

	private JItemModel model;
	private boolean handAnimationOnSwap;
	private boolean oversizedInGui;

	// Default constructor
	public JItemInfo() {}

	public JItemInfo(JItemModel model) {
		this.model = model;
	}

	// Static factory method to start building
	public static JItemInfo item() {
		return new JItemInfo();
	}

	// Fluent methods for setting properties
	public JItemInfo model(JItemModel model) {
		this.model = model;
		return this;
	}

	public JItemInfo handAnimationOnSwap(boolean handAnimationOnSwap) {
		this.handAnimationOnSwap = handAnimationOnSwap;
		return this;
	}

	public JItemInfo oversizedInGui(boolean oversizedInGui) {
		this.oversizedInGui = oversizedInGui;
		return this;
	}

	// Getter and Setter for 'model'
	public JItemModel getModel() {
		return model;
	}

	public boolean shouldPlayHandAnimationOnSwap() {
		return handAnimationOnSwap;
	}

	public boolean isOversizedInGui() {
		return oversizedInGui;
	}
}
