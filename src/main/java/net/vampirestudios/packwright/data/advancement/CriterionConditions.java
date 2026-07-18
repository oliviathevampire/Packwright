package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for a criterion's "conditions", one subtype per trigger id. Serialized as
 * the nested "conditions" object of a {@link Criterion}, dispatched by the criterion's
 * "trigger" field (mirrors {@link net.vampirestudios.packwright.data.entity.SpawnCondition}).
 */
public abstract class CriterionConditions {
	private final String trigger;

	protected CriterionConditions(String trigger) {
		this.trigger = trigger;
	}

	public final String getTrigger() {
		return trigger;
	}

	private static final Map<String, Codec<? extends CriterionConditions>> REGISTRY = new ConcurrentHashMap<>();

	public static void register(String trigger, Codec<? extends CriterionConditions> codec) {
		REGISTRY.put(trigger, codec);
	}

	@SuppressWarnings("unchecked")
	static Codec<CriterionConditions> codecFor(String trigger) {
		return (Codec<CriterionConditions>) REGISTRY.get(trigger);
	}
}
