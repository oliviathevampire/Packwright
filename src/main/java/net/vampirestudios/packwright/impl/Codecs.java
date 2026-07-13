// Codecs.java
package net.vampirestudios.packwright.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Codecs {
	private Codecs() {
	}

	/** Accept either a single value or a JSON array of values. */
	public static <T> Codec<java.util.List<T>> oneOrList(Codec<T> elem) {
		return Codec.either(elem, elem.listOf())
				.xmap(e -> e.map(java.util.List::of, l -> l),
						l -> l.size() == 1 ? Either.left(l.getFirst()) : Either.right(l));
	}

	public static <B, T> Codec<B> tagged(
			String tagKey,
			Function<B, T> typeGetter,
			Function<T, Codec<? extends B>> lookup,
			Codec<T> tagCodec
	) {
		return Codec.of(
				// Encoder: encode body, then inject tagKey: <tag>
				new Encoder<>() {
					@Override public <X> DataResult<X> encode(B value, DynamicOps<X> ops, X prefix) {
						T tag = Objects.requireNonNull(typeGetter.apply(value), "tag is null for " + value);
						@SuppressWarnings("unchecked")
						Codec<B> sub = (Codec<B>) Objects.requireNonNull(lookup.apply(tag), "no codec for tag: " + tag);
						var body = sub.encode(value, ops, prefix);
						if (body.result().isEmpty()) return body;
						var tagNode = tagCodec.encodeStart(ops, tag);
						if (tagNode.result().isEmpty()) return tagNode;
						return ops.mergeToMap(body.result().get(), ops.createString(tagKey), tagNode.result().get());
					}
				},
				// Decoder: read tagKey with tagCodec, dispatch to subtype
				new Decoder<>() {
					@Override public <X> DataResult<com.mojang.datafixers.util.Pair<B, X>> decode(DynamicOps<X> ops, X input) {
						var mapRes = ops.getMap(input);
						if (mapRes.result().isEmpty()) return DataResult.error(() -> "expected object for tagged(" + tagKey + ")");
						X tagNode = mapRes.result().get().get(tagKey);
						if (tagNode == null) return DataResult.error(() -> "missing '" + tagKey + "'");
						var tagRes = tagCodec.parse(ops, tagNode);
						if (tagRes.result().isEmpty()) return DataResult.error(() -> "bad '" + tagKey + "'");
						T tag = tagRes.result().get();

						@SuppressWarnings("unchecked")
						Codec<B> sub = (Codec<B>) lookup.apply(tag);
						if (sub == null) return DataResult.error(() -> "unknown tag: " + tag);
						return sub.decode(ops, input);
					}
				}
		);
	}

	/**
	 * Build a tagged-union codec:
	 * - Reads field {@code tagKey} to choose a subtype codec.
	 * - On encode, injects {@code tagKey} with {@code typeGetter}.
	 *
	 * @param tagKey      field name (e.g., "type" or "property")
	 * @param typeGetter  extracts tag from value (e.g., ItemModel::getType)
	 * @param lookup      maps tag -> subtype codec
	 */
	public static <B> Codec<B> tagged(String tagKey,
									  Function<B, String> typeGetter,
									  Function<String, Codec<? extends B>> lookup) {
		return Codec.of(
				// Encoder: encode with subtype, then merge the tag field
				new Encoder<>() {
					@Override
					public <T> DataResult<T> encode(B value, DynamicOps<T> ops, T prefix) {
						String tag = Objects.requireNonNull(typeGetter.apply(value), "tag is null for " + value);
						@SuppressWarnings("unchecked")
						Codec<B> sub = (Codec<B>) Objects.requireNonNull(lookup.apply(tag), "no codec for tag: " + tag);
						DataResult<T> body = sub.encode(value, ops, prefix);
						return body.flatMap(obj -> ops.mergeToMap(obj, ops.createString(tagKey), ops.createString(tag)));
					}
				},
				// Decoder: read tag field and dispatch
				new Decoder<>() {
					@Override
					public <T> DataResult<com.mojang.datafixers.util.Pair<B, T>> decode(DynamicOps<T> ops, T input) {
						var mapRes = ops.getMap(input);
						if (mapRes.result().isEmpty())
							return DataResult.error(() -> "expected object for tagged(" + tagKey + ")");
						MapLike<T> map = mapRes.result().get();
						T tagNode = map.get(tagKey);
						if (tagNode == null) return DataResult.error(() -> "missing '" + tagKey + "'");
						var s = ops.getStringValue(tagNode);
						if (s.result().isEmpty()) return DataResult.error(() -> "'" + tagKey + "' must be a string");
						String tag = s.result().get();

						@SuppressWarnings("unchecked")
						Codec<B> sub = (Codec<B>) lookup.apply(tag);
						if (sub == null) return DataResult.error(() -> "unknown tag '" + tagKey + "': " + tag);

						return sub.decode(ops, input);
					}
				}
		);
	}

	/**
	 * Like {@link #tagged} but returns a {@link MapCodec} so the tag key and all
	 * subtype fields are merged flat into the parent object rather than nested
	 * under a separate key.  {@code lookup} must return {@code MapCodec}s.
	 */
	public static <B> MapCodec<B> taggedMap(
			String tagKey,
			Function<B, String> typeGetter,
			Function<String, MapCodec<? extends B>> lookup
	) {
		return MapCodec.of(
				new MapEncoder.Implementation<>() {
					@Override
					public <T> RecordBuilder<T> encode(B value, DynamicOps<T> ops, RecordBuilder<T> prefix) {
						String tag = Objects.requireNonNull(typeGetter.apply(value), "tag is null for " + value);
						@SuppressWarnings("unchecked")
						MapCodec<B> sub = (MapCodec<B>) lookup.apply(tag);
						if (sub == null) return prefix.withErrorsFrom(DataResult.error(() -> "no codec for tag: " + tag));
						prefix.add(ops.createString(tagKey), ops.createString(tag));
						return sub.encode(value, ops, prefix);
					}

					@Override
					public <T> Stream<T> keys(DynamicOps<T> ops) {
						return Stream.of(ops.createString(tagKey));
					}
				},
				new MapDecoder.Implementation<>() {
					@Override
					public <T> DataResult<B> decode(DynamicOps<T> ops, MapLike<T> input) {
						T tagNode = input.get(tagKey);
						if (tagNode == null) return DataResult.error(() -> "missing '" + tagKey + "'");
						var tagRes = ops.getStringValue(tagNode);
						if (tagRes.result().isEmpty()) return DataResult.error(() -> "'" + tagKey + "' must be a string");
						String tag = tagRes.result().get();
						@SuppressWarnings("unchecked")
						MapCodec<B> sub = (MapCodec<B>) lookup.apply(tag);
						if (sub == null) return DataResult.error(() -> "unknown '" + tagKey + "': " + tag);
						return sub.decode(ops, input);
					}

					@Override
					public <T> Stream<T> keys(DynamicOps<T> ops) {
						return Stream.of(ops.createString(tagKey));
					}
				}
		);
	}
}
