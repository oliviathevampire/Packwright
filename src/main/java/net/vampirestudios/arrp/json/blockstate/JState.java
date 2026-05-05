package net.vampirestudios.arrp.json.blockstate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.codec.Codecs;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JState {
	// ---- Codecs ----
	// Accept either a single JVariant object or a list of them.
	private static final Codec<List<JVariant>> VARIANTS = Codecs.oneOrList(JVariant.CODEC);
	public static final Codec<JState> CODEC = RecordCodecBuilder.create(i -> i.group(
			VARIANTS.optionalFieldOf("variants").forGetter(s -> s.variants.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(s.variants))),
			JMultipart.CODEC.listOf().optionalFieldOf("multipart").forGetter(s -> s.multiparts.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(s.multiparts)))
	).apply(i, (ov, om) -> {
		if (ov.isPresent() && om.isPresent()) throw new IllegalStateException("variants XOR multipart");
		JState s = new JState();
		ov.ifPresent(l -> l.forEach(s::add));
		om.ifPresent(l -> l.forEach(s::add));
		return s;
	}));
	private final List<JVariant> variants = new ArrayList<>();
	private final List<JMultipart> multiparts = new ArrayList<>();

	/**
	 * @see #state()
	 * @see #state(JMultipart...)
	 * @see #state(JVariant...)
	 */
	public JState() {
	}

	public static JState state() {
		return new JState();
	}

	public static JState state(JVariant... variants) {
		JState state = new JState();
		for (JVariant variant : variants) {
			state.add(variant);
		}
		return state;
	}

	public static JState state(JMultipart... parts) {
		JState state = new JState();
		for (JMultipart part : parts) {
			state.add(part);
		}
		return state;
	}

	public static JVariant variant() {
		return new JVariant();
	}

	public static JVariant variant(JBlockModel model) {
		JVariant variant = new JVariant();
		variant.put("", model);
		return variant;
	}

	public static JBlockModel model(Identifier id) {
		return new JBlockModel(id);
	}

	public static JMultipart multipart(JBlockModel... models) {
		JMultipart multipart = new JMultipart();
		for (JBlockModel model : models) {
			multipart.addModel(model);
		}
		return multipart;
	}

	public static JWhen when() {
		return new JWhen();
	}

	public static JWhen.StateBuilder whenStateBuilder() {
		return new JWhen.StateBuilder();
	}

	public JState add(JVariant variant) {
		if (!this.multiparts.isEmpty()) {
			throw new IllegalStateException("BlockStates can only have variants *or* multiparts, not both");
		}
		this.variants.add(variant);
		return this;
	}

	public JState add(JMultipart multiparts) {
		if (!this.variants.isEmpty()) {
			throw new IllegalStateException("BlockStates can only have variants *or* multiparts, not both");
		}
		this.multiparts.add(multiparts);
		return this;
	}

	@Override
	public JState clone() {
		try {
			return (JState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
