package net.vampirestudios.arrp.json.lang;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JLang {
    private final Map<String, String> lang = new HashMap<>();

    /**
     * @see #lang()
     */
    public JLang() {
    }

    public static JLang lang() {
        return new JLang();
    }

    private <T> JLang object(Registry<T> registry, String str, T t, String name) {
        return this.object(str,
                Objects.requireNonNull(registry.getKey(t), "register your item before calling this"),
                name);
    }

    private JLang object(String type, Identifier identifier, String translation) {
        this.lang.put(type + '.' + identifier.getNamespace() + '.' + identifier.getPath(), translation);
        return this;
    }

    public JLang entry(String entry, String name) {
        this.lang.put(entry, name);
        return this;
    }

    /**
     * adds a translation key for an item, respects {@link Item#getDescriptionId()}
     */
    public JLang itemRespect(Item item, String name) {
        this.lang.put(item.getDescriptionId(), name);
        return this;
    }

    public JLang item(ItemStack stack, String name) {
        this.lang.put(stack.getItem().getDescriptionId(), name);
        return this;
    }

    /**
     * adds a translation key for a block, respects {@link Block#getDescriptionId()}
     */
    public JLang blockRespect(Block block, String name) {
        this.lang.put(block.getDescriptionId(), name);
        return this;
    }

    public JLang fluid(Fluid fluid, String name) {
        return this.object(BuiltInRegistries.FLUID, "fluid", fluid, name);
    }

    /**
     * adds a translation key for an entity, respects {@link EntityType#getDescriptionId()}
     */
    public JLang entityRespect(EntityType<?> type, String name) {
        this.lang.put(type.getDescriptionId(), name);
        return this;
    }

    public JLang item(Item item, String name) {
        return this.object(BuiltInRegistries.ITEM, "item", item, name);
    }

    public JLang block(Block block, String name) {
        return this.object(BuiltInRegistries.BLOCK, "block", block, name);
    }

    public JLang entity(EntityType<?> type, String name) {
        return this.object(BuiltInRegistries.ENTITY_TYPE, "entity_type", type, name);
    }

    public JLang item(Identifier item, String name) {
        return this.object("item", item, name);
    }

    public JLang block(Identifier block, String name) {
        return this.object("block", block, name);
    }

    public JLang fluid(Identifier id, String name) {
        return this.object("fluid", id, name);
    }

    public JLang entity(Identifier id, String name) {
        return this.object("entity_type", id, name);
    }

    public JLang enchantment(Identifier id, String name) {
        return this.object("enchantment", id, name);
    }

    public JLang itemGroup(Identifier id, String name) {
        return this.object("itemGroup", id, name);
    }

    public JLang sound(Identifier id, String name) {
        return this.object("sound_event", id, name);
    }

    public JLang music(Identifier id, String name) {
        return this.object("music", id, name);
    }

    public JLang effect(Identifier id, String name) {
        return this.object("effect", id, name);
    }

    /**
     * Like {@link JLang#allPotion}, but it adds in the prefixes automatically.
     */
    public JLang allPotionOf(Identifier id, String effectName) {
        return potionDefinition(id, effectName)
                .drinkablePotion(id, "Potion of " + effectName)
                .splashPotion(id, "Splash Potion of " + effectName)
                .lingeringPotion(id, "Lingering Potion of " + effectName)
                .tippedArrow(id, "Tipped Arrow of " + effectName);
    }

    public JLang allPotion(Identifier id,
                           String drinkablePotionName,
                           String splashPotionName,
                           String lingeringPotionName,
                           String tippedArrowName) {
        return this.drinkablePotion(id, drinkablePotionName).splashPotion(id, splashPotionName)
                .lingeringPotion(id, lingeringPotionName).tippedArrow(id, tippedArrowName);
    }

    public JLang potionItem(Identifier id, String name) {
        return drinkablePotion(id, name)
                .splashPotion(id, name)
                .lingeringPotion(id, name)
                .tippedArrow(id, name);
    }

    public JLang potionDefinition(Identifier id, String name) {
        return entry("potion." + id.getNamespace() + "." + id.getPath(), name);
    }

    public JLang drinkablePotion(Identifier id, String name) {
        return entry("item.minecraft.potion.effect." + id.getPath(), name);
    }

    public JLang splashPotion(Identifier id, String name) {
        return entry("item.minecraft.splash_potion.effect." + id.getPath(), name);
    }

    public JLang lingeringPotion(Identifier id, String name) {
        return entry("item.minecraft.lingering_potion.effect." + id.getPath(), name);
    }

    public JLang tippedArrow(Identifier id, String name) {
        return entry("item.minecraft.tipped_arrow.effect." + id.getPath(), name);
    }

    /**
     * Like {@link JLang#drinkablePotion}, but it adds in the "Potion of" automatically.
     */
    public JLang drinkablePotionOf(Identifier id, String effectName) {
        return drinkablePotion(id, "Potion of " + effectName);
    }

    /**
     * Like {@link JLang#splashPotion}, but it adds in the "Splash Potion of" automatically.
     */
    public JLang splashPotionOf(Identifier id, String effectName) {
        return splashPotion(id, "Splash Potion of " + effectName);
    }

    /**
     * Like {@link JLang#lingeringPotion}, but it adds in the "Lingering Potion of" automatically.
     */
    public JLang lingeringPotionOf(Identifier id, String effectName) {
        return lingeringPotion(id, "Lingering Potion of " + effectName);
    }

    /**
     * Like {@link JLang#tippedArrow}, but it adds in the "Tipped Arrow of" automatically.
     */
    public JLang tippedArrowOf(Identifier id, String effectName) {
        return tippedArrow(id, "Tipped Arrow of " + effectName);
    }

    public JLang biome(Identifier id, String name) {
        return this.object("biome", id, name);
    }

    public Map<String, String> getLang() {
        return this.lang;
    }

    public Map<String, String> copyLang() {
        return Map.copyOf(this.lang);
    }

    public JLang merge(JLang other) {
        this.lang.putAll(other.lang);
        return this;
    }

    public JLang entries(Map<String, String> entries) {
        this.lang.putAll(entries);
        return this;
    }

    public JLang copy() {
        return new JLang().entries(this.lang);
    }
}
