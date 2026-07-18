package net.vampirestudios.packwright.data.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TemplatePool {

	public static final Codec<TemplatePool> CODEC =
			RecordCodecBuilder.create(instance -> instance.group(
					Identifier.CODEC
							.fieldOf("fallback")
							.forGetter(TemplatePool::fallback),

					Entry.CODEC.listOf()
							.fieldOf("elements")
							.forGetter(TemplatePool::elements)
			).apply(instance, TemplatePool::new));

	private Identifier fallback =
			Identifier.withDefaultNamespace("empty");

	private final List<Entry> elements =
			new ArrayList<>();

	private TemplatePool() {
	}

	private TemplatePool(
			Identifier fallback,
			List<Entry> elements
	) {
		this.fallback = Objects.requireNonNull(
				fallback,
				"fallback"
		);

		this.elements.addAll(elements);
	}

	public static TemplatePool pool() {
		return new TemplatePool();
	}

	public TemplatePool fallback(Identifier fallback) {
		this.fallback = Objects.requireNonNull(
				fallback,
				"fallback"
		);

		return this;
	}

	public TemplatePool element(
			PoolElement element,
			int weight
	) {
		this.elements.add(
				new Entry(element, weight)
		);

		return this;
	}

	public TemplatePool single(
			Identifier location,
			Identifier processors,
			Projection projection,
			int weight
	) {
		return element(
				new SinglePoolElement(
						location,
						processors,
						projection
				),
				weight
		);
	}

	public TemplatePool legacySingle(
			Identifier location,
			Identifier processors,
			Projection projection,
			int weight
	) {
		return element(
				new LegacySinglePoolElement(
						location,
						processors,
						projection
				),
				weight
		);
	}

	public TemplatePool list(
			List<PoolElement> elements,
			Projection projection,
			int weight
	) {
		return element(
				new ListPoolElement(
						elements,
						projection
				),
				weight
		);
	}

	public TemplatePool empty(int weight) {
		return element(
				new EmptyPoolElement(),
				weight
		);
	}

	public Identifier fallback() {
		return this.fallback;
	}

	public List<Entry> elements() {
		return List.copyOf(this.elements);
	}

	/**
	 * One weighted entry in the pool.
	 *
	 * <p>The wrapper is important: vanilla expects every item in the pool's
	 * {@code elements} list to contain separate {@code element} and
	 * {@code weight} fields.</p>
	 */
	public record Entry(
			PoolElement element,
			int weight
	) {
		public static final Codec<Entry> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						PoolElement.CODEC
								.fieldOf("element")
								.forGetter(Entry::element),

						Codec.intRange(1, 150)
								.fieldOf("weight")
								.forGetter(Entry::weight)
				).apply(instance, Entry::new));

		public Entry {
			Objects.requireNonNull(
					element,
					"element"
			);

			if (weight < 1 || weight > 150) {
				throw new IllegalArgumentException(
						"Template-pool weight must be between 1 and 150"
				);
			}
		}
	}

	public sealed interface PoolElement permits
			SinglePoolElement,
			LegacySinglePoolElement,
			ListPoolElement,
			EmptyPoolElement {

		Codec<PoolElement> CODEC = new Codec<>() {
			@Override
			public <T> DataResult<Pair<PoolElement, T>> decode(
					DynamicOps<T> ops,
					T input
			) {
				return ops.getMap(input).flatMap(map -> {
					String rawType = string(
							map,
							ops,
							"element_type",
							""
					);

					return switch (normalizeType(rawType)) {
						case "single_pool_element" ->
								decodeElement(
										SinglePoolElement.CODEC,
										ops,
										input
								);

						case "legacy_single_pool_element" ->
								decodeElement(
										LegacySinglePoolElement.CODEC,
										ops,
										input
								);

						case "list_pool_element" ->
								decodeElement(
										ListPoolElement.CODEC,
										ops,
										input
								);

						case "empty_pool_element" ->
								decodeElement(
										EmptyPoolElement.CODEC,
										ops,
										input
								);

						default -> DataResult.error(
								() -> rawType.isEmpty()
										? "Pool element is missing element_type"
										: "Unsupported pool element type: "
										  + rawType
						);
					};
				});
			}

			@Override
			public <T> DataResult<T> encode(
					PoolElement input,
					DynamicOps<T> ops,
					T prefix
			) {
				if (input instanceof SinglePoolElement element) {
					return SinglePoolElement.CODEC
							.encode(element, ops, prefix);
				}

				if (input instanceof LegacySinglePoolElement element) {
					return LegacySinglePoolElement.CODEC
							.encode(element, ops, prefix);
				}

				if (input instanceof ListPoolElement element) {
					return ListPoolElement.CODEC
							.encode(element, ops, prefix);
				}

				if (input instanceof EmptyPoolElement element) {
					return EmptyPoolElement.CODEC
							.encode(element, ops, prefix);
				}

				return DataResult.error(
						() -> "Unsupported pool element: "
								+ input.getClass().getName()
				);
			}
		};

		Projection projection();
	}

	public record SinglePoolElement(
			Identifier location,
			Identifier processors,
			Projection projection
	) implements PoolElement {

		private static final Identifier TYPE =
				Identifier.withDefaultNamespace(
						"single_pool_element"
				);

		public static final Codec<SinglePoolElement> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						typeCodec(TYPE)
								.fieldOf("element_type")
								.forGetter(element -> TYPE),

						Identifier.CODEC
								.fieldOf("location")
								.forGetter(
										SinglePoolElement::location
								),

						Identifier.CODEC
								.fieldOf("processors")
								.forGetter(
										SinglePoolElement::processors
								),

						Projection.CODEC
								.fieldOf("projection")
								.forGetter(
										SinglePoolElement::projection
								)
				).apply(
						instance,
						(type, location, processors, projection) ->
								new SinglePoolElement(
										location,
										processors,
										projection
								)
				));

		public SinglePoolElement {
			Objects.requireNonNull(location, "location");
			Objects.requireNonNull(processors, "processors");
			Objects.requireNonNull(projection, "projection");
		}
	}

	public record LegacySinglePoolElement(
			Identifier location,
			Identifier processors,
			Projection projection
	) implements PoolElement {

		private static final Identifier TYPE =
				Identifier.withDefaultNamespace(
						"legacy_single_pool_element"
				);

		public static final Codec<LegacySinglePoolElement> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						typeCodec(TYPE)
								.fieldOf("element_type")
								.forGetter(element -> TYPE),

						Identifier.CODEC
								.fieldOf("location")
								.forGetter(
										LegacySinglePoolElement::location
								),

						Identifier.CODEC
								.fieldOf("processors")
								.forGetter(
										LegacySinglePoolElement::processors
								),

						Projection.CODEC
								.fieldOf("projection")
								.forGetter(
										LegacySinglePoolElement::projection
								)
				).apply(
						instance,
						(type, location, processors, projection) ->
								new LegacySinglePoolElement(
										location,
										processors,
										projection
								)
				));

		public LegacySinglePoolElement {
			Objects.requireNonNull(location, "location");
			Objects.requireNonNull(processors, "processors");
			Objects.requireNonNull(projection, "projection");
		}
	}

	public record ListPoolElement(
			List<PoolElement> elements,
			Projection projection
	) implements PoolElement {

		private static final Identifier TYPE =
				Identifier.withDefaultNamespace(
						"list_pool_element"
				);

		public static final Codec<ListPoolElement> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						typeCodec(TYPE)
								.fieldOf("element_type")
								.forGetter(element -> TYPE),

						PoolElement.CODEC.listOf()
								.fieldOf("elements")
								.forGetter(
										ListPoolElement::elements
								),

						Projection.CODEC
								.fieldOf("projection")
								.forGetter(
										ListPoolElement::projection
								)
				).apply(
						instance,
						(type, elements, projection) ->
								new ListPoolElement(
										elements,
										projection
								)
				));

		public ListPoolElement {
			elements = List.copyOf(elements);
			Objects.requireNonNull(projection, "projection");

			if (elements.isEmpty()) {
				throw new IllegalArgumentException(
						"A list pool element cannot be empty"
				);
			}
		}
	}

	public record EmptyPoolElement() implements PoolElement {
		private static final Identifier TYPE =
				Identifier.withDefaultNamespace(
						"empty_pool_element"
				);

		public static final Codec<EmptyPoolElement> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						typeCodec(TYPE)
								.fieldOf("element_type")
								.forGetter(element -> TYPE)
				).apply(
						instance,
						type -> new EmptyPoolElement()
				));

		@Override
		public Projection projection() {
			return Projection.RIGID;
		}
	}

	public enum Projection {
		RIGID("rigid"),
		TERRAIN_MATCHING("terrain_matching");

		public static final Codec<Projection> CODEC =
				Codec.STRING.comapFlatMap(
						id -> {
							for (Projection projection : values()) {
								if (projection.id.equals(id)) {
									return DataResult.success(projection);
								}
							}

							return DataResult.error(
									() -> "Unknown template-pool projection: "
											+ id
							);
						},
						Projection::id
				);

		private final String id;

		Projection(String id) {
			this.id = id;
		}

		public String id() {
			return this.id;
		}
	}

	private static Codec<Identifier> typeCodec(
			Identifier expected
	) {
		return Identifier.CODEC.validate(type ->
				type.equals(expected)
						? DataResult.success(type)
						: DataResult.error(
						() -> "Expected element type "
							  + expected
							  + ", got "
							  + type
				)
		);
	}

	private static <T, E extends PoolElement>
	DataResult<Pair<PoolElement, T>> decodeElement(
			Codec<E> codec,
			DynamicOps<T> ops,
			T input
	) {
		return codec.decode(ops, input).map(pair ->
				Pair.of(
						pair.getFirst(),
						pair.getSecond()
				)
		);
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');

		return separator >= 0
				? type.substring(separator + 1)
				: type;
	}

	private static <T> String string(
			MapLike<T> map,
			DynamicOps<T> ops,
			String key,
			String fallback
	) {
		T value = map.get(key);

		return value == null
				? fallback
				: ops.getStringValue(value)
				  .result()
				  .orElse(fallback);
	}
}