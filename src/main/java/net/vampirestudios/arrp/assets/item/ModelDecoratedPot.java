package net.vampirestudios.arrp.assets.item;

/**
 * Represents the "minecraft:decorated_pot" special model type.
 */
public class ModelDecoratedPot extends ModelSpecial {
    private String[] shards = new String[4]; // Array of shard textures

    protected ModelDecoratedPot() {
        super("minecraft:decorated_pot");
    }

    // Static factory method
    public static ModelDecoratedPot decoratedPot() {
        return new ModelDecoratedPot();
    }

    // Fluent methods
    public ModelDecoratedPot shard(int index, String texture) {
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
    public ModelDecoratedPot clone() {
        ModelDecoratedPot cloned = new ModelDecoratedPot();
        cloned.base(this.getBase());
        cloned.shards = this.shards.clone();
        return cloned;
    }
}
