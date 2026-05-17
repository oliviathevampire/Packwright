package net.vampirestudios.arrp.assets.equipment;

public enum LayerType {
	HUMANOID("humanoid"),
	HUMANOID_LEGGINGS("humanoid_leggings"),
	WINGS("wings"),
	WOLF_BODY("wolf_body"),
	HORSE_BODY("horse_body"),
	LLAMA_BODY("llama_body"),
	PIG_SADDLE("pig_saddle"),
	STRIDER_SADDLE("strider_saddle"),
	CAMEL_SADDLE("camel_saddle"),
	HORSE_SADDLE("horse_saddle"),
	DONKEY_SADDLE("donkey_saddle"),
	MULE_SADDLE("mule_saddle"),
	ZOMBIE_HORSE_SADDLE("zombie_horse_saddle"),
	SKELETON_HORSE_SADDLE("skeleton_horse_saddle"),
	CUSTOM(null);

	private final String id;

	LayerType(String id) {
		this.id = id;
	}

	public String asString() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}
}
