package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:bundle_selected_item" special model type.
 */
public class JModelBundleSelectedItem extends JItemModel {
	public static final String TYPE = "bundle_selected_item";

	// no extra args, so it's just a unit codec
	public static final MapCodec<JModelBundleSelectedItem> CODEC = MapCodec.unit(() -> {
		JModelBundleSelectedItem m = new JModelBundleSelectedItem();
		m.type = TYPE;
		return m;
	});

	static {
		JItemModel.register(TYPE, CODEC);
	}

	protected JModelBundleSelectedItem() {
		super("minecraft:bundle/selected_item");
	}

	// Static factory method
	public static JModelBundleSelectedItem bundleSelectedItem() {
		return new JModelBundleSelectedItem();
	}
}
