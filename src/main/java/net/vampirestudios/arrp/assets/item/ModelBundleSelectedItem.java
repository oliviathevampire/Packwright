package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:bundle_selected_item" special model type.
 */
public class ModelBundleSelectedItem extends ItemModel {
	public static final String TYPE = "bundle_selected_item";

	// no extra args, so it's just a unit codec
	public static final MapCodec<ModelBundleSelectedItem> CODEC = MapCodec.unit(() -> {
		ModelBundleSelectedItem m = new ModelBundleSelectedItem();
		m.type = TYPE;
		return m;
	});

	static {
		ItemModel.register(TYPE, CODEC);
	}

	protected ModelBundleSelectedItem() {
		super("minecraft:bundle/selected_item");
	}

	// Static factory method
	public static ModelBundleSelectedItem bundleSelectedItem() {
		return new ModelBundleSelectedItem();
	}
}
