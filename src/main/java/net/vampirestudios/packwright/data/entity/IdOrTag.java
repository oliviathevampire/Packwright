package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;

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
