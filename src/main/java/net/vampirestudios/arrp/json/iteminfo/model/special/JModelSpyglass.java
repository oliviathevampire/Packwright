package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:spyglass" special model type.
 */
public class JModelSpyglass extends JModelSpecial {
    private String scopeTexture;  // Texture for the scope overlay

    protected JModelSpyglass() {
        super("minecraft:spyglass");
    }

    // Static factory method
    public static JModelSpyglass spyglass() {
        return new JModelSpyglass();
    }

    // Fluent methods
    public JModelSpyglass scopeTexture(String scopeTexture) {
        this.scopeTexture = scopeTexture;
        return this;
    }

    // Getter
    public String getScopeTexture() {
        return scopeTexture;
    }

    @Override
    public JModelSpyglass clone() {
        JModelSpyglass cloned = new JModelSpyglass();
        cloned.base(this.getBase());
        cloned.scopeTexture = this.scopeTexture;
        return cloned;
    }
}
