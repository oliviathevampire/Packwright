package net.vampirestudios.arrp.json.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class JIcon {
	public static final Codec<JIcon> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("item").forGetter(ic -> ic.item),
			Codec.INT.optionalFieldOf("count").forGetter(ic -> Optional.ofNullable(ic.count)),
			DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(ic -> Optional.ofNullable(ic.components))
	).apply(i, (item, count, comps) -> {
		JIcon ic = new JIcon();
		ic.item = item;
		ic.count = count.orElse(null);
		ic.components = comps.orElse(null);
		return ic;
	}));

	public Identifier item;
	public Integer count;                 // optional
	public DataComponentPatch components; // optional (1.20.5+)

	// builder sugar
	public static JIcon of(Identifier itemId) {
		JIcon ic = new JIcon();
		ic.item = itemId;
		return ic;
	}

	public static JIcon of(Item item) {
		JIcon ic = new JIcon();
		ic.item = BuiltInRegistries.ITEM.getKey(item);
		return ic;
	}

	public JIcon count(int c) {
		this.count = c;
		return this;
	}

	public JIcon components(DataComponentPatch p) {
		this.components = p;
		return this;
	}
}
