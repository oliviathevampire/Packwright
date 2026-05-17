package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:trident" special model type.
 */
public class ModelTrident extends ModelSpecial {
    private String texture;  // Texture for the trident

    protected ModelTrident() {
        super("minecraft:trident");
    }

    // Static factory method
    public static ModelTrident trident() {
        return new ModelTrident();
    }

    // Fluent methods
    public ModelTrident texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Getter
    public String getTexture() {
        return texture;
    }

    @Override
    public ModelTrident clone() {
        ModelTrident cloned = new ModelTrident();
        cloned.base(this.getBase());
        cloned.texture = this.texture;
        return cloned;
    }
}
