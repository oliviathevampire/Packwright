package net.vampirestudios.packwright.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents an entry in a range dispatch model.
 */
public class RangeEntry {
    public static final Codec<RangeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("threshold").forGetter(RangeEntry::getThreshold),
            ItemModel.CODEC.fieldOf("model").forGetter(RangeEntry::getModel)
    ).apply(instance, RangeEntry::of));

    private double threshold;
    private ItemModel model;

    public RangeEntry() {}

    public RangeEntry(double threshold, ItemModel model) {
        this.threshold = threshold;
        this.model = model;
    }

    public static RangeEntry of(double threshold, ItemModel model) {
        return new RangeEntry(threshold, model);
    }

    // Getters and Setters
    public double getThreshold() {
        return threshold;
    }

    public ItemModel getModel() {
        return model;
    }
}
