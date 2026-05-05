package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:hanging_sign" special model type.
 */
public class JModelHangingSign extends JModelSpecial {
    private String woodType; // oak, spruce, birch, etc.
    private String texture;  // Optional texture ID

    public JModelHangingSign() {
        super("minecraft:hanging_sign");
    }

    // Getters and Setters
    public String getWoodType() {
        return woodType;
    }

    public JModelHangingSign woodType(String woodType) {
        this.woodType = woodType;
        return this;
    }

    public String getTexture() {
        return texture;
    }

    public JModelHangingSign texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public JModelHangingSign clone() {
        JModelHangingSign cloned = new JModelHangingSign();
        cloned.base(this.getBase());
        cloned.woodType(this.woodType);
        cloned.texture(this.texture);
        return cloned;
    }
}
