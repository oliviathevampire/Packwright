package net.vampirestudios.packwright.data.entity;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.List;

public final class IdOrTag {
    public static final Codec<IdOrTag> CODEC = Codec.STRING.comapFlatMap(
            s -> {
                boolean tag = s.startsWith("#");
                String raw = tag ? s.substring(1) : s;
                Identifier id = Identifier.tryParse(raw);
                if (id == null) return DataResult.error(() -> "Invalid id/tag: " + s);
                return DataResult.success(new IdOrTag(tag, id));
            },
            v -> (v.tag ? "#" : "") + v.id.toString()
    );

    /**
     * Mirrors vanilla's {@code RegistryCodecs.homogeneousList} shape (used by e.g.
     * {@code BiomeCheck}/{@code StructureCheck}): either a single {@code "#tag"} string,
     * or one-or-many bare id strings (a single id, or a JSON array of ids).
     */
    public static final Codec<List<IdOrTag>> LIST_CODEC = Codec.either(
            Codec.STRING.comapFlatMap(
                    s -> {
                        if (!s.startsWith("#")) {
                            return DataResult.error(() -> "Not a tag reference: " + s);
                        }
                        Identifier id = Identifier.tryParse(s.substring(1));
                        if (id == null) return DataResult.error(() -> "Invalid tag: " + s);
                        return DataResult.success(IdOrTag.tag(id));
                    },
                    v -> "#" + v.id.toString()
            ),
            Codecs.oneOrList(Identifier.CODEC)
    ).xmap(
            either -> either.map(List::of, ids -> ids.stream().map(IdOrTag::id).toList()),
            list -> (list.size() == 1 && list.get(0).isTag())
                    ? Either.left(list.get(0))
                    : Either.right(list.stream().map(IdOrTag::getId).toList())
    );

    private boolean tag;
    private Identifier id;

    public IdOrTag() {}

    public IdOrTag(boolean tag, Identifier id) {
        this.tag = tag;
        this.id = id;
    }

    public static IdOrTag id(Identifier id)  { return new IdOrTag(false, id); }
    public static IdOrTag tag(Identifier id) { return new IdOrTag(true, id); }

    public boolean isTag() { return tag; }
    public Identifier getId() { return id; }
}
