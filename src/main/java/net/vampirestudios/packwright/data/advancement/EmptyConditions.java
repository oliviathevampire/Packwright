package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/**
 * conditions for {@code minecraft:impossible} ({@code ImpossibleTrigger}), the one vanilla
 * trigger whose conditions object truly has zero fields — not even an optional "player"
 * predicate (see {@link net.vampirestudios.packwright.data.advancement.PlayerConditions}
 * for the "just a player predicate" triggers, including {@code minecraft:tick} which
 * despite the name is backed by {@code PlayerTrigger} and does have a "player" field)
 */
public final class EmptyConditions extends CriterionConditions {
	public static final Identifier IMPOSSIBLE = Identifier.withDefaultNamespace("impossible");

	static {
		CriterionConditions.register(IMPOSSIBLE.toString(), mapCodec(IMPOSSIBLE).codec());
	}

	private static MapCodec<EmptyConditions> mapCodec(Identifier trigger) {
		return MapCodec.unit(() -> new EmptyConditions(trigger));
	}

	private EmptyConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static EmptyConditions impossible() { return new EmptyConditions(IMPOSSIBLE); }
}
