package net.vampirestudios.packwright.assets.item.models;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.assets.item.ItemModel;

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
