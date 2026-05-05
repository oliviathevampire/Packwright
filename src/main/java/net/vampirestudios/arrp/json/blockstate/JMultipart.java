package net.vampirestudios.arrp.json.blockstate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JMultipart implements Cloneable {

	// ---------- CODEC ----------

	/** Accept either a single JBlockModel or a list of them. */
	private static final Codec<List<JBlockModel>> APPLY_CODEC = Codecs.oneOrList(JBlockModel.CODEC);

	public static final Codec<JMultipart> CODEC =
			RecordCodecBuilder.<JMultipart>create(inst -> inst.group(
					APPLY_CODEC.fieldOf("apply").forGetter(m -> List.copyOf(m.apply)),
					// when is optional in vanilla; omit if null
					JWhen.CODEC.optionalFieldOf("when").forGetter(m -> Optional.ofNullable(m.when))
			).apply(inst, (applyList, optWhen) -> {
				JMultipart m = new JMultipart();
				applyList.forEach(m::addModel);
				optWhen.ifPresent(m::when);
				return m;
			})).validate(m ->
					m.apply.isEmpty()
							? DataResult.error(() -> "JMultipart.apply must contain at least one model")
							: DataResult.success(m)
			);

	// one or list
	private final List<JBlockModel> apply = new ArrayList<>();
	private JWhen when;

	/**
	 * @see JState#multipart(JBlockModel...)
	 */
	public JMultipart() {}

	@Override
	public JMultipart clone() {
		try {
			return (JMultipart) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	public JMultipart when(JWhen when) {
		this.when = when;
		return this;
	}

	/** Sugar: accept a property map and build a single AND-clause. */
	public JMultipart when(Map<String, ?> props) {
		this.when = JWhen.of(props);
		return this;
	}
	
	public JMultipart addModel(JBlockModel model) {
		this.apply.add(model);
		return this;
	}

	public static class Serializer implements JsonSerializer<JMultipart> {
		@Override
		public JsonElement serialize(JMultipart src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject obj = new JsonObject();
			if (src.apply.size() == 1) {
				obj.add("apply", context.serialize(src.apply.getFirst()));
			} else {
				obj.add("apply", context.serialize(src.apply));
			}
			obj.add("when", context.serialize(src.when));
			return obj;
		}
	}
}
