package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:shulker_box" special model type.
 */
public class ModelShulkerBox extends ModelSpecial {
    private String color;    // Color of the shulker box
    private boolean open;    // Whether the shulker box is open

    protected ModelShulkerBox() {
        super("minecraft:shulker_box");
    }

    // Static factory method
    public static ModelShulkerBox shulkerBox() {
        return new ModelShulkerBox();
    }

    // Fluent methods
    public ModelShulkerBox color(String color) {
        this.color = color;
        return this;
    }

    public ModelShulkerBox open(boolean open) {
        this.open = open;
        return this;
    }

    // Getters
    public String getColor() {
        return color;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public ModelShulkerBox clone() {
        ModelShulkerBox cloned = new ModelShulkerBox();
        cloned.base(this.getBase());
        cloned.color = this.color;
        cloned.open = this.open;
        return cloned;
    }
}
