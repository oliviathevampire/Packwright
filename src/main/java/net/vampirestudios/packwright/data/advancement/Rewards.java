package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public final class Rewards {
	public static final Codec<Rewards> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("experience").forGetter(r -> Optional.ofNullable(r.experience)),
			Identifier.CODEC.listOf().optionalFieldOf("recipes").forGetter(r -> Optional.ofNullable(r.recipes)),
			Identifier.CODEC.listOf().optionalFieldOf("loot").forGetter(r -> Optional.ofNullable(r.loot)),
			Identifier.CODEC.optionalFieldOf("function").forGetter(r -> Optional.ofNullable(r.function))
	).apply(i, (xp, rec, loot, fn) -> {
		Rewards r = new Rewards();
		r.experience = xp.orElse(null);
		r.recipes = rec.orElse(null);
		r.loot = loot.orElse(null);
		r.function = fn.orElse(null);
		return r;
	}));

	private Integer experience;
	private List<Identifier> recipes;
	private List<Identifier> loot;
	private Identifier function;

	public static Rewards ofXp(int xp) {
		Rewards r = new Rewards();
		r.experience = xp;
		return r;
	}

	public Rewards experience(int xp) { this.experience = xp; return this; }
	public Rewards recipes(List<Identifier> ids) { this.recipes = ids; return this; }
	public Rewards loot(List<Identifier> ids) { this.loot = ids; return this; }
	public Rewards function(Identifier id) { this.function = id; return this; }
	public Rewards function(String id) { return function(Identifier.tryParse(id)); }

	public Integer getExperience() { return experience; }
	public List<Identifier> getRecipes() { return recipes; }
	public List<Identifier> getLoot() { return loot; }
	public Identifier getFunction() { return function; }
}
