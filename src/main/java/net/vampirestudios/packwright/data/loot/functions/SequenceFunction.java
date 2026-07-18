package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.util.LootValue;

import java.util.ArrayList;
import java.util.List;

public class SequenceFunction extends LootFunction {
	public SequenceFunction(LootFunction... functions) {
		super("minecraft:sequence");
		List<Object> list = new ArrayList<>(functions.length);
		for (LootFunction function : functions) {
			list.add(LootValue.encode(LootFunction.CODEC, function));
		}
		parameter("functions", list);
	}
}
