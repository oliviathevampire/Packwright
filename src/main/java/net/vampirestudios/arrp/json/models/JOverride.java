package net.vampirestudios.arrp.json.models;

import net.vampirestudios.arrp.json.loot.JCondition;
import net.vampirestudios.arrp.util.BaseClonable;
import net.minecraft.resources.Identifier;

public class JOverride extends BaseClonable<JOverride> {
	public final JCondition predicate;
	public final String model;

	/**
	 * @see JModel#override(JCondition, Identifier)
	 */
	public JOverride(JCondition condition, String model) {
		this.predicate = condition;
		this.model = model;
	}
}
