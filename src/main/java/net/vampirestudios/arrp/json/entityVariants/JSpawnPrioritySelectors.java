package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public class JSpawnPrioritySelectors {
    public static final JSpawnPrioritySelectors EMPTY = new JSpawnPrioritySelectors(List.of());

    public static final Codec<JSpawnPrioritySelectors> CODEC =
            JPrioritySelector.CODEC.listOf()
                    .xmap(JSpawnPrioritySelectors::new, JSpawnPrioritySelectors::getSelectors);

    private final List<JPrioritySelector> selectors;

    public JSpawnPrioritySelectors(List<JPrioritySelector> selectors) {
        this.selectors = selectors;
    }

    public static JSpawnPrioritySelectors selectors() {
        return new JSpawnPrioritySelectors(new ArrayList<>());
    }

    public static JSpawnPrioritySelectors single(JSpawnCondition cond, int priority) {
        return selectors().add(cond, priority);
    }

    public static JSpawnPrioritySelectors fallback(int priority) {
        return selectors().fallbackSelector(priority);
    }

    public JSpawnPrioritySelectors add(JSpawnCondition condition, int priority) {
        if (selectors instanceof ArrayList<JPrioritySelector> list) {
            list.add(JPrioritySelector.of(condition, priority));
            return this;
        }
        throw new UnsupportedOperationException("This JSpawnPrioritySelectors is immutable");
    }

    public JSpawnPrioritySelectors fallbackSelector(int priority) {
        if (selectors instanceof ArrayList<JPrioritySelector> list) {
            list.add(JPrioritySelector.fallback(priority));
            return this;
        }
        throw new UnsupportedOperationException("This JSpawnPrioritySelectors is immutable");
    }

    public JSpawnPrioritySelectors freeze() {
        return selectors.isEmpty() ? EMPTY : new JSpawnPrioritySelectors(List.copyOf(selectors));
    }

    public List<JPrioritySelector> getSelectors() {
        return selectors;
    }
}
