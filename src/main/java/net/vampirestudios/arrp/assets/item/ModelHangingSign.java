package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:hanging_sign" special model type.
 */
public class ModelHangingSign extends ModelSpecial {
    private String woodType; // oak, spruce, birch, etc.
    private String texture;  // Optional texture ID

    public ModelHangingSign() {
        super("minecraft:hanging_sign");
    }

    // Getters and Setters
    public String getWoodType() {
        return woodType;
    }

    public ModelHangingSign woodType(String woodType) {
        this.woodType = woodType;
        return this;
    }

    public String getTexture() {
        return texture;
    }

    public ModelHangingSign texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public ModelHangingSign clone() {
        ModelHangingSign cloned = new ModelHangingSign();
        cloned.base(this.getBase());
        cloned.woodType(this.woodType);
        cloned.texture(this.texture);
        return cloned;
    }
}
