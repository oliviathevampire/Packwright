package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:chest" special model type.
 */
public class JModelChest extends JModelSpecial {

    private String texture;  // Namespaced ID for texture
    private float openness;  // 0.0 (closed) to 1.0 (open)

    public JModelChest() {
        super("minecraft:chest");
    }

    // Getters and Setters
    public String getTexture() {
        return texture;
    }

    public JModelChest texture(String texture) {
        this.texture = texture;
        return this;
    }

    public float getOpenness() {
        return openness;
    }

    public JModelChest openness(float openness) {
        this.openness = openness;
        return this;
    }

    @Override
    public JModelChest clone() {
        JModelChest cloned = new JModelChest();
        cloned.base(this.getBase());
        cloned.texture(this.texture);
        cloned.openness(this.openness);
        return cloned;
    }
}
