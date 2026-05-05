// src/main/java/arrp/adv/JRewards.java
package net.vampirestudios.arrp.json.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public final class JRewards {
	public static final Codec<JRewards> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("experience").forGetter(r -> Optional.ofNullable(r.experience)),
			Codec.list(Codec.STRING).optionalFieldOf("recipes").forGetter(r -> Optional.ofNullable(r.recipes)),
			Codec.list(Codec.STRING).optionalFieldOf("loot").forGetter(r -> Optional.ofNullable(r.loot)),
			Codec.STRING.optionalFieldOf("function").forGetter(r -> Optional.ofNullable(r.function))
	).apply(i, (xp, rec, loot, fn) -> {
		JRewards r = new JRewards();
		r.experience = xp.orElse(null);
		r.recipes = rec.orElse(null);
		r.loot = loot.orElse(null);
		r.function = fn.orElse(null);
		return r;
	}));
	public Integer experience;           // optional
	public List<String> recipes;         // optional
	public List<String> loot;            // optional
	public String function;              // optional

	public static JRewards ofXp(int xp) {
		JRewards r = new JRewards();
		r.experience = xp;
		return r;
	}

	public JRewards recipes(List<String> ids) {
		this.recipes = ids;
		return this;
	}

	public JRewards loot(List<String> ids) {
		this.loot = ids;
		return this;
	}

	public JRewards function(String id) {
		this.function = id;
		return this;
	}
}
