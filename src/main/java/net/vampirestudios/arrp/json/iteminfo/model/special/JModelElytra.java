package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:elytra" special model type.
 */
public class JModelElytra extends JModelSpecial {
    private String texture;  // Optional texture ID

    protected JModelElytra() {
        super("minecraft:elytra");
    }

    // Static factory method
    public static JModelElytra elytra() {
        return new JModelElytra();
    }

    // Fluent methods
    public JModelElytra texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Getter
    public String getTexture() {
        return texture;
    }

    @Override
    public JModelElytra clone() {
        JModelElytra cloned = new JModelElytra();
        cloned.base(this.getBase());
        cloned.texture = this.texture;
        return cloned;
    }
}
