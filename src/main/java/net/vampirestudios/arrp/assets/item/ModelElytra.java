package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:elytra" special model type.
 */
public class ModelElytra extends ModelSpecial {
    private String texture;  // Optional texture ID

    protected ModelElytra() {
        super("minecraft:elytra");
    }

    // Static factory method
    public static ModelElytra elytra() {
        return new ModelElytra();
    }

    // Fluent methods
    public ModelElytra texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Getter
    public String getTexture() {
        return texture;
    }

    @Override
    public ModelElytra clone() {
        ModelElytra cloned = new ModelElytra();
        cloned.base(this.getBase());
        cloned.texture = this.texture;
        return cloned;
    }
}
