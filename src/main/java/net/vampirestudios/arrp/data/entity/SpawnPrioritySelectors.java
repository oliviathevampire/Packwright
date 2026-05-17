package net.vampirestudios.arrp.data.entity;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public class SpawnPrioritySelectors {
    public static final SpawnPrioritySelectors EMPTY = new SpawnPrioritySelectors(List.of());

    public static final Codec<SpawnPrioritySelectors> CODEC =
            PrioritySelector.CODEC.listOf()
                    .xmap(SpawnPrioritySelectors::new, SpawnPrioritySelectors::getSelectors);

    private final List<PrioritySelector> selectors;

    public SpawnPrioritySelectors(List<PrioritySelector> selectors) {
        this.selectors = selectors;
    }

    public static SpawnPrioritySelectors selectors() {
        return new SpawnPrioritySelectors(new ArrayList<>());
    }

    public static SpawnPrioritySelectors single(SpawnCondition cond, int priority) {
        return selectors().add(cond, priority);
    }

    public static SpawnPrioritySelectors fallback(int priority) {
        return selectors().fallbackSelector(priority);
    }

    public SpawnPrioritySelectors add(SpawnCondition condition, int priority) {
        if (selectors instanceof ArrayList<PrioritySelector> list) {
            list.add(PrioritySelector.of(condition, priority));
            return this;
        }
        throw new UnsupportedOperationException("This SpawnPrioritySelectors is immutable");
    }

    public SpawnPrioritySelectors fallbackSelector(int priority) {
        if (selectors instanceof ArrayList<PrioritySelector> list) {
            list.add(PrioritySelector.fallback(priority));
            return this;
        }
        throw new UnsupportedOperationException("This SpawnPrioritySelectors is immutable");
    }

    public SpawnPrioritySelectors freeze() {
        return selectors.isEmpty() ? EMPTY : new SpawnPrioritySelectors(List.copyOf(selectors));
    }

    public List<PrioritySelector> getSelectors() {
        return selectors;
    }
}
