package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.timeline.Timelines;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.assets.blockstates.BlockState;
import net.vampirestudios.arrp.assets.blockstates.Connectables;
import net.vampirestudios.arrp.assets.blockstates.Variant;
import net.vampirestudios.arrp.assets.equipment.*;
import net.vampirestudios.arrp.assets.item.*;
import net.vampirestudios.arrp.assets.lang.Lang;
import net.vampirestudios.arrp.assets.models.Model;
import net.vampirestudios.arrp.assets.models.Textures;
import net.vampirestudios.arrp.data.entity.*;
import net.vampirestudios.arrp.data.recipe.*;
import net.vampirestudios.arrp.data.registry.*;
import net.vampirestudios.arrp.data.worldgen.*;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.util.ResourceLocationTypeAdapter;
import net.vampirestudios.arrp.util.VanillaIds;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.vampirestudios.arrp.assets.blockstates.BlockState.*;
import static net.vampirestudios.arrp.assets.models.Model.*;
import static net.vampirestudios.arrp.data.loot.LootTable.*;
import static net.vampirestudios.arrp.util.ResourceHelpers.customId;
import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class RRPPreTest {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	static void main(String[] args) {
		RuntimeResourcePack pack = RuntimeResourcePack.create("test:test");
		pack.addLang(Identifier.tryParse("aaaa:aaaa"), new Lang().entry("aaaa", "bbbbb"));
		pack.addLang(Identifier.tryParse("modid:en_us"), new Lang().entry("item.custom", "Custom Item"));
		pack.addLang(Identifier.tryParse("modid:es_es"), new Lang().entry("item.custom", "Artículo Personalizado"));
		pack.addLang(Identifier.tryParse("minecraft:en_us"), new Lang()
				.effect(vanillaId("fire_walking"), "Fire Walking")
				.allPotionOf(vanillaId("fire_walking"), "Fire Walking")
		);
		pack.addItemModelInfo(ItemInfo.item().model(ItemModel.select()
				.property(PropertyComponent.component("minecraft:item_name"))
				.addCase(SelectCase.of("Diamond", ModelBasic.of(vanillaId("item/diamond"))))
				.addCase(SelectCase.of("Netherite Ingot", ModelBasic.of(vanillaId("item/netherite_ingot"))))
				.addCase(SelectCase.of("Dirt", ModelBasic.of(vanillaId("block/dirt"))))
				.addCase(SelectCase.of("Coal", ItemModel.rangeDispatch()
						.property(PropertyCount.count())
						.property(PropertyCount.count())
						.entry(RangeEntry.of(10, ModelBasic.of(vanillaId("item/charcoal"))))
						.fallback(ModelBasic.of(vanillaId("item/coal")))
				))
				.fallback(ModelBasic.of(vanillaId("block/stone")))), customId("test", "test_item"));
		pack.addEquipmentModel(EquipmentModel.model()
				.addLayer(LayerType.HUMANOID, Layer.layer().texture(customId("test", "a/test"))),
				customId("test", "test_armor")
		);
		pack.addWolfVariant(
				myModId("golden"),
				WolfVariant.wolfVariant()
						.assets(
								myModId("entity/wolf/wolf_golden"),
								myModId("entity/wolf/wolf_golden_tame"),
								myModId("entity/wolf/wolf_golden_angry")
						)
						.spawnConditions(SpawnPrioritySelectors.single(BiomeSpawnCondition.biomeCondition()
								.biome(vanillaId("forest")),
								1
						))
		);
		pack.addZombieNautilusVariant(
				myModId("warm_reef"),
				ZombieNautilusVariant.zombieNautilusVariant()
						.assetId(myModId("entity/zombie_nautilus/warm_reef"))
						.model(ZombieNautilusVariant.ModelType.WARM)
						.spawnConditions(SpawnPrioritySelectors.single(BiomeSpawnCondition.biomeCondition()
								.biomeTag(vanillaId("is_ocean")),
								1
						))
		);
		pack.addSimpleMobVariant(
				vanillaId("frog_variant"),
				myModId("toxic"),
				SimpleMobVariant.mobVariant()
						.assetId(myModId("entity/frog/toxic"))
						.spawnConditions(SpawnPrioritySelectors.single(BiomeSpawnCondition.biomeCondition()
								.biome(vanillaId("forest")),
								1
						))
		);
		var spawns = SpawnPrioritySelectors.selectors()
				.add(MoonBrightnessSpawnCondition.moonBrightness().range(MinMaxBounds.Doubles.atMost(0.25f)), 1)
				.freeze();

		pack.addChickenVariant(
				myModId("frost_chicken"),
				ChickenVariant.chickenVariant()
						.model(ChickenVariant.ModelType.COLD)
						.assetId(myModId("entity/chicken/frost"))
						.spawnConditions(spawns)
		);
		pack.addCowVariant(
				myModId("savanna"),
				CowVariant.cowVariant()
						.model(CowVariant.ModelType.WARM)
						.assetId(myModId("entity/cow/savanna"))
		);
		pack.addWolfSoundVariant(
				myModId("angryish"),
				WolfSoundVariant.wolfSoundVariant()
						.ambientSound(vanillaId("entity.wolf_angry.ambient"))
						.deathSound(vanillaId("entity.wolf_angry.death"))
						.growlSound(vanillaId("entity.wolf_angry.growl"))
						.hurtSound(vanillaId("entity.wolf_angry.hurt"))
						.pantSound(vanillaId("entity.wolf_angry.pant"))
						.whineSound(vanillaId("entity.wolf_angry.whine"))
		);
		var catSounds = CatSoundVariant.sounds()
				.ambientSound(vanillaId("entity.cat.ambient"))
				.strayAmbientSound(vanillaId("entity.cat.stray_ambient"))
				.hissSound(vanillaId("entity.cat.hiss"))
				.hurtSound(vanillaId("entity.cat.hurt"))
				.deathSound(vanillaId("entity.cat.death"))
				.eatSound(vanillaId("entity.cat.eat"))
				.begForFoodSound(vanillaId("entity.cat.beg_for_food"))
				.purrSound(vanillaId("entity.cat.purr"))
				.purreowSound(vanillaId("entity.cat.purreow"));
		pack.addCatSoundVariant(
				myModId("bright"),
				CatSoundVariant.catSoundVariant()
						.adultSounds(catSounds)
						.babySounds(catSounds)
		);
		var chickenSounds = ChickenSoundVariant.sounds()
				.ambientSound(vanillaId("entity.chicken.ambient"))
				.hurtSound(vanillaId("entity.chicken.hurt"))
				.deathSound(vanillaId("entity.chicken.death"))
				.stepSound(vanillaId("entity.chicken.step"));
		pack.addChickenSoundVariant(
				myModId("clucky"),
				ChickenSoundVariant.chickenSoundVariant()
						.adultSounds(chickenSounds)
						.babySounds(chickenSounds)
		);
		pack.addCowSoundVariant(
				myModId("soft"),
				CowSoundVariant.cowSoundVariant()
						.ambientSound(vanillaId("entity.cow.ambient"))
						.hurtSound(vanillaId("entity.cow.hurt"))
						.deathSound(vanillaId("entity.cow.death"))
						.stepSound(vanillaId("entity.cow.step"))
		);
		var pigSounds = PigSoundVariant.sounds()
				.ambientSound(vanillaId("entity.pig.ambient"))
				.hurtSound(vanillaId("entity.pig.hurt"))
				.deathSound(vanillaId("entity.pig.death"))
				.stepSound(vanillaId("entity.pig.step"))
				.eatSound(vanillaId("entity.pig.ambient"));
		pack.addPigSoundVariant(
				myModId("snuffly"),
				PigSoundVariant.pigSoundVariant()
						.adultSounds(pigSounds)
						.babySounds(pigSounds)
		);
		pack.addPaintingVariant(
				myModId("tiny_sky"),
				PaintingVariant.paintingVariant()
						.size(2, 1)
						.assetId(myModId("tiny_sky"))
						.title("Tiny Sky")
						.author("ARRP")
		);
		pack.addTrimMaterial(
				myModId("copper"),
				TrimMaterial.trimMaterial()
						.assets(TrimMaterial.AssetGroup.assetGroup("copper")
								.overrideArmorAsset(myModId("test_armor"), "copper_test"))
						.description("Copper")
		);
		pack.addTrimPattern(
				myModId("sunburst"),
				TrimPattern.trimPattern()
						.assetId(myModId("sunburst"))
						.description("Sunburst Armor Trim")
						.decal(false)
		);
		pack.addBannerPattern(
				myModId("spark"),
				BannerPattern.bannerPattern()
						.assetId(myModId("spark"))
						.translationKey("block.mymod.banner.spark")
		);
		pack.addDecoratedPotPattern(
				myModId("spiral"),
				DecoratedPotPattern.decoratedPotPattern()
						.assetId(myModId("spiral_pottery_pattern"))
		);
		pack.addDamageType(
				myModId("spark"),
				DamageType.damageType()
						.messageId("spark")
						.scaling(DamageType.Scaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER)
						.exhaustion(0.1F)
						.effects(DamageType.Effects.HURT)
		);
		pack.addInstrument(
				myModId("small_horn"),
				Instrument.instrument()
						.soundEvent(vanillaId("item.goat_horn.sound.0"))
						.useDuration(7.0F)
						.range(64.0F)
						.description("Small Horn")
		);
		pack.addJukeboxSong(
				myModId("test_song"),
				JukeboxSong.jukeboxSong()
						.soundEvent(myModId("music.test_song"))
						.description("Test Song")
						.lengthInSeconds(120.0F)
						.comparatorOutput(7)
		);
		pack.addConfiguredCarver(
				myModId("small_cave"),
				ConfiguredCarver.carver()
						.type("minecraft:cave")
						.probability(0.05F)
						.replaceable(Identifier.tryParse("minecraft:overworld_carver_replaceables"))
		);
		pack.addConfiguredCarver(
				myModId("canyon"),
				ConfiguredCarver.canyon()
						.config(ConfiguredCarver.Config.config()
								.debugSettings(ConfiguredCarver.DebugSettings.debugSettings()
										.airState(WorldgenBlockState.blockState(vanillaId("warped_button"))
												.property("face", "wall")
												.property("facing", "north")
												.property("powered", false))
										.barrierState(WorldgenBlockState.blockState(vanillaId("glass")))
										.lavaState(WorldgenBlockState.blockState(vanillaId("orange_stained_glass")))
										.waterState(WorldgenBlockState.blockState(vanillaId("candle"))
												.property("candles", 1)
												.property("lit", false)
												.property("waterlogged", false)))
								.lavaLevel(VerticalAnchor.aboveBottom(8))
								.probability(0.01F)
								.replaceable(vanillaId("overworld_carver_replaceables"))
								.shape(ConfiguredCarver.CanyonShape.canyonShape()
										.distanceFactor(FloatProvider.uniform(0.75F, 1.0F))
										.horizontalRadiusFactor(FloatProvider.uniform(0.75F, 1.0F))
										.thickness(FloatProvider.trapezoid(0.0F, 6.0F, 2.0F))
										.verticalRadiusCenterFactor(0.0F)
										.verticalRadiusDefaultFactor(1.0F)
										.widthSmoothness(3))
								.verticalRotation(FloatProvider.uniform(-0.125F, 0.125F))
								.y(HeightProvider.uniform(VerticalAnchor.absolute(10), VerticalAnchor.absolute(67)))
								.yScale(3.0F))
		);
		pack.addProcessorList(
				myModId("mossy_replace"),
				ProcessorList.processorList()
						.rule(ProcessorList.Rule.replace("minecraft:cobblestone", "minecraft:mossy_cobblestone"))
		);
		pack.addTemplatePool(
				myModId("test_pool"),
				TemplatePool.pool()
						.fallback(vanillaId("empty"))
						.single(myModId("test/start"), myModId("mossy_replace"), TemplatePool.Projection.RIGID, 1)
		);
		pack.addWorldPreset(
				myModId("single_biome"),
				WorldPreset.preset()
						.overworld(WorldPreset.Dimension.dimension()
								.type(vanillaId("overworld"))
								.noiseGenerator(vanillaId("overworld"), WorldPreset.Generator.BiomeSource.fixed(vanillaId("plains"))))
		);
		pack.addFlatLevelGeneratorPreset(
				myModId("copper_flat"),
				FlatLevelGeneratorPreset.preset()
						.display(vanillaId("copper_block"))
						.biome(vanillaId("plains"))
						.layer(vanillaId("bedrock"), 1)
						.layer(VanillaIds.STONE, 3)
						.layer(vanillaId("grass_block"), 1)
		);
		pack.addVillagerTrade(
				myModId("toolsmith/1/copper_for_emeralds"),
				VillagerTrade.trade()
						.wants("minecraft:emerald", 3)
						.gives("minecraft:copper_ingot", 8)
						.maxUses(12)
						.xp(2)
		);
		pack.addTradeSet(
				myModId("toolsmith_copper"),
				TradeSet.tradeSet()
						.trade("mymod:toolsmith/1/copper_for_emeralds")
						.amount(TradeSet.NumberProvider.uniform(1, 2))
						.allowDuplicates(false)
		);
		pack.addDialog(
				myModId("welcome"),
				Dialog.notice("Welcome", "Continue")
						.plainMessage("This dialog was generated by ARRP.")
						.input(Dialog.Input.text("name", "Name"))
		);
		pack.addWorldClock(
				myModId("example_clock"),
				WorldClock.worldClock()
		);
		pack.addItemModelInfo(
				ItemInfo.item().model(ItemModel.model(customId("test", "block/model")).tint(Tint.dye(0xFFFFFF))),
				myModId("test_block")
		);

		pack.addRecipe(
				customId("example", "diamond_pickaxe_to_block"),
				Recipe.shapeless(
						Ingredients.ingredients().addFabricCustom(
								vanillaId("diamond_pickaxe"),
								components -> components.addProperty("minecraft:damage", 0)
						),
						Result.result(vanillaId("diamond_block"))
				).group("test").category("misc")
		);
		pack.addRecipe(customId("example", "pumpkin"),
				Recipe.shaped(
						Pattern.pattern("PPP", "P P", "PPP"),
						Keys.keys().item("P", vanillaId("pumpkin_pie")),
						Result.stackedResult(vanillaId("pumpkin"), 3)
				)
		);
		pack.addRecipe(
				customId("example", "golden_sword"),
				Recipe.shapeless(
						Ingredients.ingredients()
								.addItem(vanillaId("stick"))
								.addItem(vanillaId("gold_ingot"))
								.addItem(vanillaId("gold_ingot"))
								.addItem(vanillaId("gold_ingot")),
						Result.result(vanillaId("golden_sword"))
								.components(builder -> {
									builder.addProperty("minecraft:damage", 3);
									builder.addProperty("minecraft:rarity", "RARE");
								})
				)
		);
		pack.dumpDirect(Path.of("aaaa"));

		BlockState iron_block = state(variant(BlockState.model(vanillaId("block/iron_block"))));
		BlockState oak_fence = state(multipart(BlockState.model(vanillaId("block/oak_fence_post"))),
				multipart(BlockState.model(vanillaId("block/oak_fence_side")).uvlock()).when(when().isTrue("north")),
				multipart(BlockState.model(vanillaId("block/oak_fence_side")).y(90).uvlock()).when(when().isTrue("east")),
				multipart(BlockState.model(vanillaId("block/oak_fence_side")).y(180).uvlock()).when(when().isTrue("south")),
				multipart(BlockState.model(vanillaId("block/oak_fence_side")).y(270).uvlock()).when(when().isTrue("west")));

		Model model = Model.model().textures(Model.textures().var("all", "block/bamboo_stalk").particle("block/bamboo_stalk"))
				.element(element().bounds(7, 0, 7, 9, 16, 9)
						.faces(faces().down(face("all").cullface(Direction.DOWN).uv(13, 4, 15, 6))
								.up(face("all").cullface(Direction.UP).uv(13, 0, 15, 2))
								.north(face("all").uv(9, 0, 11, 16))
								.south(face("all").uv(9, 0, 11, 16))
								.west(face("all").uv(9, 0, 11, 16))
								.east(face("all").uv(9, 0, 11, 16))
						)
				);


		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Textures.class, new Textures.Serializer())
				.registerTypeAdapter(Identifier.class, new ResourceLocationTypeAdapter())
				.setPrettyPrinting()
				.create();

		Lang lang = Lang.lang().allPotionOf(customId("mod_id", "potion_id"), "Example");

		System.out.println(RuntimeResourcePackImpl.GSON.toJson(loot("minecraft:block").pool(pool().rolls(1)
				.entry(entry().type("minecraft:item").name("minecraft:diamond"))
				.condition(predicate("minecraft:survives_explosion")))));
		System.out.println(gson.toJson(iron_block));
		System.out.println(gson.toJson(oak_fence));
		System.out.println(gson.toJson(model));

		System.out.println(gson.toJson(lang));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ModelBasic.of(vanillaId("item/template_spawn_egg"))
				.tint(new TintConstant(-278045))
				.tint(new TintConstant(-5886604))
		)));


		ItemInfo itemInfo = ItemInfo.item().model(
				ItemModel.model(vanillaId("item/template_spawn_egg"))
						.tints(Tint.constant(-278045), Tint.constant(-5886604))
		).handAnimationOnSwap(true);
		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, itemInfo));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.model(vanillaId("item/bamboo")))));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.rangeDispatch()
				.property(PropertyUseCycle.useCycle().period(10f))
				.scale(0.1)
				.entry(RangeEntry.of(0.25, ModelBasic.of(vanillaId("item/brush_brushing_0"))))
				.entry(RangeEntry.of(0.5, ModelBasic.of(vanillaId("item/brush_brushing_1"))))
				.entry(RangeEntry.of(0.75, ModelBasic.of(vanillaId("item/brush_brushing_2"))))
				.fallback(ModelBasic.of(vanillaId("item/brush")))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.condition()
				.property(new PropertyUsingItem())
				.onTrue(ModelBasic.of(vanillaId("item/using_model")))
				.onFalse(ModelBasic.of(vanillaId("item/default_model")))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.composite()
				.model(ModelBasic.of(vanillaId("item/part1")))
				.model(ModelBasic.of(vanillaId("item/part2")))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.rangeDispatch()
				.property(PropertyDamage.of(true))
				.entry(RangeEntry.of(0.25, ModelBasic.of(vanillaId("item/damage_low"))))
				.entry(RangeEntry.of(0.5, ModelBasic.of(vanillaId("item/damage_medium"))))
				.entry(RangeEntry.of(0.75, ModelBasic.of(vanillaId("item/damage_high"))))
				.fallback(ModelBasic.of(vanillaId("item/damage_full")))
		)));

//		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ModelSpecial.head()
//				.kind(SkullBlock.Type.DRAGON)
//				.animation(0.5f)
//		).handAnimationOnSwap(false)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.condition()
				.property(new PropertyKeybindDown("key.sneak"))
				.onTrue(ModelBasic.of(vanillaId("item/sneaking_model")))
				.onFalse(ModelBasic.of(vanillaId("item/normal_model")))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ModelBasic
				.of(vanillaId("item/team_colored_item"))
				.tint(TintTeam.of(-1))
		)));


		// Create range dispatch entries
		List<RangeEntry> entries = new ArrayList<>();
		for (int i = 0; i <= 31; i++) {
			entries.add(new RangeEntry(
					i / 32.0, // normalized threshold
					ItemModel.model(vanillaId("item/compass_" + (i < 10 ? "0" : "") + i))
			));
		}

		// Create range dispatch for "on_true"
		ModelRangeDispatch onTrueDispatch = ItemModel.rangeDispatch()
				.property(new PropertyCompass().target("lodestone"))
				.scale(32.0)
				.entries(entries);

		// Create range dispatch for fallback
		ModelRangeDispatch fallbackDispatch = ItemModel.rangeDispatch()
				.property(new PropertyCompass().target("none"))
				.scale(32.0)
				.entries(entries);

		// Create select model for "on_false"
		ItemModel onFalseSelect = ItemModel.select()
				.property(new PropertyContextDimension())
				.addCase(SelectCase.of("overworld", ItemModel.rangeDispatch()
						.property(new PropertyCompass().target("spawn"))
						.scale(32.0)
						.entries(entries)
				)).fallback(fallbackDispatch);

		// Create the full condition model
		ModelCondition compassModel = ItemModel.condition()
				.property(PropertyHasComponent.of("minecraft:lodestone_tracker"))
				.onTrue(onTrueDispatch)
				.onFalse(onFalseSelect);

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(compassModel)));

		// Create the condition property
		PropertyUsingItem usingItemProperty = new PropertyUsingItem();

		// Create the nested shield model
//		ItemModel shieldNestedModel = ModelSpecial.shield();
//
//		// Create the on_false model (not blocking)
//		ModelSpecial onFalseModel = ModelSpecial.specialModel("minecraft:item/shield", shieldNestedModel);
//
//		// Create the on_true model (blocking)
//		ModelSpecial onTrueModel = ModelSpecial.specialModel("minecraft:item/shield_blocking", shieldNestedModel);
//
//		// Create the condition model
//		ModelCondition conditionModel = ItemModel.condition()
//				.property(usingItemProperty)
//				.onFalse(onFalseModel)
//				.onTrue(onTrueModel);
//
//		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(conditionModel)));

		PropertyComponent componentProperty = PropertyComponent.component("minecraft:item_name");

		// Build the select model with cases
		ItemModel selectModel = ItemModel.select()
				.property(componentProperty)
				.addCase(SelectCase.of("Diamond", ModelBasic.of(vanillaId("item/diamond"))))
				.addCase(SelectCase.of("Netherite Ingot", ModelBasic.of(vanillaId("item/netherite_ingot"))))
				.addCase(SelectCase.of("Dirt", ModelBasic.of(vanillaId("block/dirt"))))
				.addCase(SelectCase.of("Coal", ItemModel.rangeDispatch()
						.property(PropertyCount.count())
						.entry(RangeEntry.of(10, ModelBasic.of(vanillaId("item/charcoal"))))
						.fallback(ModelBasic.of(vanillaId("item/coal")))
				))
				.fallback(ModelBasic.of(vanillaId("block/stone")));
		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(selectModel)));

		selectModel = ItemModel.select()
				.property(componentProperty)
				.addCase(SelectCase.of("Diamond", ModelBasic.of(vanillaId("item/diamond"))))
				.addCase(SelectCase.of("Netherite Ingot", ModelBasic.of(vanillaId("item/netherite_ingot"))))
				.addCase(SelectCase.of("Dirt", ModelBasic.of(vanillaId("block/dirt"))))
				.addCase(SelectCase.of("Coal", ItemModel.rangeDispatch()
						.property(PropertyCount.count())
						.entry(RangeEntry.of(10, ModelBasic.of(vanillaId("item/charcoal"))))
						.fallback(ModelBasic.of(vanillaId("item/coal")))
				))
				.fallback(ModelBasic.of(vanillaId("block/stone")).tint(new TintDye(0xFF00FF)));
		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(selectModel)));

		EquipmentModel eq = EquipmentModel.model()
				.addLayer("wings",
						Layer.layer()
								.texture(vanillaId("elytra"))
								.usePlayerTexture(true)
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, eq));

		EquipmentModel armorModel = EquipmentModel.model()
				// leather leggings, dyeable with default color
				.addLayer(LayerType.HUMANOID_LEGGINGS,
						Layer.layer()
								.texture(vanillaId("leather_leggings"))
								.dyeable(Optional.of(0xA06540))   // default brown if undyed
				)
				// main body layer, dyeable
				.addLayer(LayerType.HUMANOID,
						Layer.layer()
								.texture(vanillaId("leather"))
								.dyeable(Optional.empty())        // no default color
				)
				// overlay (non-dyeable)
				.addLayer(LayerType.HUMANOID,
						Layer.layer()
								.texture(vanillaId("leather_overlay"))
								.usePlayerTexture(false)
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, armorModel));

		EquipmentModel saddleModel = EquipmentModel.model()
				.addLayer(LayerType.HORSE_BODY,
						Layer.layer()
								.texture(vanillaId("saddle"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, saddleModel));

		EquipmentModel backpack = EquipmentModel.model()
				.addLayerCustom("backpack",
						Layer.layer()
								.texture(myModId("backpack"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, backpack));

		Layer wolfLayer = Layer.layer()
				.texture(myModId("wolf_tag"))
				.usePlayerTexture(false);

		Layer bodyLayer = Layer.layer()
				.texture(myModId("wolf_body"))
				.dyeable(Optional.of(0xCCCCCC));

		EquipmentModel wolfWithTag = EquipmentModel.model()
				.addLayer(LayerType.WOLF_BODY, bodyLayer)
				.addLayerCustom("wolf_tag", wolfLayer);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, wolfWithTag));

		EquipmentModel model1 = EquipmentModel.model()
				.addLayer(LayerType.HORSE_BODY,
						Layer.layer().texture(vanillaId("diamond"))
				)
				.addLayer(LayerType.HUMANOID,
						Layer.layer().texture(vanillaId("diamond"))
				)
				.addLayer(LayerType.HUMANOID_LEGGINGS,
						Layer.layer().texture(vanillaId("diamond"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, model1));

		var baseModel = customId("test", "furniture/aaaaaaaaa");

		Variant variant = new Variant()
				// north
				.put("facing=north,lit=false,occupied=false", BlockState.model(baseModel))
				.put("facing=north,lit=false,occupied=true",  BlockState.model(baseModel))
				.put("facing=north,lit=true,occupied=false",  BlockState.model(baseModel))
				.put("facing=north,lit=true,occupied=true",   BlockState.model(baseModel))
				// east
				.put("facing=east,lit=false,occupied=false",  BlockState.model(baseModel).y(90))
				.put("facing=east,lit=false,occupied=true",   BlockState.model(baseModel).y(90))
				.put("facing=east,lit=true,occupied=false",   BlockState.model(baseModel).y(90))
				.put("facing=east,lit=true,occupied=true",    BlockState.model(baseModel).y(90))
				// south
				.put("facing=south,lit=false,occupied=false", BlockState.model(baseModel).y(180))
				.put("facing=south,lit=false,occupied=true",  BlockState.model(baseModel).y(180))
				.put("facing=south,lit=true,occupied=false",  BlockState.model(baseModel).y(180))
				.put("facing=south,lit=true,occupied=true",   BlockState.model(baseModel).y(180))
				// west
				.put("facing=west,lit=false,occupied=false",  BlockState.model(baseModel).y(270))
				.put("facing=west,lit=false,occupied=true",   BlockState.model(baseModel).y(270))
				.put("facing=west,lit=true,occupied=false",   BlockState.model(baseModel).y(270))
				.put("facing=west,lit=true,occupied=true",    BlockState.model(baseModel).y(270));
		System.out.println(JsonBytes.encodeToPrettyString(BlockState.CODEC, state(variant)));

		{
			// Fence-like: post always, arms when north/east/south/west are true
			Identifier POST = Identifier.tryParse("block/oak_fence_post");
			Identifier ARM  = Identifier.tryParse("block/oak_fence_side");

			BlockState fenceLike = state(Connectables.fenceLike(
					POST, ARM,
					"north", "east", "south", "west",
					true // uvlock
			));
			System.out.println("// Connectables.fenceLikeState");
			System.out.println(JsonBytes.encodeToPrettyString(BlockState.CODEC, fenceLike));
		}

		{
			// Pane-like: optional center + side per direction
			Identifier CENTER = Identifier.tryParse("block/glass_pane_noside");
			Identifier SIDE   = Identifier.tryParse("block/glass_pane_side");
			BlockState paneLike = state(Connectables.paneLike(
					CENTER, SIDE,
					"north", "east", "south", "west",
					true
			));
			System.out.println("// Connectables.paneLike");
			System.out.println(JsonBytes.encodeToPrettyString(BlockState.CODEC, paneLike));
		}

		{
			// Counter-like: top always, edges per side, corners when two sides meet
			Identifier TOP    = Identifier.tryParse("test:block/counter/top");
			Identifier EDGE   = Identifier.tryParse("test:block/counter/edge");
			Identifier INNER  = Identifier.tryParse("test:block/counter/inner_corner");
			Identifier OUTER  = Identifier.tryParse("test:block/counter/outer_corner");

			BlockState counterLike = state(Connectables.counterLike(
					TOP, EDGE, INNER, OUTER,
					"north", "east", "south", "west",
					true
			));
			System.out.println("// Connectables.counterLike");
			System.out.println(JsonBytes.encodeToPrettyString(BlockState.CODEC, counterLike));

			SkyIslandsWorldgen.main(args);

			EnvironmentAttributes environmentAttributes = new EnvironmentAttributes()
					.skyColor("#78a7ff")
					.fogColor("#c0d8ff")
					.cloudColor("#ccffffff")
					.cloudHeight(192.33f)
					.starBrightness(0.65f)
					.skyLightFactor(1.0f)
					.waterEvaporates(false)
					.respawnAnchorWorks(false)
					.netherPortalSpawnsPiglins(true)
					.monstersBurn(true)
					.villagerActivity("minecraft:work");
			EnvironmentAttributes environmentAttributes1 = new EnvironmentAttributes()
					.set(EnvironmentAttributes.FOG_START_DISTANCE, 10.0F)
					.set(EnvironmentAttributes.FOG_END_DISTANCE, 96.0F)
					.set(EnvironmentAttributes.SKY_LIGHT_COLOR, Timelines.NIGHT_SKY_LIGHT_COLOR)
					.set(EnvironmentAttributes.SKY_LIGHT_LEVEL, 4.0F)
					.set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0.0F)
					.set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, -13621215)
					.set(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, vanillaId("pointed_dripstone_lava"))
					.set(EnvironmentAttributes.BED_RULE, "explodes")
					.set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
					.set(EnvironmentAttributes.WATER_EVAPORATES, true)
					.set(EnvironmentAttributes.FAST_LAVA, true)
					.set(EnvironmentAttributes.PIGLINS_ZOMBIFY, false)
					.set(EnvironmentAttributes.CAN_START_RAID, false)
					.set(EnvironmentAttributes.SNOW_GOLEM_MELTS, true);
			System.out.println(JsonBytes.encodeToPrettyString(EnvironmentAttributes.CODEC, environmentAttributes));
			System.out.println(JsonBytes.encodeToPrettyString(WorldClock.CODEC, WorldClock.worldClock()));

			System.out.println(JsonBytes.encodeToPrettyString(DimensionType.CODEC, DimensionType.dimensionType()
					.hasFixedTime(false)
					.hasSkylight(true)
					.hasCeiling(false)
					.coordinateScale(1.0)
					.minY(-64)
					.height(384)
					.logicalHeight(384)
					.infiniburn(BlockTags.INFINIBURN_OVERWORLD)
					.ambientLight(0.0F)
					.monsterSpawnLightUniform(0, 7)
					.monsterSpawnBlockLightLimit(0)
					.skybox(DimensionType.Skybox.OVERWORLD)
					.cardinalLight(DimensionType.CardinalLightType.DEFAULT)
					.attributes(environmentAttributes)
			));
		}
	}
}
