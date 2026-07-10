package net.vampirestudios.packwright.assets.blockstates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.impl.Codecs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Multipart {

	// ---------- CODEC ----------

	/** Accept either a single SimpleModel or a list of them. */
	private static final Codec<List<SimpleModel>> APPLY_CODEC = Codecs.oneOrList(SimpleModel.CODEC);

	public static final Codec<Multipart> CODEC =
			RecordCodecBuilder.<Multipart>create(inst -> inst.group(
					APPLY_CODEC.fieldOf("apply").forGetter(m -> List.copyOf(m.apply)),
					// when is optional in vanilla; omit if null
					When.CODEC.optionalFieldOf("when").forGetter(m -> Optional.ofNullable(m.when))
			).apply(inst, (applyList, optWhen) -> {
				Multipart m = new Multipart();
				applyList.forEach(m::addModel);
				optWhen.ifPresent(m::when);
				return m;
			})).validate(m ->
					m.apply.isEmpty()
							? DataResult.error(() -> "Multipart.apply must contain at least one model")
							: DataResult.success(m)
			);

	// one or list
	private final List<SimpleModel> apply = new ArrayList<>();
	private When when;

	public static Multipart multipart() {
		return new Multipart();
	}

	/**
	 * @see BlockState#multipart(SimpleModel...)
	 */
	public Multipart() {}

	public Multipart when(When when) {
		this.when = when;
		return this;
	}

	/** Sugar: accept a property map and build a single AND-clause. */
	public Multipart when(Map<String, ?> props) {
		this.when = When.of(props);
		return this;
	}
	
	public Multipart addModel(SimpleModel model) {
		this.apply.add(model);
		return this;
	}

	public static class Serializer implements JsonSerializer<Multipart> {
		@Override
		public JsonElement serialize(Multipart src, Type typeOfSrc, JsonSerializationContext context) {
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
