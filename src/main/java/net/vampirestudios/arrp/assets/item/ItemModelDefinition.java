package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * an item model info, static import this class
 */
public class ItemModelDefinition {
	public static final Codec<ItemModelDefinition> CODEC = RecordCodecBuilder.create(i -> i.group(
			ItemModel.CODEC.optionalFieldOf("model").forGetter(x -> Optional.ofNullable(x.model)),
			Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(ItemModelDefinition::shouldPlayHandAnimationOnSwap),
			Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(ItemModelDefinition::isOversizedInGui)
	).apply(i, (m, swap, gui) -> {
		ItemModelDefinition out = new ItemModelDefinition();
		m.ifPresent(out::model);
		out.handAnimationOnSwap = swap;
		out.oversizedInGui = gui;
		return out;
	}));

	private ItemModel model;
	private boolean handAnimationOnSwap;
	private boolean oversizedInGui;

	// Default constructor
	public ItemModelDefinition() {}

	public ItemModelDefinition(ItemModel model) {
		this.model = model;
	}

	// Static factory method to start building
	public static ItemModelDefinition item() {
		return new ItemModelDefinition();
	}

	// Fluent methods for setting properties
	public ItemModelDefinition model(ItemModel model) {
		this.model = model;
		return this;
	}

	public ItemModelDefinition handAnimationOnSwap(boolean handAnimationOnSwap) {
		this.handAnimationOnSwap = handAnimationOnSwap;
		return this;
	}

	public ItemModelDefinition oversizedInGui(boolean oversizedInGui) {
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
