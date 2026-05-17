package net.vampirestudios.arrp.assets.models;

import net.vampirestudios.arrp.data.loot.Condition;
import net.vampirestudios.arrp.util.BaseClonable;
import net.minecraft.resources.Identifier;

public class ModelOverride extends BaseClonable<ModelOverride> {
	public final Condition predicate;
	public final String model;

	/**
	 * @see Model#override(Condition, Identifier)
	 */
	public ModelOverride(Condition condition, String model) {
		this.predicate = condition;
		this.model = model;
	}
}
