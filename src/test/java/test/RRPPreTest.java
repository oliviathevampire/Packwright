package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.util.ResourceLocationTypeAdapter;
import net.vampirestudios.arrp.assets.blockstates.Connectables;
import net.vampirestudios.arrp.assets.blockstates.BlockState;
import net.vampirestudios.arrp.assets.blockstates.Variant;
import net.vampirestudios.arrp.data.entity.*;
import net.vampirestudios.arrp.assets.equipment.*;
import net.vampirestudios.arrp.assets.item.ItemInfo;
import net.vampirestudios.arrp.assets.item.*;
import net.vampirestudios.arrp.assets.item.*;
import net.vampirestudios.arrp.assets.item.Tint;
import net.vampirestudios.arrp.assets.item.TintConstant;
import net.vampirestudios.arrp.assets.item.TintDye;
import net.vampirestudios.arrp.assets.item.TintTeam;
import net.vampirestudios.arrp.assets.lang.Lang;
import net.vampirestudios.arrp.assets.models.Model;
import net.vampirestudios.arrp.assets.models.Textures;
import net.vampirestudios.arrp.data.recipe.*;
import net.vampirestudios.arrp.data.registry.*;
import net.vampirestudios.arrp.data.worldgen.*;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.vampirestudios.arrp.assets.blockstates.BlockState.*;
import static net.vampirestudios.arrp.data.loot.LootTable.*;
import static net.vampirestudios.arrp.assets.models.Model.*;

public class RRPPreTest {

	private static Identifier id(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	private static Identifier id(String namespace, String path) {
		return Identifier.fromNamespaceAndPath(namespace, path);
	}

	public static void main(String[] args) {
		RuntimeResourcePack pack = RuntimeResourcePack.create("test:test");
		pack.addLang(Identifier.tryParse("aaaa:aaaa"), new Lang().entry("aaaa", "bbbbb"));
		pack.addLang(Identifier.tryParse("modid:en_us"), new Lang().entry("item.custom", "Custom Item"));
		pack.addLang(Identifier.tryParse("modid:es_es"), new Lang().entry("item.custom", "Artículo Personalizado"));
		pack.addLang(Identifier.tryParse("minecraft:en_us"), new Lang()
				.effect(Identifier.withDefaultNamespace("fire_walking"), "Fire Walking")
				.allPotionOf(Identifier.withDefaultNamespace("fire_walking"), "Fire Walking")
		);
		pack.addItemModelInfo(ItemInfo.item().model(ItemModel.select()
				.property(PropertyComponent.component("minecraft:item_name"))
				.addCase(SelectCase.of("Diamond", ModelBasic.of("minecraft:item/diamond")))
				.addCase(SelectCase.of("Netherite Ingot", ModelBasic.of("minecraft:item/netherite_ingot")))
				.addCase(SelectCase.of("Dirt", ModelBasic.of("minecraft:block/dirt")))
				.addCase(SelectCase.of("Coal", ItemModel.rangeDispatch()
						.property(PropertyCount.count())
						.property(PropertyCount.count())
						.entry(RangeEntry.of(10, ModelBasic.of("minecraft:item/charcoal")))
						.fallback(ModelBasic.of("minecraft:item/coal"))
				))
				.fallback(ModelBasic.of("minecraft:block/stone"))), Identifier.fromNamespaceAndPath("test", "test_item"));
		pack.addEquipmentModel(EquipmentModel.model()
				.addLayer(LayerType.HUMANOID, Layer.layer().texture(Identifier.fromNamespaceAndPath("test", "a/test"))),
				Identifier.fromNamespaceAndPath("test", "test_armor")
		);
		pack.addWolfVariant(
				Identifier.fromNamespaceAndPath("mymod", "golden"),
				WolfVariant.wolfVariant()
						.assets(
								Identifier.fromNamespaceAndPath("mymod", "entity/wolf/wolf_golden"),
								Identifier.fromNamespaceAndPath("mymod", "entity/wolf/wolf_golden_tame"),
								Identifier.fromNamespaceAndPath("mymod", "entity/wolf/wolf_golden_angry")
						)
						.spawnConditions(SpawnPrioritySelectors.single(BiomeSpawnCondition.biomeCondition()
								.biome(Identifier.fromNamespaceAndPath("minecraft", "forest")),
								1
						))
		);
		pack.addZombieNautilusVariant(
				Identifier.fromNamespaceAndPath("mymod", "warm_reef"),
				ZombieNautilusVariant.zombieNautilusVariant()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "entity/zombie_nautilus/warm_reef"))
						.model(ZombieNautilusVariant.ModelType.WARM)
						.spawnConditions(SpawnPrioritySelectors.single(BiomeSpawnCondition.biomeCondition()
								.biomeTag(Identifier.fromNamespaceAndPath("minecraft", "is_ocean")),
								1
						))
		);
		pack.addSimpleMobVariant(
				Identifier.fromNamespaceAndPath("minecraft","frog_variant"),
				Identifier.fromNamespaceAndPath("mymod","toxic"),
				SimpleMobVariant.mobVariant()
						.assetId(Identifier.fromNamespaceAndPath("mymod","entity/frog/toxic"))
						.spawnConditions(SpawnPrioritySelectors.single(BiomeSpawnCondition.biomeCondition()
								.biome(Identifier.fromNamespaceAndPath("minecraft", "forest")),
								1
						))
		);
		var spawns = SpawnPrioritySelectors.selectors()
				.add(MoonBrightnessSpawnCondition.moonBrightness().range(MinMaxBounds.Doubles.atMost(0.25f)), 1)
				.freeze();

		pack.addChickenVariant(
				Identifier.fromNamespaceAndPath("mymod", "frost_chicken"),
				ChickenVariant.chickenVariant()
						.model(ChickenVariant.ModelType.COLD)
						.assetId(Identifier.fromNamespaceAndPath("mymod", "entity/chicken/frost"))
						.spawnConditions(spawns)
		);
		pack.addCowVariant(
				Identifier.fromNamespaceAndPath("mymod", "savanna"),
				CowVariant.cowVariant()
						.model(CowVariant.ModelType.WARM)
						.assetId(Identifier.fromNamespaceAndPath("mymod", "entity/cow/savanna"))
		);
		pack.addWolfSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "angryish"),
				WolfSoundVariant.wolfSoundVariant()
						.ambientSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.ambient"))
						.deathSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.death"))
						.growlSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.growl"))
						.hurtSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.hurt"))
						.pantSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.pant"))
						.whineSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.whine"))
		);
		var catSounds = CatSoundVariant.sounds()
				.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.ambient"))
				.strayAmbientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.stray_ambient"))
				.hissSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.hiss"))
				.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.hurt"))
				.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.death"))
				.eatSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.eat"))
				.begForFoodSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.beg_for_food"))
				.purrSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.purr"))
				.purreowSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cat.purreow"));
		pack.addCatSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "bright"),
				CatSoundVariant.catSoundVariant()
						.adultSounds(catSounds)
						.babySounds(catSounds)
		);
		var chickenSounds = ChickenSoundVariant.sounds()
				.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.ambient"))
				.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.hurt"))
				.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.death"))
				.stepSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.step"));
		pack.addChickenSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "clucky"),
				ChickenSoundVariant.chickenSoundVariant()
						.adultSounds(chickenSounds)
						.babySounds(chickenSounds)
		);
		pack.addCowSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "soft"),
				CowSoundVariant.cowSoundVariant()
						.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.ambient"))
						.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.hurt"))
						.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.death"))
						.stepSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.step"))
		);
		var pigSounds = PigSoundVariant.sounds()
				.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.ambient"))
				.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.hurt"))
				.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.death"))
				.stepSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.step"))
				.eatSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.ambient"));
		pack.addPigSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "snuffly"),
				PigSoundVariant.pigSoundVariant()
						.adultSounds(pigSounds)
						.babySounds(pigSounds)
		);
		pack.addPaintingVariant(
				Identifier.fromNamespaceAndPath("mymod", "tiny_sky"),
				PaintingVariant.paintingVariant()
						.size(2, 1)
						.assetId(Identifier.fromNamespaceAndPath("mymod", "tiny_sky"))
						.title("Tiny Sky")
						.author("ARRP")
		);
		pack.addTrimMaterial(
				Identifier.fromNamespaceAndPath("mymod", "copper"),
				TrimMaterial.trimMaterial()
						.assets(TrimMaterial.AssetGroup.assetGroup("copper")
								.overrideArmorAsset(Identifier.fromNamespaceAndPath("mymod", "test_armor"), "copper_test"))
						.description("Copper")
		);
		pack.addTrimPattern(
				Identifier.fromNamespaceAndPath("mymod", "sunburst"),
				TrimPattern.trimPattern()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "sunburst"))
						.description("Sunburst Armor Trim")
						.decal(false)
		);
		pack.addBannerPattern(
				Identifier.fromNamespaceAndPath("mymod", "spark"),
				BannerPattern.bannerPattern()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "spark"))
						.translationKey("block.mymod.banner.spark")
		);
		pack.addDecoratedPotPattern(
				Identifier.fromNamespaceAndPath("mymod", "spiral"),
				DecoratedPotPattern.decoratedPotPattern()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "spiral_pottery_pattern"))
		);
		pack.addDamageType(
				Identifier.fromNamespaceAndPath("mymod", "spark"),
				DamageType.damageType()
						.messageId("spark")
						.scaling(DamageType.Scaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER)
						.exhaustion(0.1F)
						.effects(DamageType.Effects.HURT)
		);
		pack.addInstrument(
				Identifier.fromNamespaceAndPath("mymod", "small_horn"),
				Instrument.instrument()
						.soundEvent(Identifier.fromNamespaceAndPath("minecraft", "item.goat_horn.sound.0"))
						.useDuration(7.0F)
						.range(64.0F)
						.description("Small Horn")
		);
		pack.addJukeboxSong(
				Identifier.fromNamespaceAndPath("mymod", "test_song"),
				JukeboxSong.jukeboxSong()
						.soundEvent(Identifier.fromNamespaceAndPath("mymod", "music.test_song"))
						.description("Test Song")
						.lengthInSeconds(120.0F)
						.comparatorOutput(7)
		);
		pack.addConfiguredCarver(
				Identifier.fromNamespaceAndPath("mymod", "small_cave"),
				ConfiguredCarver.carver()
						.type("minecraft:cave")
						.probability(0.05F)
						.replaceable("#minecraft:overworld_carver_replaceables")
		);
		pack.addConfiguredCarver(
				Identifier.fromNamespaceAndPath("mymod", "canyon"),
				ConfiguredCarver.canyon()
						.config(ConfiguredCarver.Config.config()
								.debugSettings(ConfiguredCarver.DebugSettings.debugSettings()
										.airState(BlockState.blockState("minecraft:warped_button")
												.property("face", "wall")
												.property("facing", "north")
												.property("powered", false))
										.barrierState(BlockState.blockState("minecraft:glass"))
										.lavaState(BlockState.blockState("minecraft:orange_stained_glass"))
										.waterState(BlockState.blockState("minecraft:candle")
												.property("candles", 1)
												.property("lit", false)
												.property("waterlogged", false)))
								.lavaLevel(VerticalAnchor.aboveBottom(8))
								.probability(0.01F)
								.replaceable("#minecraft:overworld_carver_replaceables")
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
				Identifier.fromNamespaceAndPath("mymod", "mossy_replace"),
				ProcessorList.processorList()
						.rule(ProcessorList.Rule.replace("minecraft:cobblestone", "minecraft:mossy_cobblestone"))
		);
		pack.addTemplatePool(
				Identifier.fromNamespaceAndPath("mymod", "test_pool"),
				TemplatePool.pool()
						.fallback("minecraft:empty")
						.single("mymod:test/start", "mymod:mossy_replace", TemplatePool.Projection.RIGID, 1)
		);
		pack.addWorldPreset(
				Identifier.fromNamespaceAndPath("mymod", "single_biome"),
				WorldPreset.preset()
						.overworld(WorldPreset.Dimension.dimension()
								.type("minecraft:overworld")
								.noiseGenerator("minecraft:overworld", WorldPreset.Generator.BiomeSource.fixed("minecraft:plains")))
		);
		pack.addFlatLevelGeneratorPreset(
				Identifier.fromNamespaceAndPath("mymod", "copper_flat"),
				FlatLevelGeneratorPreset.preset()
						.display("minecraft:copper_block")
						.biome("minecraft:plains")
						.layer("minecraft:bedrock", 1)
						.layer("minecraft:stone", 3)
						.layer("minecraft:grass_block", 1)
		);
		pack.addVillagerTrade(
				Identifier.fromNamespaceAndPath("mymod", "toolsmith/1/copper_for_emeralds"),
				VillagerTrade.trade()
						.wants("minecraft:emerald", 3)
						.gives("minecraft:copper_ingot", 8)
						.maxUses(12)
						.xp(2)
		);
		pack.addTradeSet(
				Identifier.fromNamespaceAndPath("mymod", "toolsmith_copper"),
				TradeSet.tradeSet()
						.trade("mymod:toolsmith/1/copper_for_emeralds")
						.amount(TradeSet.NumberProvider.uniform(1, 2))
						.allowDuplicates(false)
		);
		pack.addDialog(
				Identifier.fromNamespaceAndPath("mymod", "welcome"),
				Dialog.notice("Welcome", "Continue")
						.plainMessage("This dialog was generated by ARRP.")
						.input(Dialog.Input.text("name", "Name"))
		);
		pack.addWorldClock(
				Identifier.fromNamespaceAndPath("mymod", "example_clock"),
				WorldClock.worldClock()
		);
		pack.addItemModelInfo(
				ItemInfo.item().model(ItemModel.model("test:block/model").tint(Tint.dye(0xFFFFFF))),
				Identifier.fromNamespaceAndPath("mymod", "test_block")
		);

		pack.addRecipe(
				Identifier.fromNamespaceAndPath("example", "diamond_pickaxe_to_block"),
				Recipe.shapeless(
						Ingredients.ingredients().addFabricCustom(
								Identifier.withDefaultNamespace("diamond_pickaxe"),
								components -> components.addProperty("minecraft:damage", 0)
						),
						Result.result(Identifier.withDefaultNamespace("diamond_block"))
				).group("test").category("misc")
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath("example", "pumpkin"),
				Recipe.shaped(
						Pattern.pattern("PPP", "P P", "PPP"),
						Keys.keys().item("P", Identifier.withDefaultNamespace("pumpkin_pie")),
						Result.stackedResult(Identifier.withDefaultNamespace("pumpkin"), 3)
				)
		);
		pack.addRecipe(
				Identifier.fromNamespaceAndPath("example", "golden_sword"),
				Recipe.shapeless(
						Ingredients.ingredients()
								.addItem(Identifier.withDefaultNamespace("stick"))
								.addItem(Identifier.withDefaultNamespace("gold_ingot"))
								.addItem(Identifier.withDefaultNamespace("gold_ingot"))
								.addItem(Identifier.withDefaultNamespace("gold_ingot")),
						Result.result(Identifier.withDefaultNamespace("golden_sword"))
								.components(builder -> {
									builder.addProperty("minecraft:damage", 3);
									builder.addProperty("minecraft:rarity", "RARE");
								})
				)
		);
		pack.dumpDirect(Path.of("aaaa"));

		BlockState iron_block = state(variant(BlockState.model(id("block/iron_block"))));
		BlockState oak_fence = state(multipart(BlockState.model(id("block/oak_fence_post"))),
				multipart(BlockState.model(id("block/oak_fence_side")).uvlock()).when(when().isTrue("north")),
				multipart(BlockState.model(id("block/oak_fence_side")).y(90).uvlock()).when(when().isTrue("east")),
				multipart(BlockState.model(id("block/oak_fence_side")).y(180).uvlock()).when(when().isTrue("south")),
				multipart(BlockState.model(id("block/oak_fence_side")).y(270).uvlock()).when(when().isTrue("west")));

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

		Lang lang = Lang.lang().allPotionOf(Identifier.fromNamespaceAndPath("mod_id", "potion_id"), "Example");

		System.out.println(RuntimeResourcePackImpl.GSON.toJson(loot("minecraft:block").pool(pool().rolls(1)
				.entry(entry().type("minecraft:item").name("minecraft:diamond"))
				.condition(predicate("minecraft:survives_explosion")))));
		System.out.println(gson.toJson(iron_block));
		System.out.println(gson.toJson(oak_fence));
		System.out.println(gson.toJson(model));

		System.out.println(gson.toJson(lang));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ModelBasic.of("minecraft:item/template_spawn_egg")
				.tint(new TintConstant(-278045))
				.tint(new TintConstant(-5886604))
		)));


		ItemInfo itemInfo = ItemInfo.item().model(
				ItemModel.model("minecraft:item/template_spawn_egg")
						.tints(Tint.constant(-278045), Tint.constant(-5886604))
		).handAnimationOnSwap(true);
		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, itemInfo));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.model("minecraft:item/bamboo"))));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.rangeDispatch()
				.property(PropertyUseCycle.useCycle().period(10f))
				.scale(0.1)
				.entry(RangeEntry.of(0.25, ModelBasic.of("minecraft:item/brush_brushing_0")))
				.entry(RangeEntry.of(0.5, ModelBasic.of("minecraft:item/brush_brushing_1")))
				.entry(RangeEntry.of(0.75, ModelBasic.of("minecraft:item/brush_brushing_2")))
				.fallback(ModelBasic.of("minecraft:item/brush"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.condition()
				.property(new PropertyUsingItem())
				.onTrue(ModelBasic.of("minecraft:item/using_model"))
				.onFalse(ModelBasic.of("minecraft:item/default_model"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.composite()
				.model(ModelBasic.of("minecraft:item/part1"))
				.model(ModelBasic.of("minecraft:item/part2"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.rangeDispatch()
				.property(PropertyDamage.of(true))
				.entry(RangeEntry.of(0.25, ModelBasic.of("minecraft:item/damage_low")))
				.entry(RangeEntry.of(0.5, ModelBasic.of("minecraft:item/damage_medium")))
				.entry(RangeEntry.of(0.75, ModelBasic.of("minecraft:item/damage_high")))
				.fallback(ModelBasic.of("minecraft:item/damage_full"))
		)));

//		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ModelSpecial.head()
//				.kind(SkullBlock.Type.DRAGON)
//				.animation(0.5f)
//		).handAnimationOnSwap(false)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ItemModel.condition()
				.property(new PropertyKeybindDown("key.sneak"))
				.onTrue(ModelBasic.of("minecraft:item/sneaking_model"))
				.onFalse(ModelBasic.of("minecraft:item/normal_model"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(ModelBasic
				.of("minecraft:item/team_colored_item")
				.tint(TintTeam.of(-1))
		)));


		// Create range dispatch entries
		List<RangeEntry> entries = new ArrayList<>();
		for (int i = 0; i <= 31; i++) {
			entries.add(new RangeEntry(
					i / 32.0, // normalized threshold
					ItemModel.model("minecraft:item/compass_" + (i < 10 ? "0" : "") + i)
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
				.addCase(SelectCase.of("minecraft:overworld", ItemModel.rangeDispatch()
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
				.addCase(SelectCase.of("Diamond", ModelBasic.of("minecraft:item/diamond")))
				.addCase(SelectCase.of("Netherite Ingot", ModelBasic.of("minecraft:item/netherite_ingot")))
				.addCase(SelectCase.of("Dirt", ModelBasic.of("minecraft:block/dirt")))
				.addCase(SelectCase.of("Coal", ItemModel.rangeDispatch()
						.property(PropertyCount.count())
						.entry(RangeEntry.of(10, ModelBasic.of("minecraft:item/charcoal")))
						.fallback(ModelBasic.of("minecraft:item/coal"))
				))
				.fallback(ModelBasic.of("minecraft:block/stone"));
		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(selectModel)));

		selectModel = ItemModel.select()
				.property(componentProperty)
				.addCase(SelectCase.of("Diamond", ModelBasic.of("minecraft:item/diamond")))
				.addCase(SelectCase.of("Netherite Ingot", ModelBasic.of("minecraft:item/netherite_ingot")))
				.addCase(SelectCase.of("Dirt", ModelBasic.of("minecraft:block/dirt")))
				.addCase(SelectCase.of("Coal", ItemModel.rangeDispatch()
						.property(PropertyCount.count())
						.entry(RangeEntry.of(10, ModelBasic.of("minecraft:item/charcoal")))
						.fallback(ModelBasic.of("minecraft:item/coal"))
				))
				.fallback(ModelBasic.of("minecraft:block/stone").tint(new TintDye(0xFF00FF)));
		System.out.println(JsonBytes.encodeToPrettyString(ItemInfo.CODEC, ItemInfo.item().model(selectModel)));

		EquipmentModel eq = EquipmentModel.model()
				.addLayer("wings",
						Layer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "elytra"))
								.usePlayerTexture(true)
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, eq));

		EquipmentModel armorModel = EquipmentModel.model()
				// leather leggings, dyeable with default color
				.addLayer(LayerType.HUMANOID_LEGGINGS,
						Layer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "leather_leggings"))
								.dyeable(Optional.of(0xA06540))   // default brown if undyed
				)
				// main body layer, dyeable
				.addLayer(LayerType.HUMANOID,
						Layer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "leather"))
								.dyeable(Optional.empty())        // no default color
				)
				// overlay (non-dyeable)
				.addLayer(LayerType.HUMANOID,
						Layer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "leather_overlay"))
								.usePlayerTexture(false)
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, armorModel));

		EquipmentModel saddleModel = EquipmentModel.model()
				.addLayer(LayerType.HORSE_BODY,
						Layer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "saddle"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, saddleModel));

		EquipmentModel backpack = EquipmentModel.model()
				.addLayerCustom("backpack",
						Layer.layer()
								.texture(Identifier.fromNamespaceAndPath("mymod", "backpack"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, backpack));

		Layer wolfLayer = Layer.layer()
				.texture(Identifier.fromNamespaceAndPath("mymod", "wolf_tag"))
				.usePlayerTexture(false);

		Layer bodyLayer = Layer.layer()
				.texture(Identifier.fromNamespaceAndPath("mymod", "wolf_body"))
				.dyeable(Optional.of(0xCCCCCC));

		EquipmentModel wolfWithTag = EquipmentModel.model()
				.addLayer(LayerType.WOLF_BODY, bodyLayer)
				.addLayerCustom("wolf_tag", wolfLayer);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, wolfWithTag));

		EquipmentModel model1 = EquipmentModel.model()
				.addLayer(LayerType.HORSE_BODY,
						Layer.layer().texture(Identifier.fromNamespaceAndPath("minecraft", "diamond"))
				)
				.addLayer(LayerType.HUMANOID,
						Layer.layer().texture(Identifier.fromNamespaceAndPath("minecraft", "diamond"))
				)
				.addLayer(LayerType.HUMANOID_LEGGINGS,
						Layer.layer().texture(Identifier.fromNamespaceAndPath("minecraft", "diamond"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(EquipmentModel.CODEC, model1));

		var baseModel = id("test", "furniture/aaaaaaaaa");

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
//					.attribute("minecraft:gameplay/bed_rule", BedRule.CODEC, BedRule.CAN_SLEEP_WHEN_DARK)
//					.attribute("minecraft:audio/background_music", BackgroundMusic.CODEC, BackgroundMusic.OVERWORLD)
//					.attribute("minecraft:audio/ambient_sounds", AmbientSounds.CODEC, AmbientSounds.LEGACY_CAVE_SETTINGS)
			));
		}
	}
}
