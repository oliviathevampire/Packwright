package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.loot.util.LootValue;

import java.util.List;

public record LootAttributeModifier(Identifier id, Identifier attribute, String operation, NumberProvider amount, List<String> slots) {
	public static final Codec<LootAttributeModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(LootAttributeModifier::id),
			Identifier.CODEC.fieldOf("attribute").forGetter(LootAttributeModifier::attribute),
			Codec.STRING.fieldOf("operation").forGetter(LootAttributeModifier::operation),
			NumberProvider.CODEC.fieldOf("amount").forGetter(LootAttributeModifier::amount),
			Codec.STRING.listOf().fieldOf("slot").forGetter(LootAttributeModifier::slots)
	).apply(i, LootAttributeModifier::new));

	public LootAttributeModifier(Identifier id, Identifier attribute, String operation, NumberProvider amount, String... slots) {
		this(id, attribute, operation, amount, List.of(slots));
	}

	public Object value() {
		return LootValue.encode(CODEC, this);
	}
}
