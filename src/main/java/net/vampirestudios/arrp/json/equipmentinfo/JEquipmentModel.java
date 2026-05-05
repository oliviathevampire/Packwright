package net.vampirestudios.arrp.json.equipmentinfo;

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
public class JEquipmentModel {

	public static final Codec<JEquipmentModel> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.unboundedMap(Codec.STRING, JLayer.CODEC.listOf())
					.fieldOf("layers")
					.forGetter(JEquipmentModel::getLayersForCodec)
	).apply(i, map -> {
		JEquipmentModel model = new JEquipmentModel();
		map.forEach(model::addLayersInternal);
		return model;
	}));

	// ─────────────────────────────────────────────
	// Internal state
	// ─────────────────────────────────────────────

	private final Map<String, List<JLayer>> layers = new LinkedHashMap<>();

	// ─────────────────────────────────────────────
	// Constructors / entry point
	// ─────────────────────────────────────────────

	public JEquipmentModel() {}

	public static JEquipmentModel model() {
		return new JEquipmentModel();
	}

	// ─────────────────────────────────────────────
	// Fluent API
	// ─────────────────────────────────────────────

	public JEquipmentModel addLayer(String layerType, JLayer... layer) {
		Objects.requireNonNull(layerType, "layerType");
		Objects.requireNonNull(layer, "layer");

		List<JLayer> list = layers.computeIfAbsent(layerType, k -> new ArrayList<>());
		Collections.addAll(list, layer);
		return this;
	}

	public JEquipmentModel addLayer(Identifier layerType, JLayer... layer) {
		return addLayer(layerType.toString(), layer);
	}

	public JEquipmentModel addLayer(LayerType type, JLayer... layer) {
		if (type == LayerType.CUSTOM) {
			throw new IllegalArgumentException("Use addLayerCustom for CUSTOM types");
		}
		return addLayer(type.asString(), layer);
	}

	public JEquipmentModel addLayerCustom(String customType, JLayer... layer) {
		return addLayer(customType, layer);
	}

	public JEquipmentModel addLayerCustom(Identifier customType, JLayer... layer) {
		return addLayer(customType.toString(), layer);
	}

	// ─────────────────────────────────────────────
	// Getters (codec + user)
	// ─────────────────────────────────────────────

	public Map<String, List<JLayer>> getLayers() {
		return layers;
	}

	// Used only by CODEC
	private Map<String, List<JLayer>> getLayersForCodec() {
		return layers;
	}

	private void addLayersInternal(String key, List<JLayer> list) {
		this.layers.put(key, new ArrayList<>(list));
	}
}
