package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class Icon {
	public static final Codec<Icon> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("item").forGetter(ic -> ic.item),
			Codec.INT.optionalFieldOf("count").forGetter(ic -> Optional.ofNullable(ic.count)),
			DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(ic -> Optional.ofNullable(ic.components))
	).apply(i, (item, count, comps) -> {
		Icon ic = new Icon();
		ic.item = item;
		ic.count = count.orElse(null);
		ic.components = comps.orElse(null);
		return ic;
	}));

	private Identifier item;
	private Integer count;
	private DataComponentPatch components;

	public static Icon of(Identifier itemId) {
		Icon ic = new Icon();
		ic.item = itemId;
		return ic;
	}

	public static Icon of(Item item) {
		Icon ic = new Icon();
		ic.item = BuiltInRegistries.ITEM.getKey(item);
		return ic;
	}

	public Icon count(int c) { this.count = c; return this; }
	public Icon components(DataComponentPatch p) { this.components = p; return this; }

	public Identifier getItem() { return item; }
	public Integer getCount() { return count; }
	public DataComponentPatch getComponents() { return components; }
}
