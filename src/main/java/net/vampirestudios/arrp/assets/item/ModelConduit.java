package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:conduit" special model type.
 */
public class ModelConduit extends ModelSpecial {
    private String texture;  // Namespaced ID for texture

    protected ModelConduit() {
        super("minecraft:conduit");
    }

    // Static factory method
    public static ModelConduit conduit() {
        return new ModelConduit();
    }

    // Fluent methods
    public ModelConduit texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Getter
    public String getTexture() {
        return texture;
    }

    @Override
    public ModelConduit clone() {
        ModelConduit cloned = new ModelConduit();
        cloned.base(this.getBase());
        cloned.texture = this.texture;
        return cloned;
    }
}
