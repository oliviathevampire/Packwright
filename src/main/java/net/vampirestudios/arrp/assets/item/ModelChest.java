package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:chest" special model type.
 */
public class ModelChest extends ModelSpecial {

    private String texture;  // Namespaced ID for texture
    private float openness;  // 0.0 (closed) to 1.0 (open)

    public ModelChest() {
        super("minecraft:chest");
    }

    // Getters and Setters
    public String getTexture() {
        return texture;
    }

    public ModelChest texture(String texture) {
        this.texture = texture;
        return this;
    }

    public float getOpenness() {
        return openness;
    }

    public ModelChest openness(float openness) {
        this.openness = openness;
        return this;
    }

    @Override
    public ModelChest clone() {
        ModelChest cloned = new ModelChest();
        cloned.base(this.getBase());
        cloned.texture(this.texture);
        cloned.openness(this.openness);
        return cloned;
    }
}
