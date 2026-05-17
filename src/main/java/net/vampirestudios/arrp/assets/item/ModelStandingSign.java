package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:standing_sign" special model type.
 */
public class ModelStandingSign extends ModelSpecial {
    private String woodType; // oak, spruce, birch, etc.
    private String texture;  // Optional texture ID

    public ModelStandingSign() {
        super("minecraft:standing_sign");
    }

    // Getters and Setters
    public String getWoodType() {
        return woodType;
    }

    public ModelStandingSign woodType(String woodType) {
        this.woodType = woodType;
        return this;
    }

    public String getTexture() {
        return texture;
    }

    public ModelStandingSign texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public ModelStandingSign clone() {
        ModelStandingSign cloned = new ModelStandingSign();
        cloned.base(this.getBase());
        cloned.woodType(this.woodType);
        cloned.texture(this.texture);
        return cloned;
    }
}
