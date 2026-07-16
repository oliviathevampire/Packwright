package net.vampirestudios.packwright.data.predicate;

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

	/**
	 * requires an exact data component value on the block entity, e.g.
	 * {@code component("minecraft:custom_name", "...")}
	 */
	public BlockPredicate component(String component, Object value) {
		subMap("components").put(component, value);
		return this;
	}

	/**
	 * adds a data-component sub-predicate on the block entity, e.g.
	 * {@code predicate("minecraft:container", Map.of("items", Map.of("contains", List.of())))}
	 */
	public BlockPredicate predicate(String id, Object value) {
		subMap("predicates").put(id, wrapPredicateValue(value));
		return this;
	}
}
