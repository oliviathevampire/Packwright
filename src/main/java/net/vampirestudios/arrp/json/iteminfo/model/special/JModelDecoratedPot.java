package net.vampirestudios.arrp.json.iteminfo.model.special;

/**
 * Represents the "minecraft:decorated_pot" special model type.
 */
public class JModelDecoratedPot extends JModelSpecial {
    private String[] shards = new String[4]; // Array of shard textures

    protected JModelDecoratedPot() {
        super("minecraft:decorated_pot");
    }

    // Static factory method
    public static JModelDecoratedPot decoratedPot() {
        return new JModelDecoratedPot();
    }

    // Fluent methods
    public JModelDecoratedPot shard(int index, String texture) {
        if (index >= 0 && index < 4) {
            this.shards[index] = texture;
        }
        return this;
    }

    // Getter
    public String[] getShards() {
        return shards;
    }

    @Override
    public JModelDecoratedPot clone() {
        JModelDecoratedPot cloned = new JModelDecoratedPot();
        cloned.base(this.getBase());
        cloned.shards = this.shards.clone();
        return cloned;
    }
}
