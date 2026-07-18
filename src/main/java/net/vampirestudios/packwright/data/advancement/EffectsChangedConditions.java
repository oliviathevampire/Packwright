package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * conditions for {@code minecraft:effects_changed}. Vanilla's "effects" field is a
 * {@code MobEffectsPredicate} (a map of effect id to amplifier/duration/visible checks,
 * structurally the same shape as {@link EntityPredicate#effect}'s checks map); this project
 * has no dedicated builder for it yet, so it's simplified to a raw effect-id -> checks map
 * passed straight through via {@code JavaOps}, matching the pattern used by
 * {@code PredicateBuilder.codecOf}.
 */
public final class EffectsChangedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("effects_changed");

	private static final Codec<Map<String, Object>> EFFECTS_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
		Object value = dynamic.convert(JavaOps.INSTANCE).getValue();
		if (!(value instanceof Map<?, ?> map)) return DataResult.error(() -> "effects must be an object");
		Map<String, Object> result = new LinkedHashMap<>();
		map.forEach((k, v) -> result.put(String.valueOf(k), v));
		return DataResult.success(result);
	}, map -> new Dynamic<>(JavaOps.INSTANCE, map));

	public static final MapCodec<EffectsChangedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			EFFECTS_CODEC.optionalFieldOf("effects").forGetter(x -> x.effects.isEmpty() ? Optional.empty() : Optional.of(Map.copyOf(x.effects))),
			AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("source").forGetter(x -> Optional.ofNullable(x.source))
	).apply(i, (player, effects, source) -> {
		EffectsChangedConditions out = new EffectsChangedConditions();
		out.player = player.orElse(null);
		effects.ifPresent(out.effects::putAll);
		out.source = source.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private final Map<String, Object> effects = new LinkedHashMap<>();
	private EntityPredicate source;

	public EffectsChangedConditions() {
		super(TYPE.toString());
	}

	public static EffectsChangedConditions gotEffectsFrom(EntityPredicate source) {
		EffectsChangedConditions out = new EffectsChangedConditions();
		out.source = source;
		return out;
	}

	/**
	 * requires the given status effect with extra checks, e.g. {@code Map.of("amplifier", Map.of("min", 1))}
	 */
	public EffectsChangedConditions effect(String effect, Map<String, ?> checks) {
		this.effects.put(effect, checks);
		return this;
	}

	public EffectsChangedConditions player(Condition player) { this.player = player; return this; }

	public EffectsChangedConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public Map<String, Object> getEffects() { return Map.copyOf(effects); }
	public EntityPredicate getSource() { return source; }
}
