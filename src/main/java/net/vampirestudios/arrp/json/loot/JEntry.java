package net.vampirestudios.arrp.json.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class JEntry {
	public static final Codec<JEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("type").forGetter(e -> java.util.Optional.ofNullable(e.type)),
			Codec.STRING.optionalFieldOf("name").forGetter(e -> java.util.Optional.ofNullable(e.name)),
			Codec.BOOL.optionalFieldOf("expand").forGetter(e -> java.util.Optional.ofNullable(e.expand)),
			JFunction.CODEC.listOf().optionalFieldOf("functions").forGetter(e -> java.util.Optional.ofNullable(e.functions)),
			JCondition.CODEC.listOf().optionalFieldOf("conditions").forGetter(e -> java.util.Optional.ofNullable(e.conditions)),
			Codec.INT.optionalFieldOf("weight").forGetter(e -> java.util.Optional.ofNullable(e.weight)),
			Codec.INT.optionalFieldOf("quality").forGetter(e -> java.util.Optional.ofNullable(e.quality)),
			// recursive: children
			Codec.lazyInitialized(() -> JEntry.CODEC).listOf().optionalFieldOf("children")
					.forGetter(e -> java.util.Optional.ofNullable(e.children))
	).apply(i, (otype, oname, oexp, ofunc, ocond, oweight, oqual, och) -> {
		JEntry e = new JEntry();
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
	private List<JEntry> children;
	private Boolean expand;
	private List<JFunction> functions;
	private List<JCondition> conditions;
	private Integer weight;
	private Integer quality;

	/**
	 * @see JLootTable#entry()
	 */
	public JEntry() {}

	public JEntry type(String type) {
		this.type = type;
		return this;
	}

	public JEntry name(String name) {
		this.name = name;
		return this;
	}

	public JEntry child(JEntry child) {
		if (this == child) {
			throw new IllegalArgumentException("Can't add entry as its own child!");
		}
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		this.children.add(child);
		return this;
	}

	public JEntry expand(Boolean expand) {
		this.expand = expand;
		return this;
	}

	public JEntry function(JFunction function) {
		if (this.functions == null) {
			this.functions = new ArrayList<>();
		}
		this.functions.add(function);
		return this;
	}

	public JEntry function(String function) {
	    return function(JLootTable.function(function));
    }

	public JEntry condition(JCondition condition) {
		if(this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		this.conditions.add(condition);
		return this;
	}

	public JEntry condition(String condition) {
		return condition(JLootTable.predicate(condition));
	}

	public JEntry weight(Integer weight) {
		this.weight = weight;
		return this;
	}

	public JEntry quality(Integer quality) {
		this.quality = quality;
		return this;
	}
}
