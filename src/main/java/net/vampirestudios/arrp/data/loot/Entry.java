package net.vampirestudios.arrp.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class Entry {
	public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("type").forGetter(e -> java.util.Optional.ofNullable(e.type)),
			Codec.STRING.optionalFieldOf("name").forGetter(e -> java.util.Optional.ofNullable(e.name)),
			Codec.BOOL.optionalFieldOf("expand").forGetter(e -> java.util.Optional.ofNullable(e.expand)),
			LootFunction.CODEC.listOf().optionalFieldOf("functions").forGetter(e -> java.util.Optional.ofNullable(e.functions)),
			Condition.CODEC.listOf().optionalFieldOf("conditions").forGetter(e -> java.util.Optional.ofNullable(e.conditions)),
			Codec.INT.optionalFieldOf("weight").forGetter(e -> java.util.Optional.ofNullable(e.weight)),
			Codec.INT.optionalFieldOf("quality").forGetter(e -> java.util.Optional.ofNullable(e.quality)),
			// recursive: children
			Codec.lazyInitialized(() -> Entry.CODEC).listOf().optionalFieldOf("children")
					.forGetter(e -> java.util.Optional.ofNullable(e.children))
	).apply(i, (otype, oname, oexp, ofunc, ocond, oweight, oqual, och) -> {
		Entry e = new Entry();
		e.type = otype.orElse(null);
		e.name = oname.orElse(null);
		e.expand = oexp.orElse(null);
		e.functions = ofunc.orElse(null);
		e.conditions = ocond.orElse(null);
		e.weight = oweight.orElse(null);
		e.quality = oqual.orElse(null);
		e.children = och.orElse(null);
		return e;
	}));
	private String type;
	private String name;
	private List<Entry> children;
	private Boolean expand;
	private List<LootFunction> functions;
	private List<Condition> conditions;
	private Integer weight;
	private Integer quality;

	/**
	 * @see LootTable#entry()
	 */
	public Entry() {}

	public Entry type(String type) {
		this.type = type;
		return this;
	}

	public Entry name(String name) {
		this.name = name;
		return this;
	}

	public Entry child(Entry child) {
		if (this == child) {
			throw new IllegalArgumentException("Can't add entry as its own child!");
		}
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		this.children.add(child);
		return this;
	}

	public Entry expand(Boolean expand) {
		this.expand = expand;
		return this;
	}

	public Entry function(LootFunction function) {
		if (this.functions == null) {
			this.functions = new ArrayList<>();
		}
		this.functions.add(function);
		return this;
	}

	public Entry function(String function) {
	    return function(LootTable.function(function));
    }

	public Entry condition(Condition condition) {
		if(this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		this.conditions.add(condition);
		return this;
	}

	public Entry condition(String condition) {
		return condition(LootTable.predicate(condition));
	}

	public Entry weight(Integer weight) {
		this.weight = weight;
		return this;
	}

	public Entry quality(Integer quality) {
		this.quality = quality;
		return this;
	}
}
