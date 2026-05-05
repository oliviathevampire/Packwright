package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:bed" special model type.
 */
public class JModelBed extends JModelSpecial {

    private String texture; // Namespaced ID for texture

    public JModelBed() {
        super("minecraft:bed");
    }

    // Getter and Setter
    public String getTexture() {
        return texture;
    }

    public JModelBed texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public JModelBed clone() {
        JModelBed cloned = new JModelBed();
        cloned.base(this.getBase());
        cloned.texture(this.texture);
        return cloned;
    }
}
