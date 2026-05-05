package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;

public final class JIdOrTag {
    public static final Codec<JIdOrTag> CODEC = Codec.STRING.comapFlatMap(
            s -> {
                boolean tag = s.startsWith("#");
                String raw = tag ? s.substring(1) : s;
                Identifier id = Identifier.tryParse(raw);
                if (id == null) return DataResult.error(() -> "Invalid id/tag: " + s);
                return DataResult.success(new JIdOrTag(tag, id));
            },
            v -> (v.tag ? "#" : "") + v.id.toString()
    );

    private boolean tag;
    private Identifier id;

    public JIdOrTag() {}

    public JIdOrTag(boolean tag, Identifier id) {
        this.tag = tag;
        this.id = id;
    }

    public static JIdOrTag id(Identifier id)  { return new JIdOrTag(false, id); }
    public static JIdOrTag tag(Identifier id) { return new JIdOrTag(true, id); }

    public boolean isTag() { return tag; }
    public Identifier getId() { return id; }
}
