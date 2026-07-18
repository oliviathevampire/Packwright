package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Objects;

/**
 * data/<ns>/wolf_sound_variant/<id>.json
 *
 * {
 *   "ambient_sound": "minecraft:...",
 *   "death_sound":   "minecraft:...",
 *   "growl_sound":   "minecraft:...",
 *   "hurt_sound":    "minecraft:...",
 *   "pant_sound":    "minecraft:...",
 *   "whine_sound":   "minecraft:...",
 *   "step_sound":    "minecraft:..."
 * }
 */
public class WolfSoundVariant {
    public static final Codec<WolfSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("ambient_sound").forGetter(x -> x.ambientSound),
            Identifier.CODEC.fieldOf("death_sound").forGetter(x -> x.deathSound),
            Identifier.CODEC.fieldOf("growl_sound").forGetter(x -> x.growlSound),
            Identifier.CODEC.fieldOf("hurt_sound").forGetter(x -> x.hurtSound),
            Identifier.CODEC.fieldOf("pant_sound").forGetter(x -> x.pantSound),
            Identifier.CODEC.fieldOf("whine_sound").forGetter(x -> x.whineSound),
            Identifier.CODEC.fieldOf("step_sound").forGetter(x -> x.stepSound)
    ).apply(i, (ambient, death, growl, hurt, pant, whine, step) -> {
        WolfSoundVariant out = new WolfSoundVariant();
        out.ambientSound = ambient;
        out.deathSound = death;
        out.growlSound = growl;
        out.hurtSound = hurt;
        out.pantSound = pant;
        out.whineSound = whine;
        out.stepSound = step;
        return out;
    }));

    private Identifier ambientSound;
    private Identifier deathSound;
    private Identifier growlSound;
    private Identifier hurtSound;
    private Identifier pantSound;
    private Identifier whineSound;
    private Identifier stepSound;

    public WolfSoundVariant() {}

    public static WolfSoundVariant wolfSoundVariant() {
        return new WolfSoundVariant();
    }

    public WolfSoundVariant ambientSound(Identifier id) { this.ambientSound = Objects.requireNonNull(id, "ambientSound"); return this; }
    public WolfSoundVariant deathSound(Identifier id)   { this.deathSound   = Objects.requireNonNull(id, "deathSound");   return this; }
    public WolfSoundVariant growlSound(Identifier id)   { this.growlSound   = Objects.requireNonNull(id, "growlSound");   return this; }
    public WolfSoundVariant hurtSound(Identifier id)    { this.hurtSound    = Objects.requireNonNull(id, "hurtSound");    return this; }
    public WolfSoundVariant pantSound(Identifier id)    { this.pantSound    = Objects.requireNonNull(id, "pantSound");    return this; }
    public WolfSoundVariant whineSound(Identifier id)   { this.whineSound   = Objects.requireNonNull(id, "whineSound");   return this; }
    public WolfSoundVariant stepSound(Identifier id)    { this.stepSound    = Objects.requireNonNull(id, "stepSound");    return this; }

    public Identifier getAmbientSound() { return ambientSound; }
    public Identifier getDeathSound()   { return deathSound; }
    public Identifier getGrowlSound()   { return growlSound; }
    public Identifier getHurtSound()    { return hurtSound; }
    public Identifier getPantSound()    { return pantSound; }
    public Identifier getWhineSound()   { return whineSound; }
    public Identifier getStepSound()    { return stepSound; }
}
