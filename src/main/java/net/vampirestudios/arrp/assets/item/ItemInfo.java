package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.ItemModel;

import java.util.Optional;

/**
 * an item model info, static import this class
 */
public class ItemInfo implements Cloneable {
	public static final Codec<ItemInfo> CODEC = RecordCodecBuilder.create(i -> i.group(
			ItemModel.CODEC.optionalFieldOf("model").forGetter(x -> Optional.ofNullable(x.model)),
			Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(ItemInfo::shouldPlayHandAnimationOnSwap),
			Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(ItemInfo::isOversizedInGui)
	).apply(i, (m, swap, gui) -> {
		ItemInfo out = new ItemInfo();
		m.ifPresent(out::model);
		out.handAnimationOnSwap = swap;
		out.oversizedInGui = gui;
		return out;
	}));

	private ItemModel model;
	private boolean handAnimationOnSwap;
	private boolean oversizedInGui;

	// Default constructor
	public ItemInfo() {}

	public ItemInfo(ItemModel model) {
		this.model = model;
	}

	// Static factory method to start building
	public static ItemInfo item() {
		return new ItemInfo();
	}

	// Fluent methods for setting properties
	public ItemInfo model(ItemModel model) {
		this.model = model;
		return this;
	}

	public ItemInfo handAnimationOnSwap(boolean handAnimationOnSwap) {
		this.handAnimationOnSwap = handAnimationOnSwap;
		return this;
	}

	public ItemInfo oversizedInGui(boolean oversizedInGui) {
		this.oversizedInGui = oversizedInGui;
		return this;
	}

	// Getter and Setter for 'model'
	public ItemModel getModel() {
		return model;
	}

	public boolean shouldPlayHandAnimationOnSwap() {
		return handAnimationOnSwap;
	}

	public boolean isOversizedInGui() {
		return oversizedInGui;
	}
}
