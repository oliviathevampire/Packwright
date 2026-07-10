package net.vampirestudios.packwright.assets.item.models;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.properties.Property;
import net.vampirestudios.packwright.assets.item.SelectCase;
import net.vampirestudios.packwright.assets.item.tints.Tint;
import net.vampirestudios.packwright.assets.models.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a select model type "minecraft:select".
 */
public class ModelSelect extends ItemModel {
	public static final String TYPE = "minecraft:select";
	public static final MapCodec<ModelSelect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelSelect::codecGetTints),
			LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelSelect::codecGetFallback),
			Transformation.CODEC.optionalFieldOf("transformation").forGetter(ModelSelect::codecGetTransformation),
			Property.MAP_CODEC.forGetter(ModelSelect::getProperty),
			SelectCase.CODEC.listOf().fieldOf("cases").forGetter(ModelSelect::getCases)
	).apply(i, (tints, fallback, transformation, property, cases) -> {
		ModelSelect m = new ModelSelect();
		applyBase(m, tints, fallback, transformation);
		m.property = property;
		m.cases = cases;
		return m;
	}));

	static {
		ItemModel.register(TYPE, CODEC);
	}

	private Property property;
	private List<SelectCase> cases;

	public ModelSelect() {
		super(TYPE);
		this.cases = new ArrayList<>();
	}

	// Getters and Setters
	public Property getProperty() {
		return property;
	}

	public ModelSelect property(Property property) {
		this.property = property;
		return this;
	}

	public List<SelectCase> getCases() {
		return cases;
	}

	public ModelSelect cases(List<SelectCase> cases) {
		this.cases = cases;
		return this;
	}

	public ModelSelect addCase(SelectCase selectCase) {
		this.cases.add(selectCase);
		return this;
	}
}
