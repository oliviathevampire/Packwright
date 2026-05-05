package net.vampirestudios.arrp.json.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class JResult {
	public static final Codec<JResult> OBJECT_CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("item").forGetter(r -> r.item),
			// count is OPTIONAL; only present for stacks
			Codec.INT.optionalFieldOf("count").forGetter(r ->
					(r instanceof JStackedResult s) ? Optional.ofNullable(s.getCount()) : Optional.empty()
			),
			// ✅ components OPTIONAL — don't encode when null
			DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(r -> Optional.ofNullable(r.components))
	).apply(i, (id, oc, comps) -> {
		JResult res = oc.isPresent() ? JResult.stackedResult(id, oc.get()) : JResult.result(id);
		comps.ifPresent(res::components);
		return res;
	}));

	public static final Codec<JResult> CODEC = Codec.either(Identifier.CODEC, OBJECT_CODEC).xmap(
			// decode
			e -> e.map(JResult::result, x -> x),
			// encode: compact as string when possible
			r -> {
				boolean hasCount = (r instanceof JStackedResult s) && s.getCount() != null && s.getCount() != 1;
				boolean hasComps = r.components != null && !r.components.isEmpty(); // empty() is fine
				return (!hasCount && !hasComps) ? Either.left(r.item) : Either.right(r);
			}
	);

	protected final Identifier item;
	protected DataComponentPatch components; // NEW

	JResult(final Identifier id) {
		this.item = id;
	}

	public JResult(Identifier item, DataComponentPatch components) {
		this.item = item;
		this.components = components;
	}

	public static JResult item(final Item item) {
		return result(BuiltInRegistries.ITEM.getKey(item));
	}

	public static JResult result(final Identifier id) {
		return new JResult(id);
	}

	public static JStackedResult itemStack(final Item item, final int count) {
		return stackedResult(BuiltInRegistries.ITEM.getKey(item), count);
	}

	public static JStackedResult stackedResult(final Identifier id, final int count) {
		final JStackedResult stackedResult = new JStackedResult(id);

		stackedResult.count = count;

		return stackedResult;
	}

	/** Set full component changes. */
	public JResult components(DataComponentPatch changes) {
		this.components = changes;
		return this;
	}

	/** Builder-style convenience. */
	public JResult components(java.util.function.Consumer<DataComponentPatch.Builder> build) {
		DataComponentPatch.Builder b = DataComponentPatch.builder();
		build.accept(b);
		this.components = b.build();
		return this;
	}

	public Identifier getItem() {
		return item;
	}

	public DataComponentPatch getComponents() {
		return components;
	}

	@Override
	protected JResult clone() {
		try {
			return (JResult) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
