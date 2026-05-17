package net.vampirestudios.arrp.assets.lang;

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

public class Lang {
    private final Map<String, String> lang = new HashMap<>();

    /**
     * @see #lang()
     */
    public Lang() {
    }

    public static Lang lang() {
        return new Lang();
    }

    private <T> Lang object(Registry<T> registry, String str, T t, String name) {
        return this.object(str,
                Objects.requireNonNull(registry.getKey(t), "register your item before calling this"),
                name);
    }

    private Lang object(String type, Identifier identifier, String translation) {
        this.lang.put(type + '.' + identifier.getNamespace() + '.' + identifier.getPath(), translation);
        return this;
    }

    public Lang entry(String entry, String name) {
        this.lang.put(entry, name);
        return this;
    }

    /**
     * adds a translation key for an item, respects {@link Item#getDescriptionId()}
     */
    public Lang itemRespect(Item item, String name) {
        this.lang.put(item.getDescriptionId(), name);
        return this;
    }

    public Lang item(ItemStack stack, String name) {
        this.lang.put(stack.getItem().getDescriptionId(), name);
        return this;
    }

    /**
     * adds a translation key for a block, respects {@link Block#getDescriptionId()}
     */
    public Lang blockRespect(Block block, String name) {
        this.lang.put(block.getDescriptionId(), name);
        return this;
    }

    public Lang fluid(Fluid fluid, String name) {
        return this.object(BuiltInRegistries.FLUID, "fluid", fluid, name);
    }

    /**
     * adds a translation key for an entity, respects {@link EntityType#getDescriptionId()}
     */
    public Lang entityRespect(EntityType<?> type, String name) {
        this.lang.put(type.getDescriptionId(), name);
        return this;
    }

    public Lang item(Item item, String name) {
        return this.object(BuiltInRegistries.ITEM, "item", item, name);
    }

    public Lang block(Block block, String name) {
        return this.object(BuiltInRegistries.BLOCK, "block", block, name);
    }

    public Lang entity(EntityType<?> type, String name) {
        return this.object(BuiltInRegistries.ENTITY_TYPE, "entity_type", type, name);
    }

    public Lang item(Identifier item, String name) {
        return this.object("item", item, name);
    }

    public Lang block(Identifier block, String name) {
        return this.object("block", block, name);
    }

    public Lang fluid(Identifier id, String name) {
        return this.object("fluid", id, name);
    }

    public Lang entity(Identifier id, String name) {
        return this.object("entity_type", id, name);
    }

    public Lang enchantment(Identifier id, String name) {
        return this.object("enchantment", id, name);
    }

    public Lang itemGroup(Identifier id, String name) {
        return this.object("itemGroup", id, name);
    }

    public Lang sound(Identifier id, String name) {
        return this.object("sound_event", id, name);
    }

    public Lang music(Identifier id, String name) {
        return this.object("music", id, name);
    }

    public Lang effect(Identifier id, String name) {
        return this.object("effect", id, name);
    }

    /**
     * Like {@link Lang#allPotion}, but it adds in the prefixes automatically.
     */
    public Lang allPotionOf(Identifier id, String effectName) {
        return potionDefinition(id, effectName)
                .drinkablePotion(id, "Potion of " + effectName)
                .splashPotion(id, "Splash Potion of " + effectName)
                .lingeringPotion(id, "Lingering Potion of " + effectName)
                .tippedArrow(id, "Tipped Arrow of " + effectName);
    }

    public Lang allPotion(Identifier id,
                           String drinkablePotionName,
                           String splashPotionName,
                           String lingeringPotionName,
                           String tippedArrowName) {
        return this.drinkablePotion(id, drinkablePotionName).splashPotion(id, splashPotionName)
                .lingeringPotion(id, lingeringPotionName).tippedArrow(id, tippedArrowName);
    }

    public Lang potionItem(Identifier id, String name) {
        return drinkablePotion(id, name)
                .splashPotion(id, name)
                .lingeringPotion(id, name)
                .tippedArrow(id, name);
    }

    public Lang potionDefinition(Identifier id, String name) {
        return entry("potion." + id.getNamespace() + "." + id.getPath(), name);
    }

    public Lang drinkablePotion(Identifier id, String name) {
        return entry("item.minecraft.potion.effect." + id.getPath(), name);
    }

    public Lang splashPotion(Identifier id, String name) {
        return entry("item.minecraft.splash_potion.effect." + id.getPath(), name);
    }

    public Lang lingeringPotion(Identifier id, String name) {
        return entry("item.minecraft.lingering_potion.effect." + id.getPath(), name);
    }

    public Lang tippedArrow(Identifier id, String name) {
        return entry("item.minecraft.tipped_arrow.effect." + id.getPath(), name);
    }

    /**
     * Like {@link Lang#drinkablePotion}, but it adds in the "Potion of" automatically.
     */
    public Lang drinkablePotionOf(Identifier id, String effectName) {
        return drinkablePotion(id, "Potion of " + effectName);
    }

    /**
     * Like {@link Lang#splashPotion}, but it adds in the "Splash Potion of" automatically.
     */
    public Lang splashPotionOf(Identifier id, String effectName) {
        return splashPotion(id, "Splash Potion of " + effectName);
    }

    /**
     * Like {@link Lang#lingeringPotion}, but it adds in the "Lingering Potion of" automatically.
     */
    public Lang lingeringPotionOf(Identifier id, String effectName) {
        return lingeringPotion(id, "Lingering Potion of " + effectName);
    }

    /**
     * Like {@link Lang#tippedArrow}, but it adds in the "Tipped Arrow of" automatically.
     */
    public Lang tippedArrowOf(Identifier id, String effectName) {
        return tippedArrow(id, "Tipped Arrow of " + effectName);
    }

    public Lang biome(Identifier id, String name) {
        return this.object("biome", id, name);
    }

    public Map<String, String> getLang() {
        return this.lang;
    }

    public Map<String, String> copyLang() {
        return Map.copyOf(this.lang);
    }

    public Lang merge(Lang other) {
        this.lang.putAll(other.lang);
        return this;
    }

    public Lang entries(Map<String, String> entries) {
        this.lang.putAll(entries);
        return this;
    }

    public Lang copy() {
        return new Lang().entries(this.lang);
    }
}
