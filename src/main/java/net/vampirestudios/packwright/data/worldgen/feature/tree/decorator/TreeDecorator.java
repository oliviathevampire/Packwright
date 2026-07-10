package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

import java.util.List;

public sealed interface TreeDecorator permits
		TrunkVineTreeDecorator,
		LeaveVineTreeDecorator,
		CocoaTreeDecorator,
		BeehiveTreeDecorator,
		AlterGroundTreeDecorator,
		AttachedToLeavesTreeDecorator,
		PlaceOnGroundTreeDecorator,
		ShelfMushroomTreeDecorator,
		PaleMossTreeDecorator,
		CreakingHeartTreeDecorator,
		AttachedToLogsTreeDecorator {
	Codec<TreeDecorator> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<TreeDecorator, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "trunk_vine" -> TrunkVineTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "leave_vine" -> LeaveVineTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "cocoa" -> CocoaTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "beehive" -> BeehiveTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "alter_ground" -> AlterGroundTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "attached_to_leaves" -> AttachedToLeavesTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "place_on_ground" -> PlaceOnGroundTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "shelf_mushroom" -> ShelfMushroomTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "pale_moss" -> PaleMossTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "creaking_heart" -> CreakingHeartTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "attached_to_logs" -> AttachedToLogsTreeDecorator.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported tree decorator type");
			});
		}

		@Override
		public <T> DataResult<T> encode(TreeDecorator input, DynamicOps<T> ops, T prefix) {
			if (input instanceof TrunkVineTreeDecorator decorator) return TrunkVineTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof LeaveVineTreeDecorator decorator) return LeaveVineTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof CocoaTreeDecorator decorator) return CocoaTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof BeehiveTreeDecorator decorator) return BeehiveTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof AlterGroundTreeDecorator decorator) return AlterGroundTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof AttachedToLeavesTreeDecorator decorator) return AttachedToLeavesTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof PlaceOnGroundTreeDecorator decorator) return PlaceOnGroundTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof ShelfMushroomTreeDecorator decorator) return ShelfMushroomTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof PaleMossTreeDecorator decorator) return PaleMossTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof CreakingHeartTreeDecorator decorator) return CreakingHeartTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			if (input instanceof AttachedToLogsTreeDecorator decorator) return AttachedToLogsTreeDecorator.CODEC.codec().encode(decorator, ops, prefix);
			return DataResult.error(() -> "Unsupported tree decorator: " + input.getClass().getSimpleName());
		}
	};

	static TrunkVineTreeDecorator trunkVine() {
		return new TrunkVineTreeDecorator();
	}

	static LeaveVineTreeDecorator leaveVine(float probability) {
		return new LeaveVineTreeDecorator(probability);
	}

	static CocoaTreeDecorator cocoa(float probability) {
		return new CocoaTreeDecorator(probability);
	}

	static BeehiveTreeDecorator beehive(float probability) {
		return new BeehiveTreeDecorator(probability);
	}

	static AlterGroundTreeDecorator alterGround(BlockStateProvider provider) {
		return new AlterGroundTreeDecorator(provider);
	}

	static AttachedToLeavesTreeDecorator attachedToLeaves(float probability, int exclusionRadiusXz, int exclusionRadiusY, BlockStateProvider blockProvider, int requiredEmptyBlocks, String... directions) {
		return new AttachedToLeavesTreeDecorator(probability, exclusionRadiusXz, exclusionRadiusY, blockProvider, requiredEmptyBlocks, List.of(directions));
	}

	static AttachedToLeavesTreeDecorator attachedToLeaves(float probability, int exclusionRadiusXz, int exclusionRadiusY, int requiredEmptyBlocks, Identifier... blocks) {
		return attachedToLeaves(probability, exclusionRadiusXz, exclusionRadiusY, BlockStateProvider.weighted(
				java.util.Arrays.stream(blocks)
						.map(block -> BlockStateProvider.weightedEntry(block, 1))
						.toArray(BlockStateProvider.WeightedEntry[]::new)
		), requiredEmptyBlocks, "down");
	}

	static AttachedToLogsTreeDecorator attachedToLogs(float probability, BlockStateProvider provider, String... directions) {
		return new AttachedToLogsTreeDecorator(probability, provider, List.of(directions));
	}

	static PlaceOnGroundTreeDecorator placeOnGround(int tries, int radius, int height, BlockStateProvider provider) {
		return new PlaceOnGroundTreeDecorator(tries, radius, height, provider);
	}

	static ShelfMushroomTreeDecorator shelfMushroom(float probability) {
		return new ShelfMushroomTreeDecorator(probability);
	}

	static PaleMossTreeDecorator paleMoss(float groundChance, float leavesChance, float trunkChance) {
		return new PaleMossTreeDecorator(groundChance, leavesChance, trunkChance);
	}

	static CreakingHeartTreeDecorator creakingHeart(float probability) {
		return new CreakingHeartTreeDecorator(probability);
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
