package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:banner" special model type.
 */
public class JModelBanner extends JModelSpecial {

    private String color; // One of 16 predefined colors

    public JModelBanner() {
        super("minecraft:banner");
    }

    // Getter and Setter
    public String getColor() {
        return color;
    }

    public JModelBanner color(String color) {
        this.color = color;
        return this;
    }

    @Override
    public JModelBanner clone() {
        JModelBanner cloned = new JModelBanner();
        cloned.base(this.getBase());
        cloned.color(this.color);
        return cloned;
    }
}
