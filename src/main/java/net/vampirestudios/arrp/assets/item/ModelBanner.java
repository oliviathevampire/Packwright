package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:banner" special model type.
 */
public class ModelBanner extends ModelSpecial {

    private String color; // One of 16 predefined colors

    public ModelBanner() {
        super("minecraft:banner");
    }

    // Getter and Setter
    public String getColor() {
        return color;
    }

    public ModelBanner color(String color) {
        this.color = color;
        return this;
    }

    @Override
    public ModelBanner clone() {
        ModelBanner cloned = new ModelBanner();
        cloned.base(this.getBase());
        cloned.color(this.color);
        return cloned;
    }
}
