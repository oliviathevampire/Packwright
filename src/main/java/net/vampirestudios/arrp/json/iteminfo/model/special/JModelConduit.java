package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:conduit" special model type.
 */
public class JModelConduit extends JModelSpecial {
    private String texture;  // Namespaced ID for texture

    protected JModelConduit() {
        super("minecraft:conduit");
    }

    // Static factory method
    public static JModelConduit conduit() {
        return new JModelConduit();
    }

    // Fluent methods
    public JModelConduit texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Getter
    public String getTexture() {
        return texture;
    }

    @Override
    public JModelConduit clone() {
        JModelConduit cloned = new JModelConduit();
        cloned.base(this.getBase());
        cloned.texture = this.texture;
        return cloned;
    }
}
