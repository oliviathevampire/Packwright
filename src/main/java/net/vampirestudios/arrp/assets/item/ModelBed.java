package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:bed" special model type.
 */
public class ModelBed extends ModelSpecial {

    private String texture; // Namespaced ID for texture

    public ModelBed() {
        super("minecraft:bed");
    }

    // Getter and Setter
    public String getTexture() {
        return texture;
    }

    public ModelBed texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public ModelBed clone() {
        ModelBed cloned = new ModelBed();
        cloned.base(this.getBase());
        cloned.texture(this.texture);
        return cloned;
    }
}
