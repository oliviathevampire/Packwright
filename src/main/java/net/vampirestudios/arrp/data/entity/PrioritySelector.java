package net.vampirestudios.arrp.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class PrioritySelector {
    public static final Codec<PrioritySelector> CODEC = RecordCodecBuilder.create(i -> i.group(
            SpawnCondition.CODEC.optionalFieldOf("condition").forGetter(x -> Optional.ofNullable(x.condition)),
            Codec.INT.fieldOf("priority").forGetter(PrioritySelector::getPriority)
    ).apply(i, (cond, prio) -> {
        PrioritySelector out = new PrioritySelector();
        out.priority = prio;
        cond.ifPresent(out::condition);
        return out;
    }));

    private SpawnCondition condition;
    private int priority;

    public PrioritySelector() {}

    public static PrioritySelector selector() { return new PrioritySelector(); }

    public static PrioritySelector of(SpawnCondition condition, int priority) {
        return selector().condition(condition).priority(priority);
    }

    public static PrioritySelector fallback(int priority) {
        return selector().priority(priority);
    }

    public PrioritySelector condition(SpawnCondition condition) { this.condition = condition; return this; }
    public PrioritySelector priority(int priority) { this.priority = priority; return this; }

    public SpawnCondition getCondition() { return condition; }
    public int getPriority() { return priority; }
}