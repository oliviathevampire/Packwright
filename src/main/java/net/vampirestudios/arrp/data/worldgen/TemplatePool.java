package net.vampirestudios.arrp.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class TemplatePool {
	public static final Codec<TemplatePool> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.optionalFieldOf("fallback", Identifier.withDefaultNamespace("empty")).forGetter(x -> x.fallback),
			Element.CODEC.listOf().fieldOf("elements").forGetter(x -> x.elements)
	).apply(i, (fallback, elements) -> new TemplatePool().fallback(fallback).elements(elements)));

	private Identifier fallback = Identifier.withDefaultNamespace("empty");
	private List<Element> elements = new ArrayList<>();

	public static TemplatePool pool() {
		return new TemplatePool();
	}

	public TemplatePool fallback(Identifier fallback) {
		this.fallback = fallback;
		return this;
	}

	public TemplatePool elements(List<Element> elements) {
		this.elements = new ArrayList<>(elements);
		return this;
	}

	public TemplatePool element(Element element) {
		this.elements.add(element);
		return this;
	}

	public TemplatePool single(Identifier location, Identifier processors, Projection projection, int weight) {
		return element(Element.single(location, processors, projection, weight));
	}

	public enum Projection implements StringRepresentable {
		RIGID("rigid"),
		TERRAIN_MATCHING("terrain_matching");

		public static final Codec<Projection> CODEC = StringRepresentable.fromEnum(Projection::values);
		private final String name;

		Projection(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public static class Element {
		public static final Codec<Element> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("element_type").forGetter(x -> x.elementType),
				Identifier.CODEC.optionalFieldOf("location").forGetter(x -> x.location),
				Identifier.CODEC.optionalFieldOf("processors", Identifier.withDefaultNamespace("empty")).forGetter(x -> x.processors),
				Projection.CODEC.optionalFieldOf("projection", Projection.RIGID).forGetter(x -> x.projection),
				Codec.INT.fieldOf("weight").forGetter(x -> x.weight)
		).apply(i, (elementType, location, processors, projection, weight) -> new Element().elementType(elementType).location(location).processors(processors).projection(projection).weight(weight)));

		private Identifier elementType = Identifier.withDefaultNamespace("single_pool_element");
		private Optional<Identifier> location = Optional.empty();
		private Identifier processors = Identifier.withDefaultNamespace("empty");
		private Projection projection = Projection.RIGID;
		private int weight = 1;

		public static Element single(Identifier location, Identifier processors, Projection projection, int weight) {
			return new Element().elementType(vanillaId("single_pool_element")).location(location).processors(processors).projection(projection).weight(weight);
		}

		public Element elementType(Identifier elementType) {
			this.elementType = elementType;
			return this;
		}

		public Element location(Optional<Identifier> location) {
			this.location = location;
			return this;
		}

		public Element location(Identifier location) {
			this.location = Optional.of(location);
			return this;
		}

		public Element processors(Identifier processors) {
			this.processors = processors;
			return this;
		}

		public Element projection(Projection projection) {
			this.projection = projection;
			return this;
		}

		public Element weight(int weight) {
			this.weight = weight;
			return this;
		}
	}
}
