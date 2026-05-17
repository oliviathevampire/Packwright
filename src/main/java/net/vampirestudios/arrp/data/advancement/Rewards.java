// src/main/java/arrp/adv/Rewards.java
package net.vampirestudios.arrp.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public final class Rewards {
	public static final Codec<Rewards> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("experience").forGetter(r -> Optional.ofNullable(r.experience)),
			Codec.list(Codec.STRING).optionalFieldOf("recipes").forGetter(r -> Optional.ofNullable(r.recipes)),
			Codec.list(Codec.STRING).optionalFieldOf("loot").forGetter(r -> Optional.ofNullable(r.loot)),
			Codec.STRING.optionalFieldOf("function").forGetter(r -> Optional.ofNullable(r.function))
	).apply(i, (xp, rec, loot, fn) -> {
		Rewards r = new Rewards();
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

	public static Rewards ofXp(int xp) {
		Rewards r = new Rewards();
		r.experience = xp;
		return r;
	}

	public Rewards recipes(List<String> ids) {
		this.recipes = ids;
		return this;
	}

	public Rewards loot(List<String> ids) {
		this.loot = ids;
		return this;
	}

	public Rewards function(String id) {
		this.function = id;
		return this;
	}
}
