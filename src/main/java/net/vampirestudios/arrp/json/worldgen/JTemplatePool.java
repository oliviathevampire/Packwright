package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JTemplatePool {
	public static final Codec<JTemplatePool> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("fallback", "minecraft:empty").forGetter(x -> x.fallback),
			Element.CODEC.listOf().fieldOf("elements").forGetter(x -> x.elements)
	).apply(i, (fallback, elements) -> new JTemplatePool().fallback(fallback).elements(elements)));

	private String fallback = "minecraft:empty";
	private List<Element> elements = new ArrayList<>();

	public static JTemplatePool pool() { return new JTemplatePool(); }
	public JTemplatePool fallback(String fallback) { this.fallback = fallback; return this; }
	public JTemplatePool elements(List<Element> elements) { this.elements = new ArrayList<>(elements); return this; }
	public JTemplatePool element(Element element) { this.elements.add(element); return this; }
	public JTemplatePool single(String location, String processors, Projection projection, int weight) {
		return element(Element.single(location, processors, projection, weight));
	}

	public static class Element {
		public static final Codec<Element> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("element_type").forGetter(x -> x.elementType),
				Codec.STRING.optionalFieldOf("location").forGetter(x -> x.location),
				Codec.STRING.optionalFieldOf("processors", "minecraft:empty").forGetter(x -> x.processors),
				Projection.CODEC.optionalFieldOf("projection", Projection.RIGID).forGetter(x -> x.projection),
				Codec.INT.fieldOf("weight").forGetter(x -> x.weight)
		).apply(i, (elementType, location, processors, projection, weight) -> new Element().elementType(elementType).location(location).processors(processors).projection(projection).weight(weight)));

		private Identifier elementType = Identifier.withDefaultNamespace("single_pool_element");
		private Optional<String> location = Optional.empty();
		private String processors = "minecraft:empty";
		private Projection projection = Projection.RIGID;
		private int weight = 1;

		public static Element single(String location, String processors, Projection projection, int weight) {
			return new Element().elementType("minecraft:single_pool_element").location(location).processors(processors).projection(projection).weight(weight);
		}

		public Element elementType(String elementType) { return elementType(Identifier.tryParse(elementType)); }
		public Element elementType(Identifier elementType) { this.elementType = elementType; return this; }
		public Element location(Optional<String> location) { this.location = location; return this; }
		public Element location(String location) { this.location = Optional.of(location); return this; }
		public Element processors(String processors) { this.processors = processors; return this; }
		public Element projection(Projection projection) { this.projection = projection; return this; }
		public Element weight(int weight) { this.weight = weight; return this; }
	}

	public enum Projection implements StringRepresentable {
		RIGID("rigid"),
		TERRAIN_MATCHING("terrain_matching");
		public static final Codec<Projection> CODEC = StringRepresentable.fromEnum(Projection::values);
		private final String name;
		Projection(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}
}
