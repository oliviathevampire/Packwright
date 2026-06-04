package net.vampirestudios.arrp.assets.item.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.ItemModel;
import net.vampirestudios.arrp.assets.item.properties.Property;
import net.vampirestudios.arrp.assets.item.RangeEntry;
import net.vampirestudios.arrp.assets.item.tints.Tint;
import net.vampirestudios.arrp.assets.models.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a range dispatch model type "minecraft:range_dispatch".
 */
public class ModelRangeDispatch extends ItemModel {
	public static final String TYPE = "minecraft:range_dispatch";

	public static final MapCodec<ModelRangeDispatch> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelRangeDispatch::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelRangeDispatch::codecGetFallback),
			Transformation.CODEC.optionalFieldOf("transformation").forGetter(ModelRangeDispatch::codecGetTransformation),
			Property.MAP_CODEC.forGetter(ModelRangeDispatch::getProperty),
			Codec.DOUBLE.optionalFieldOf("scale", 1.0).forGetter(ModelRangeDispatch::getScale),
			RangeEntry.CODEC.listOf().fieldOf("entries").forGetter(ModelRangeDispatch::getEntries)
	).apply(i, (tints, fallback, transformation, property, scale, entries) -> {
		ModelRangeDispatch m = new ModelRangeDispatch();
		applyBase(m, tints, fallback, transformation);
		m.property = property;
		m.scale = scale;
		m.entries = entries;
		return m;
	}));

	static {
		ItemModel.register(TYPE, CODEC);
	}

	private Property property;
	private double scale = 1.0;
	private List<RangeEntry> entries;

	public ModelRangeDispatch() {
		super("minecraft:range_dispatch");
		this.entries = new ArrayList<>();
	}

	public ModelRangeDispatch(Property property, double scale, List<RangeEntry> entries) {
		this();
		this.property = property;
		this.scale = scale;
		this.entries = entries;
	}

	// Getters and Setters
	public Property getProperty() {
		return property;
	}

	public ModelRangeDispatch property(Property property) {
		this.property = property;
		return this;
	}

	public double getScale() {
		return scale;
	}

	public ModelRangeDispatch scale(double scale) {
		this.scale = scale;
		return this;
	}

	public List<RangeEntry> getEntries() {
		return entries;
	}

	public ModelRangeDispatch entries(List<RangeEntry> entries) {
		this.entries = entries;
		return this;
	}

	public ModelRangeDispatch entry(RangeEntry entry) {
		this.entries.add(entry);
		return this;
	}
}
