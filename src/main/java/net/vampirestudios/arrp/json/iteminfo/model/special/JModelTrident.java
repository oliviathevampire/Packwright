package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:trident" special model type.
 */
public class JModelTrident extends JModelSpecial {
    private String texture;  // Texture for the trident

    protected JModelTrident() {
        super("minecraft:trident");
    }

    // Static factory method
    public static JModelTrident trident() {
        return new JModelTrident();
    }

    // Fluent methods
    public JModelTrident texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Getter
    public String getTexture() {
        return texture;
    }

    @Override
    public JModelTrident clone() {
        JModelTrident cloned = new JModelTrident();
        cloned.base(this.getBase());
        cloned.texture = this.texture;
        return cloned;
    }
}
