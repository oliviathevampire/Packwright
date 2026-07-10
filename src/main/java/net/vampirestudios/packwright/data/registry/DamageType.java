package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

public class DamageType {
	public static final Codec<DamageType> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("message_id").forGetter(x -> x.messageId),
			Scaling.CODEC.fieldOf("scaling").forGetter(x -> x.scaling),
			Codec.FLOAT.fieldOf("exhaustion").forGetter(x -> x.exhaustion),
			Effects.CODEC.optionalFieldOf("effects", Effects.HURT).forGetter(x -> x.effects),
			DeathMessageType.CODEC.optionalFieldOf("death_message_type", DeathMessageType.DEFAULT).forGetter(x -> x.deathMessageType)
	).apply(i, (messageId, scaling, exhaustion, effects, deathMessageType) -> {
		DamageType out = new DamageType();
		out.messageId = messageId;
		out.scaling = scaling;
		out.exhaustion = exhaustion;
		out.effects = effects;
		out.deathMessageType = deathMessageType;
		return out;
	}));

	private String messageId;
	private Scaling scaling = Scaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
	private float exhaustion;
	private Effects effects = Effects.HURT;
	private DeathMessageType deathMessageType = DeathMessageType.DEFAULT;

	public static DamageType damageType() {
		return new DamageType();
	}

	public DamageType messageId(String messageId) { this.messageId = messageId; return this; }
	public DamageType scaling(Scaling scaling) { this.scaling = scaling; return this; }
	public DamageType exhaustion(float exhaustion) { this.exhaustion = exhaustion; return this; }
	public DamageType effects(Effects effects) { this.effects = effects; return this; }
	public DamageType deathMessageType(DeathMessageType deathMessageType) { this.deathMessageType = deathMessageType; return this; }

	public String getMessageId() { return messageId; }
	public Scaling getScaling() { return scaling; }
	public float getExhaustion() { return exhaustion; }
	public Effects getEffects() { return effects; }
	public DeathMessageType getDeathMessageType() { return deathMessageType; }

	public enum Scaling implements StringRepresentable {
		NEVER("never"),
		ALWAYS("always"),
		WHEN_CAUSED_BY_LIVING_NON_PLAYER("when_caused_by_living_non_player");

		public static final Codec<Scaling> CODEC = StringRepresentable.fromEnum(Scaling::values);
		private final String name;
		Scaling(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}

	public enum Effects implements StringRepresentable {
		HURT("hurt"),
		THORNS("thorns"),
		DROWNING("drowning"),
		BURNING("burning"),
		POKING("poking"),
		FREEZING("freezing");

		public static final Codec<Effects> CODEC = StringRepresentable.fromEnum(Effects::values);
		private final String name;
		Effects(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}

	public enum DeathMessageType implements StringRepresentable {
		DEFAULT("default"),
		FALL_VARIANTS("fall_variants"),
		INTENTIONAL_GAME_DESIGN("intentional_game_design");

		public static final Codec<DeathMessageType> CODEC = StringRepresentable.fromEnum(DeathMessageType::values);
		private final String name;
		DeathMessageType(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}
}
