package net.vampirestudios.packwright.assets.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.*;

/**
 * Vanilla-style equipment model JSON:
 *
 * {
 *   "layers": {
 *     "humanoid": [ ... ],
 *     "leggings": [ ... ]
 *   }
 * }
 */
public class EquipmentModel {

	public static final Codec<EquipmentModel> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.unboundedMap(Codec.STRING, Layer.CODEC.listOf())
					.fieldOf("layers")
					.forGetter(EquipmentModel::getLayersForCodec)
	).apply(i, map -> {
		EquipmentModel model = new EquipmentModel();
		map.forEach(model::addLayersInternal);
		return model;
	}));

	// ─────────────────────────────────────────────
	// Internal state
	// ─────────────────────────────────────────────

	private final Map<String, List<Layer>> layers = new LinkedHashMap<>();

	// ─────────────────────────────────────────────
	// Constructors / entry point
	// ─────────────────────────────────────────────

	public EquipmentModel() {}

	public static EquipmentModel model() {
		return new EquipmentModel();
	}

	// ─────────────────────────────────────────────
	// Fluent API
	// ─────────────────────────────────────────────

	public EquipmentModel addLayer(String layerType, Layer... layer) {
		Objects.requireNonNull(layerType, "layerType");
		Objects.requireNonNull(layer, "layer");

		List<Layer> list = layers.computeIfAbsent(layerType, k -> new ArrayList<>());
		Collections.addAll(list, layer);
		return this;
	}

	public EquipmentModel addLayer(Identifier layerType, Layer... layer) {
		return addLayer(layerType.toString(), layer);
	}

	public EquipmentModel addLayer(LayerType type, Layer... layer) {
		if (type == LayerType.CUSTOM) {
			throw new IllegalArgumentException("Use addLayerCustom for CUSTOM types");
		}
		return addLayer(type.asString(), layer);
	}

	public EquipmentModel addLayerCustom(String customType, Layer... layer) {
		return addLayer(customType, layer);
	}

	public EquipmentModel addLayerCustom(Identifier customType, Layer... layer) {
		return addLayer(customType.toString(), layer);
	}

	// ─────────────────────────────────────────────
	// Getters (codec + user)
	// ─────────────────────────────────────────────

	public Map<String, List<Layer>> getLayers() {
		return layers;
	}

	// Used only by CODEC
	private Map<String, List<Layer>> getLayersForCodec() {
		return layers;
	}

	private void addLayersInternal(String key, List<Layer> list) {
		this.layers.put(key, new ArrayList<>(list));
	}
}
