package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.Entry;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.util.LootValue;

import java.util.ArrayList;
import java.util.List;

public class SetContentsFunction extends LootFunction {
	public SetContentsFunction(String component, Entry... entries) {
		super("minecraft:set_contents");
		List<Object> list = new ArrayList<>(entries.length);
		for (Entry entry : entries) {
			list.add(LootValue.encode(Entry.CODEC, entry));
		}
		parameter("component", component);
		parameter("entries", list);
	}
}
