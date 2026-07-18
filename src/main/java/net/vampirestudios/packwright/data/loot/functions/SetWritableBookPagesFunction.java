package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class SetWritableBookPagesFunction extends LootFunction {
	public SetWritableBookPagesFunction(ListOperation mode, String... pages) {
		super("minecraft:set_writable_book_pages");
		mode(mode);
		parameter("pages", List.of(pages));
	}
}
