package net.vampirestudios.arrp.data.worldgen.feature.tree.root;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;

import java.util.Optional;

public sealed interface TreeRootPlacer permits MangroveRootPlacer {
	Codec<TreeRootPlacer> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<TreeRootPlacer, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "mangrove_root_placer" -> MangroveRootPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported tree root placer type");
			});
		}

		@Override
		public <T> DataResult<T> encode(TreeRootPlacer input, DynamicOps<T> ops, T prefix) {
			if (input instanceof MangroveRootPlacer placer) return MangroveRootPlacer.CODEC.codec().encode(placer, ops, prefix);
			return DataResult.error(() -> "Unsupported tree root placer: " + input.getClass().getSimpleName());
		}
	};

	static MangroveRootPlacer mangrove(BlockStateProvider rootProvider, BlockStateProvider muddyRootsProvider) {
		return new MangroveRootPlacer(Optional.empty(), rootProvider, Optional.empty(), Optional.empty(), Optional.of(muddyRootsProvider));
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
