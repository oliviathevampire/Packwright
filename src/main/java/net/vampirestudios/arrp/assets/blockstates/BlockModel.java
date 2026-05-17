package net.vampirestudios.arrp.assets.blockstates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class BlockModel implements Cloneable {
	private final Identifier model;
	private Integer x;
	private Integer y;
	private Integer z;
	private Boolean uvlock;
	private Integer weight;

	// ---- Codecs ----
	public static final Codec<BlockModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			Identifier.CODEC.fieldOf("model").forGetter(m -> m.model),
			Codec.INT.optionalFieldOf("x").forGetter(m -> java.util.Optional.ofNullable(m.x)),
			Codec.INT.optionalFieldOf("y").forGetter(m -> java.util.Optional.ofNullable(m.y)),
			Codec.INT.optionalFieldOf("z").forGetter(m -> java.util.Optional.ofNullable(m.z)),
			Codec.BOOL.optionalFieldOf("uvlock").forGetter(m -> java.util.Optional.ofNullable(m.uvlock)),
			Codec.INT.optionalFieldOf("weight").forGetter(m -> java.util.Optional.ofNullable(m.weight))
	).apply(inst, (modelId, x, y, z, uv, w) -> {
		BlockModel m = new BlockModel(modelId);
		x.ifPresent(m::x);
		y.ifPresent(m::y);
		z.ifPresent(m::z);
		uv.ifPresent(u -> { if (u) m.uvlock(); });
		w.ifPresent(m::weight);
		return m;
	}));

	public static BlockModel blockModel(Identifier model) {
		return new BlockModel(model);
	}

	public BlockModel(Identifier model) {
		this.model = model;
	}

	@Override
	public BlockModel clone() {
		try {
			return (BlockModel) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	public BlockModel x(int x) {
		this.x = x;
		return this;
	}

	public BlockModel y(int y) {
		this.y = y;
		return this;
	}

	public BlockModel z(int z) {
		this.z = z;
		return this;
	}

	public BlockModel uvlock() {
		this.uvlock = true;
		return this;
	}

	public BlockModel weight(int weight) {
		this.weight = weight;
		return this;
	}

}
