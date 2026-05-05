package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:shulker_box" special model type.
 */
public class JModelShulkerBox extends JModelSpecial {
    private String color;    // Color of the shulker box
    private boolean open;    // Whether the shulker box is open

    protected JModelShulkerBox() {
        super("minecraft:shulker_box");
    }

    // Static factory method
    public static JModelShulkerBox shulkerBox() {
        return new JModelShulkerBox();
    }

    // Fluent methods
    public JModelShulkerBox color(String color) {
        this.color = color;
        return this;
    }

    public JModelShulkerBox open(boolean open) {
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
    public JModelShulkerBox clone() {
        JModelShulkerBox cloned = new JModelShulkerBox();
        cloned.base(this.getBase());
        cloned.color = this.color;
        cloned.open = this.open;
        return cloned;
    }
}
