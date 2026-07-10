package net.vampirestudios.packwright.data.worldgen;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import java.util.Optional;

/**
 * Single Environment Attribute value.
 *
 * Encoded as JSON via Codec:
 *  - boolean
 *  - number (double)
 *  - string
 *  - free-form JSON (for object-valued attributes like {@code minecraft:gameplay/bed_rule})
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
            } else if (value instanceof JsonValue j) {
                return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, j.value).convert(ops).getValue());
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

            // fallback: keep any other shape (objects, lists) as raw JSON
            JsonElement json = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
            return DataResult.success(Pair.of(ofJson(json), input));
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
    public static EnvironmentAttributeValue ofJson(JsonElement value) {
        return new JsonValue(value);
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

    public static final class JsonValue extends EnvironmentAttributeValue {
        public final JsonElement value;

        public JsonValue(JsonElement value) {
            this.value = value.deepCopy();
        }
    }
}
