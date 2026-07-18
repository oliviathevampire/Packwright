package net.vampirestudios.packwright.data.worldgen.feature.builders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

/** places a weighted-random prebuilt NBT structure template with random rotation; {@code minecraft:template} in vanilla */
public final class TemplateFeatureBuilder extends FeatureBuilder {
	private static final List<String> ALL_ROTATIONS = List.of("none", "clockwise_90", "clockwise_180", "counterclockwise_90");

	private final List<WeightedTemplate> templates = new ArrayList<>();

	public TemplateFeatureBuilder() {
		super("minecraft:template");
	}

	public TemplateFeatureBuilder template(Identifier template, int weight) {
		return template(template, weight, ALL_ROTATIONS);
	}

	public TemplateFeatureBuilder template(Identifier template, int weight, List<String> rotations) {
		templates.add(new WeightedTemplate(new TemplateEntry(template, rotations), weight));
		this.feature.property("templates", templates, WeightedTemplate.CODEC);
		return this;
	}

	public record TemplateEntry(Identifier id, List<String> rotations) {
		public static final Codec<TemplateEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("id").forGetter(TemplateEntry::id),
				Codec.STRING.listOf().fieldOf("rotations").orElse(ALL_ROTATIONS).forGetter(TemplateEntry::rotations)
		).apply(i, TemplateEntry::new));
	}

	public record WeightedTemplate(TemplateEntry data, int weight) {
		public static final Codec<WeightedTemplate> CODEC = RecordCodecBuilder.create(i -> i.group(
				TemplateEntry.CODEC.fieldOf("data").forGetter(WeightedTemplate::data),
				Codec.INT.fieldOf("weight").forGetter(WeightedTemplate::weight)
		).apply(i, WeightedTemplate::new));
	}
}
