package net.vampirestudios.arrp.assets.models;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a transformation for item model definitions.
 * Encoded as either a 16-element column-major float matrix or a decomposed
 * object with {@code translation}, {@code left_rotation}, {@code right_rotation},
 * and {@code scale} fields.
 */
public class Transformation {
    private static final Codec<float[]> VEC3 = Codec.FLOAT.listOf()
            .xmap(l -> new float[]{l.getFirst(), l.get(1), l.get(2)},
                  a -> List.of(a[0], a[1], a[2]));

    private static final Codec<float[]> QUAT_ARRAY = Codec.FLOAT.listOf()
            .xmap(l -> new float[]{l.getFirst(), l.get(1), l.get(2), l.get(3)},
                  a -> List.of(a[0], a[1], a[2], a[3]));

    private static final Codec<float[]> QUAT_OBJ = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.fieldOf("x").forGetter(a -> a[0]),
            Codec.FLOAT.fieldOf("y").forGetter(a -> a[1]),
            Codec.FLOAT.fieldOf("z").forGetter(a -> a[2]),
            Codec.FLOAT.fieldOf("w").forGetter(a -> a[3])
    ).apply(i, (x, y, z, w) -> new float[]{x, y, z, w}));

    // Accepts [x,y,z,w] array or {x,y,z,w} object; always encodes as array
    private static final Codec<float[]> QUAT = Codec.either(QUAT_ARRAY, QUAT_OBJ)
            .xmap(e -> e.map(a -> a, a -> a), Either::left);

    private static final Codec<float[]> MATRIX16 = Codec.FLOAT.listOf()
            .validate(l -> l.size() == 16
                    ? DataResult.success(l)
                    : DataResult.error(() -> "transformation matrix must have exactly 16 elements, got " + l.size()))
            .xmap(l -> { float[] a = new float[16]; for (int i = 0; i < 16; i++) a[i] = l.get(i); return a; },
                  a -> { List<Float> l = new ArrayList<>(16); for (float f : a) l.add(f); return l; });

    private static final Codec<Transformation> DECOMPOSED = RecordCodecBuilder.create(i -> i.group(
            VEC3.optionalFieldOf("translation").forGetter(t -> Optional.ofNullable(t.translation)),
            QUAT.optionalFieldOf("left_rotation").forGetter(t -> Optional.ofNullable(t.leftRotation)),
            QUAT.optionalFieldOf("right_rotation").forGetter(t -> Optional.ofNullable(t.rightRotation)),
            VEC3.optionalFieldOf("scale").forGetter(t -> Optional.ofNullable(t.scale))
    ).apply(i, (tr, lr, rr, sc) -> {
        Transformation t = new Transformation();
        tr.ifPresent(v -> t.translation = v);
        lr.ifPresent(v -> t.leftRotation = v);
        rr.ifPresent(v -> t.rightRotation = v);
        sc.ifPresent(v -> t.scale = v);
        return t;
    }));

    public static final Codec<Transformation> CODEC = Codec.either(MATRIX16, DECOMPOSED)
            .xmap(
                    e -> e.map(m -> { Transformation t = new Transformation(); t.matrix = m; return t; }, t -> t),
                    t -> t.matrix != null ? Either.left(t.matrix) : Either.right(t)
            );

    private float[] matrix;        // 16 floats, column-major
    private float[] translation;   // [x, y, z]
    private float[] leftRotation;  // [x, y, z, w]
    private float[] rightRotation; // [x, y, z, w]
    private float[] scale;         // [x, y, z]

    private Transformation() {}

    public static Transformation ofMatrix(float... matrix) {
        if (matrix.length != 16) throw new IllegalArgumentException("matrix must have 16 elements");
        Transformation t = new Transformation();
        t.matrix = matrix;
        return t;
    }

    public static Transformation decomposed() {
        return new Transformation();
    }

    public Transformation translation(float x, float y, float z) {
        this.translation = new float[]{x, y, z};
        return this;
    }

    public Transformation leftRotation(float x, float y, float z, float w) {
        this.leftRotation = new float[]{x, y, z, w};
        return this;
    }

    public Transformation rightRotation(float x, float y, float z, float w) {
        this.rightRotation = new float[]{x, y, z, w};
        return this;
    }

    public Transformation scale(float x, float y, float z) {
        this.scale = new float[]{x, y, z};
        return this;
    }
}
