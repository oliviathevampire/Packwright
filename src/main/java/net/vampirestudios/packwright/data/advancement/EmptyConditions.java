package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/** conditions for parameterless triggers such as {@code minecraft:tick} and {@code minecraft:impossible} */
public final class EmptyConditions extends CriterionConditions {
	public static final Identifier TICK = Identifier.withDefaultNamespace("tick");
	public static final Identifier IMPOSSIBLE = Identifier.withDefaultNamespace("impossible");

	static {
		CriterionConditions.register(TICK.toString(), mapCodec(TICK).codec());
		CriterionConditions.register(IMPOSSIBLE.toString(), mapCodec(IMPOSSIBLE).codec());
	}

	private static MapCodec<EmptyConditions> mapCodec(Identifier trigger) {
		return MapCodec.unit(() -> new EmptyConditions(trigger));
	}

	private EmptyConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static EmptyConditions tick() { return new EmptyConditions(TICK); }
	public static EmptyConditions impossible() { return new EmptyConditions(IMPOSSIBLE); }
}
