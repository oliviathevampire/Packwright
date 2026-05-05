package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents an entry in a range dispatch model.
 */
public class JRangeEntry {
    public static final Codec<JRangeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("threshold").forGetter(JRangeEntry::getThreshold),
            JItemModel.CODEC.fieldOf("model").forGetter(JRangeEntry::getModel)
    ).apply(instance, JRangeEntry::of));

    private double threshold;
    private JItemModel model;

    public JRangeEntry() {}

    public JRangeEntry(double threshold, JItemModel model) {
        this.threshold = threshold;
        this.model = model;
    }

    public static JRangeEntry of(double threshold, JItemModel model) {
        return new JRangeEntry(threshold, model);
    }

    // Getters and Setters
    public double getThreshold() {
        return threshold;
    }

    public JItemModel getModel() {
        return model;
    }
}
