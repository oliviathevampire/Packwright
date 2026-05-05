// Codecs.java
package net.vampirestudios.arrp.json.codec;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.Objects;
import java.util.function.Function;

public final class Codecs {
	private Codecs() {
	}

	/** Accept either a single value or a JSON array of values. */
	public static <T> Codec<java.util.List<T>> oneOrList(Codec<T> elem) {
		return Codec.either(elem, elem.listOf())
				.xmap(e -> e.map(java.util.List::of, l -> l),
						l -> l.size() == 1 ? Either.left(l.getFirst()) : Either.right(l));
	}

	public static final Codec<JsonElement> JSON = new Codec<>() {
		@Override public <T> DataResult<T> encode(JsonElement v, DynamicOps<T> ops, T prefix) {
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, v).convert(ops).getValue());
		}
		@Override public <T> DataResult<Pair<JsonElement, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			return DataResult.success(Pair.of(el, input));
		}
	};

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
	 * @param typeGetter  extracts tag from value (e.g., JItemModel::getType)
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
}
