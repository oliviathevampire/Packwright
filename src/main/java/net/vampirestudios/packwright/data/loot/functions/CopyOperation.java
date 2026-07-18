package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.util.LootValue;

public record CopyOperation(String source, String target, String op) {
	public static final Codec<CopyOperation> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("source").forGetter(CopyOperation::source),
			Codec.STRING.fieldOf("target").forGetter(CopyOperation::target),
			Codec.STRING.fieldOf("op").forGetter(CopyOperation::op)
	).apply(i, CopyOperation::new));

	public static CopyOperation replace(String source, String target) {
		return new CopyOperation(source, target, "replace");
	}

	public static CopyOperation append(String source, String target) {
		return new CopyOperation(source, target, "append");
	}

	public static CopyOperation merge(String source, String target) {
		return new CopyOperation(source, target, "merge");
	}

	public Object value() {
		return LootValue.encode(CODEC, this);
	}
}
