package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class JPrioritySelector {
    public static final Codec<JPrioritySelector> CODEC = RecordCodecBuilder.create(i -> i.group(
            JSpawnCondition.CODEC.optionalFieldOf("condition").forGetter(x -> Optional.ofNullable(x.condition)),
            Codec.INT.fieldOf("priority").forGetter(JPrioritySelector::getPriority)
    ).apply(i, (cond, prio) -> {
        JPrioritySelector out = new JPrioritySelector();
        out.priority = prio;
        cond.ifPresent(out::condition);
        return out;
    }));

    private JSpawnCondition condition;
    private int priority;

    public JPrioritySelector() {}

    public static JPrioritySelector selector() { return new JPrioritySelector(); }

    public static JPrioritySelector of(JSpawnCondition condition, int priority) {
        return selector().condition(condition).priority(priority);
    }

    public static JPrioritySelector fallback(int priority) {
        return selector().priority(priority);
    }

    public JPrioritySelector condition(JSpawnCondition condition) { this.condition = condition; return this; }
    public JPrioritySelector priority(int priority) { this.priority = priority; return this; }

    public JSpawnCondition getCondition() { return condition; }
    public int getPriority() { return priority; }
}