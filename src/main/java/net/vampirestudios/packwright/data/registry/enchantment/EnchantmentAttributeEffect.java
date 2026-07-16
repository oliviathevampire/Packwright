package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

/**
 * an attribute modifier applied while the item is equipped in a matching slot, used by the
 * {@code attributes} effect component. Entries here are bare objects (no {@code type} key) —
 * {@code attributes} decodes a plain list of these, unlike the other dispatched effect types.
 */
public record EnchantmentAttributeEffect(Identifier id, Identifier attribute, LevelBasedValue amount, Operation operation) {
	public static final Codec<EnchantmentAttributeEffect> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(EnchantmentAttributeEffect::id),
			Identifier.CODEC.fieldOf("attribute").forGetter(EnchantmentAttributeEffect::attribute),
			LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentAttributeEffect::amount),
			Operation.CODEC.fieldOf("operation").forGetter(EnchantmentAttributeEffect::operation)
	).apply(i, EnchantmentAttributeEffect::new));

	public enum Operation implements StringRepresentable {
		ADD_VALUE("add_value"),
		ADD_MULTIPLIED_BASE("add_multiplied_base"),
		ADD_MULTIPLIED_TOTAL("add_multiplied_total");

		public static final Codec<Operation> CODEC = StringRepresentable.fromEnum(Operation::values);
		private final String name;
		Operation(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}
}
