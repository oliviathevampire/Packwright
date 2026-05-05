package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:standing_sign" special model type.
 */
public class JModelStandingSign extends JModelSpecial {
    private String woodType; // oak, spruce, birch, etc.
    private String texture;  // Optional texture ID

    public JModelStandingSign() {
        super("minecraft:standing_sign");
    }

    // Getters and Setters
    public String getWoodType() {
        return woodType;
    }

    public JModelStandingSign woodType(String woodType) {
        this.woodType = woodType;
        return this;
    }

    public String getTexture() {
        return texture;
    }

    public JModelStandingSign texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public JModelStandingSign clone() {
        JModelStandingSign cloned = new JModelStandingSign();
        cloned.base(this.getBase());
        cloned.woodType(this.woodType);
        cloned.texture(this.texture);
        return cloned;
    }
}
