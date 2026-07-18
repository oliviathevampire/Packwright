package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class SetWrittenBookPagesFunction extends LootFunction {
	public SetWrittenBookPagesFunction(ListOperation mode, String... pages) {
		super("minecraft:set_written_book_pages");
		mode(mode);
		parameter("pages", List.of(pages));
	}
}
