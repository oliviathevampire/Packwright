package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * data/<ns>/wolf_sound_variant/<id>.json
 *
 * {
 *   "ambient_sound": "minecraft:...",
 *   "death_sound":   "minecraft:...",
 *   "growl_sound":   "minecraft:...",
 *   "hurt_sound":    "minecraft:...",
 *   "pant_sound":    "minecraft:...",
 *   "whine_sound":   "minecraft:..."
 * }
 */
public class JWolfSoundVariant {
    public static final Codec<JWolfSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("ambient_sound").forGetter(x -> x.ambientSound),
            Identifier.CODEC.fieldOf("death_sound").forGetter(x -> x.deathSound),
            Identifier.CODEC.fieldOf("growl_sound").forGetter(x -> x.growlSound),
            Identifier.CODEC.fieldOf("hurt_sound").forGetter(x -> x.hurtSound),
            Identifier.CODEC.fieldOf("pant_sound").forGetter(x -> x.pantSound),
            Identifier.CODEC.fieldOf("whine_sound").forGetter(x -> x.whineSound)
    ).apply(i, (ambient, death, growl, hurt, pant, whine) -> {
        JWolfSoundVariant out = new JWolfSoundVariant();
        out.ambientSound = ambient;
        out.deathSound = death;
        out.growlSound = growl;
        out.hurtSound = hurt;
        out.pantSound = pant;
        out.whineSound = whine;
        return out;
    }));

    private Identifier ambientSound;
    private Identifier deathSound;
    private Identifier growlSound;
    private Identifier hurtSound;
    private Identifier pantSound;
    private Identifier whineSound;

    public JWolfSoundVariant() {}

    public static JWolfSoundVariant wolfSoundVariant() {
        return new JWolfSoundVariant();
    }

    public JWolfSoundVariant ambientSound(Identifier id) { this.ambientSound = id; return this; }
    public JWolfSoundVariant deathSound(Identifier id)   { this.deathSound = id; return this; }
    public JWolfSoundVariant growlSound(Identifier id)   { this.growlSound = id; return this; }
    public JWolfSoundVariant hurtSound(Identifier id)    { this.hurtSound = id; return this; }
    public JWolfSoundVariant pantSound(Identifier id)    { this.pantSound = id; return this; }
    public JWolfSoundVariant whineSound(Identifier id)   { this.whineSound = id; return this; }

    public Identifier getAmbientSound() { return ambientSound; }
    public Identifier getDeathSound()   { return deathSound; }
    public Identifier getGrowlSound()   { return growlSound; }
    public Identifier getHurtSound()    { return hurtSound; }
    public Identifier getPantSound()    { return pantSound; }
    public Identifier getWhineSound()   { return whineSound; }
}
