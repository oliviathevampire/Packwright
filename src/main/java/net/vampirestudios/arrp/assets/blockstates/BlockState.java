package net.vampirestudios.arrp.assets.blockstates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.impl.Codecs;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BlockState {
	private static final Codec<List<Variant>> VARIANTS = Codecs.oneOrList(Variant.CODEC);
	public static final Codec<BlockState> CODEC = RecordCodecBuilder.create(i -> i.group(
			VARIANTS.optionalFieldOf("variants").forGetter(s -> s.variants.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(s.variants))),
			Multipart.CODEC.listOf().optionalFieldOf("multipart").forGetter(s -> s.multiparts.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(s.multiparts)))
	).apply(i, (ov, om) -> {
		if (ov.isPresent() && om.isPresent()) throw new IllegalStateException("variants XOR multipart");
		BlockState s = new BlockState();
		ov.ifPresent(l -> l.forEach(s::add));
		om.ifPresent(l -> l.forEach(s::add));
		return s;
	}));
	private final List<Variant> variants = new ArrayList<>();
	private final List<Multipart> multiparts = new ArrayList<>();

	/**
	 * @see #state()
	 * @see #state(Multipart...)
	 * @see #state(Variant...)
	 */
	public BlockState() {
	}

	public static BlockState state() {
		return new BlockState();
	}

	public static BlockState state(Variant... variants) {
		BlockState state = new BlockState();
		for (Variant variant : variants) {
			state.add(variant);
		}
		return state;
	}

	public static BlockState state(Multipart... parts) {
		BlockState state = new BlockState();
		for (Multipart part : parts) {
			state.add(part);
		}
		return state;
	}

	public static Variant variant() {
		return new Variant();
	}

	public static Variant variant(BlockModel model) {
		Variant variant = new Variant();
		variant.put("", model);
		return variant;
	}

	public static BlockModel model(Identifier id) {
		return new BlockModel(id);
	}

	public static Multipart multipart(BlockModel... models) {
		Multipart multipart = new Multipart();
		for (BlockModel model : models) {
			multipart.addModel(model);
		}
		return multipart;
	}

	public static When when() {
		return new When();
	}

	public static When.StateBuilder whenStateBuilder() {
		return new When.StateBuilder();
	}

	public BlockState add(Variant variant) {
		if (!this.multiparts.isEmpty()) {
			throw new IllegalStateException("BlockStates can only have variants *or* multiparts, not both");
		}
		this.variants.add(variant);
		return this;
	}

	public BlockState add(Multipart multiparts) {
		if (!this.variants.isEmpty()) {
			throw new IllegalStateException("BlockStates can only have variants *or* multiparts, not both");
		}
		this.multiparts.add(multiparts);
		return this;
	}

	@Override
	public BlockState clone() {
		try {
			return (BlockState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
