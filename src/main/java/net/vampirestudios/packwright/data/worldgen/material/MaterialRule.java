package net.vampirestudios.packwright.data.worldgen.material;

import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;

import java.util.List;

public sealed interface MaterialRule permits BlockMaterialRule, SequenceMaterialRule, ConditionalMaterialRule, BandlandsMaterialRule {
	Codec<MaterialRule> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<MaterialRule, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "block" -> BlockMaterialRule.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "sequence" -> SequenceMaterialRule.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "condition" -> ConditionalMaterialRule.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "bandlands" -> BandlandsMaterialRule.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported material rule type");
			});
		}

		@Override
		public <T> DataResult<T> encode(MaterialRule input, DynamicOps<T> ops, T prefix) {
			if (input instanceof BlockMaterialRule rule) return BlockMaterialRule.CODEC.codec().encode(rule, ops, prefix);
			if (input instanceof SequenceMaterialRule rule) return SequenceMaterialRule.CODEC.codec().encode(rule, ops, prefix);
			if (input instanceof ConditionalMaterialRule rule) return ConditionalMaterialRule.CODEC.codec().encode(rule, ops, prefix);
			if (input instanceof BandlandsMaterialRule rule) return BandlandsMaterialRule.CODEC.codec().encode(rule, ops, prefix);
			return DataResult.error(() -> "Unsupported material rule: " + input.getClass().getSimpleName());
		}
	};

	static BlockMaterialRule block(Identifier block) {
		return new BlockMaterialRule(WorldgenBlockState.blockState(block));
	}

	static SequenceMaterialRule sequence(MaterialRule... rules) {
		return new SequenceMaterialRule(List.of(rules));
	}

	static ConditionalMaterialRule condition(MaterialCondition condition, MaterialRule thenRun) {
		return new ConditionalMaterialRule(condition, thenRun);
	}

	/** places the badlands terracotta color-band pattern */
	static BandlandsMaterialRule bandlands() {
		return new BandlandsMaterialRule();
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
