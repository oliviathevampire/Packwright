package net.vampirestudios.arrp.data.predicate;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A block predicate, as used inside {@link LocationPredicate}.
 */
public class BlockPredicate extends PredicateBuilder<BlockPredicate> {

	public static BlockPredicate of() {
		return new BlockPredicate();
	}

	/**
	 * matches any of the given block ids or {@code #tag}s
	 */
	public BlockPredicate blocks(String... blocksOrTag) {
		return parameter("blocks", List.of(blocksOrTag));
	}

	public BlockPredicate blocks(Identifier... blocks) {
		List<String> list = new ArrayList<>(blocks.length);
		for (Identifier block : blocks) {
			list.add(block.toString());
		}
		return parameter("blocks", list);
	}

	public BlockPredicate state(String property, String value) {
		subMap("state").put(property, value);
		return this;
	}

	public BlockPredicate state(String property, boolean value) {
		subMap("state").put(property, value);
		return this;
	}

	public BlockPredicate state(String property, int value) {
		subMap("state").put(property, value);
		return this;
	}

	public BlockPredicate nbt(String nbt) {
		return parameter("nbt", nbt);
	}
}
