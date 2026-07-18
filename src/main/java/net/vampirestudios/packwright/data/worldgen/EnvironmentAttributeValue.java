package net.vampirestudios.packwright.data.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.packwright.util.DynamicMap;

import java.util.Optional;

/**
 * Single Environment Attribute value.
 * <p>
 * Encoded as JSON via Codec:
 *  - boolean
 *  - number (double)
 *  - string
 *  - free-form object (for object-valued attributes like {@code minecraft:gameplay/bed_rule})
 */
public abstract class EnvironmentAttributeValue {

    public static final Codec<EnvironmentAttributeValue> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(EnvironmentAttributeValue value, DynamicOps<T> ops, T prefix) {
            if (value instanceof BoolValue b) {
                return DataResult.success(ops.createBoolean(b.value));
            } else if (value instanceof NumberValue n) {
                return DataResult.success(ops.createDouble(n.value));
            } else if (value instanceof StringValue s) {
                return DataResult.success(ops.createString(s.value));
            } else if (value instanceof DynamicValue d) {
                return DataResult.success(d.value.convert(ops).getValue());
            }
            return DataResult.error(() -> "Unknown EnvironmentAttributeValue subclass: " + value.getClass());
        }

        @Override
        public <T> DataResult<Pair<EnvironmentAttributeValue, T>> decode(DynamicOps<T> ops, T input) {
            // try boolean
            Optional<Boolean> boolOpt = ops.getBooleanValue(input).result();
            if (boolOpt.isPresent()) {
                return DataResult.success(Pair.of(ofBoolean(boolOpt.get()), input));
            }

            // then number
            Optional<Number> numOpt = ops.getNumberValue(input).result();
            if (numOpt.isPresent()) {
                return DataResult.success(Pair.of(ofNumber(numOpt.get().doubleValue()), input));
            }

            // then string
            Optional<String> strOpt = ops.getStringValue(input).result();
            if (strOpt.isPresent()) {
                return DataResult.success(Pair.of(ofString(strOpt.get()), input));
            }

            // fallback: keep any other shape (objects, lists) as an opaque dynamic value
            return DataResult.success(Pair.of(new DynamicValue(new Dynamic<>(ops, input)), input));
        }
    };

    // Factory methods
    public static EnvironmentAttributeValue ofBoolean(boolean value) {
        return new BoolValue(value);
    }

    public static EnvironmentAttributeValue ofNumber(double value) {
        return new NumberValue(value);
    }

    public static EnvironmentAttributeValue ofString(String value) {
        return new StringValue(value);
    }

    /** for object-valued attributes, e.g. bed rules */
    public static EnvironmentAttributeValue ofObject(DynamicMap value) {
        return new DynamicValue(value.toDynamic());
    }

    /** for object-valued attributes with a real codec, e.g. bed rules */
    public static <T> EnvironmentAttributeValue ofEncoded(Codec<T> codec, T value) {
        Object raw = codec.encodeStart(JavaOps.INSTANCE, value).getOrThrow();
        return new DynamicValue(new Dynamic<>(JavaOps.INSTANCE, raw));
    }

    // Concrete variants

    public static final class BoolValue extends EnvironmentAttributeValue {
        public final boolean value;

        public BoolValue(boolean value) {
            this.value = value;
        }
    }

    public static final class NumberValue extends EnvironmentAttributeValue {
        public final double value;

        public NumberValue(double value) {
            this.value = value;
        }
    }

    public static final class StringValue extends EnvironmentAttributeValue {
        public final String value;

        public StringValue(String value) {
            this.value = value;
        }
    }

    public static final class DynamicValue extends EnvironmentAttributeValue {
        public final Dynamic<?> value;

        public DynamicValue(Dynamic<?> value) {
            this.value = value;
        }
    }
}
