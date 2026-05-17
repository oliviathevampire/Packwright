package net.vampirestudios.arrp.assets.item;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;

import java.util.List;
import java.util.Optional;

/**
 * Represents a case in a select model.
 */
public class SelectCase implements Cloneable {
    private Object when; // Can be a single value or a list of values
    private ItemModel model;

    public SelectCase() {}

    public SelectCase(Object when, ItemModel model) {
        this.when = when;
        this.model = model;
    }

    public static SelectCase of(Object when, ItemModel model) {
        return new SelectCase(when, model);
    }

    // Getters and Setters
    public Object getWhen() {
        return when;
    }

    public ItemModel getModel() {
        return model;
    }

    // ---- Codecs ----

    /** Single primitive (string/number/boolean). */
    private static final Codec<Object> PRIMITIVE = Codec.of(
            // Encoder
            new Encoder<>() {
                @Override public <T> DataResult<T> encode(Object v, DynamicOps<T> ops, T prefix) {
                    if (v instanceof String s)   return DataResult.success(ops.createString(s));
                    if (v instanceof Boolean b)  return DataResult.success(ops.createBoolean(b));
                    if (v instanceof Number n) {
                        if (n instanceof Integer || n instanceof Short || n instanceof Byte)
                            return DataResult.success(ops.createInt(n.intValue()));
                        if (n instanceof Long)
                            return DataResult.success(ops.createLong(n.longValue()));
                        if (n instanceof Float)
                            return DataResult.success(ops.createFloat(n.floatValue()));
                        return DataResult.success(ops.createDouble(n.doubleValue()));
                    }
                    return DataResult.error(() -> "SelectCase.when: unsupported value type: " + v.getClass());
                }
            },
            // Decoder
            new Decoder<>() {
                @Override public <T> DataResult<com.mojang.datafixers.util.Pair<Object,T>> decode(DynamicOps<T> ops, T input) {
                    // try boolean → number → string
                    Optional<Boolean> b = ops.getBooleanValue(input).result();
                    if (b.isPresent()) return DataResult.success(com.mojang.datafixers.util.Pair.of(b.get(), input));

                    Optional<Number> num = ops.getNumberValue(input).result();
                    if (num.isPresent()) {
                        Number n = num.get();
                        double d = n.doubleValue();
                        if (Math.rint(d) == d) {
                            long L = n.longValue();
                            Object boxed = (L >= Integer.MIN_VALUE && L <= Integer.MAX_VALUE) ? (int)L : L;
                            return DataResult.success(com.mojang.datafixers.util.Pair.of(boxed, input));
                        }
                        return DataResult.success(com.mojang.datafixers.util.Pair.of(d, input));
                    }

                    Optional<String> s = ops.getStringValue(input).result();
					return s.<DataResult<com.mojang.datafixers.util.Pair<Object, T>>>map(string ->
                            DataResult.success(com.mojang.datafixers.util.Pair.of(string, input))
                    ).orElseGet(() -> DataResult.error(() -> "SelectCase.when: expected string/number/boolean"));
				}
            }
    );

    /** Accept single primitive or array of primitives; keep `Object` as value-or-list. */
    private static final Codec<Object> WHEN_CODEC =
            Codec.either(PRIMITIVE, PRIMITIVE.listOf())
                    .xmap(
                            e -> e.map(v -> v, l -> l), // Either -> Object
                            o -> (o instanceof List<?> l) ? Either.right((List<Object>) l) : Either.left(o)
                    );

    public static final Codec<SelectCase> CODEC =
            com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group(
                    WHEN_CODEC.fieldOf("when").forGetter(SelectCase::getWhen),
                    ItemModel.CODEC.fieldOf("model").forGetter(SelectCase::getModel)
            ).apply(i, SelectCase::new));
}
