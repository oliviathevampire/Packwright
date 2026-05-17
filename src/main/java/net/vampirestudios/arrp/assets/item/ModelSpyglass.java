package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:spyglass" special model type.
 */
public class ModelSpyglass extends ModelSpecial {
    private String scopeTexture;  // Texture for the scope overlay

    protected ModelSpyglass() {
        super("minecraft:spyglass");
    }

    // Static factory method
    public static ModelSpyglass spyglass() {
        return new ModelSpyglass();
    }

    // Fluent methods
    public ModelSpyglass scopeTexture(String scopeTexture) {
        this.scopeTexture = scopeTexture;
        return this;
    }

    // Getter
    public String getScopeTexture() {
        return scopeTexture;
    }

    @Override
    public ModelSpyglass clone() {
        ModelSpyglass cloned = new ModelSpyglass();
        cloned.base(this.getBase());
        cloned.scopeTexture = this.scopeTexture;
        return cloned;
    }
}
