package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.json.JsonBytes;
import net.vampirestudios.arrp.json.ResourceLocationTypeAdapter;
import net.vampirestudios.arrp.json.blockstate.Connectables;
import net.vampirestudios.arrp.json.blockstate.JState;
import net.vampirestudios.arrp.json.blockstate.JVariant;
import net.vampirestudios.arrp.json.entityVariants.*;
import net.vampirestudios.arrp.json.equipmentinfo.JEquipmentModel;
import net.vampirestudios.arrp.json.equipmentinfo.JLayer;
import net.vampirestudios.arrp.json.equipmentinfo.JTrimMaterial;
import net.vampirestudios.arrp.json.equipmentinfo.JTrimPattern;
import net.vampirestudios.arrp.json.equipmentinfo.LayerType;
import net.vampirestudios.arrp.json.iteminfo.JItemInfo;
import net.vampirestudios.arrp.json.iteminfo.model.*;
import net.vampirestudios.arrp.json.iteminfo.property.*;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;
import net.vampirestudios.arrp.json.iteminfo.tint.JTintConstant;
import net.vampirestudios.arrp.json.iteminfo.tint.JTintDye;
import net.vampirestudios.arrp.json.iteminfo.tint.JTintTeam;
import net.vampirestudios.arrp.json.lang.JLang;
import net.vampirestudios.arrp.json.models.JModel;
import net.vampirestudios.arrp.json.models.JTextures;
import net.vampirestudios.arrp.json.registry.*;
import net.vampirestudios.arrp.json.worldgen.*;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimensionType;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.vampirestudios.arrp.json.blockstate.JState.*;
import static net.vampirestudios.arrp.json.loot.JLootTable.*;
import static net.vampirestudios.arrp.json.models.JModel.*;

public class RRPPreTest {

	private static Identifier id(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	private static Identifier id(String namespace, String path) {
		return Identifier.fromNamespaceAndPath(namespace, path);
	}

	public static void main(String[] args) {
		RuntimeResourcePack pack = RuntimeResourcePack.create("test:test");
		pack.addLang(Identifier.tryParse("aaaa:aaaa"), new JLang().entry("aaaa", "bbbbb"));
		pack.addLang(Identifier.tryParse("modid:lang/en_us"), new JLang().entry("item.custom", "Custom Item"));
		pack.addLang(Identifier.tryParse("modid:lang/es_es"), new JLang().entry("item.custom", "Artículo Personalizado"));
		pack.addItemModelInfo(JItemInfo.item().model(JItemModel.select()
				.property(JPropertyComponent.component("minecraft:item_name"))
				.addCase(JSelectCase.of("Diamond", JModelBasic.of("minecraft:item/diamond")))
				.addCase(JSelectCase.of("Netherite Ingot", JModelBasic.of("minecraft:item/netherite_ingot")))
				.addCase(JSelectCase.of("Dirt", JModelBasic.of("minecraft:block/dirt")))
				.addCase(JSelectCase.of("Coal", JItemModel.rangeDispatch()
						.property(JPropertyCount.count())
						.property(JPropertyCount.count())
						.entry(JRangeEntry.of(10, JModelBasic.of("minecraft:item/charcoal")))
						.fallback(JModelBasic.of("minecraft:item/coal"))
				))
				.fallback(JModelBasic.of("minecraft:block/stone"))), Identifier.fromNamespaceAndPath("test", "test_item"));
		pack.addEquipmentModel(JEquipmentModel.model()
				.addLayer(LayerType.HUMANOID, JLayer.layer().texture(Identifier.fromNamespaceAndPath("test", "a/test"))),
				Identifier.fromNamespaceAndPath("test", "test_armor")
		);
		pack.addWolfVariant(
				Identifier.fromNamespaceAndPath("mymod", "golden"),
				JWolfVariant.wolfVariant()
						.assets(
								Identifier.fromNamespaceAndPath("mymod", "entity/wolf/wolf_golden"),
								Identifier.fromNamespaceAndPath("mymod", "entity/wolf/wolf_golden_tame"),
								Identifier.fromNamespaceAndPath("mymod", "entity/wolf/wolf_golden_angry")
						)
						.spawnConditions(JSpawnPrioritySelectors.single(JBiomeSpawnCondition.biomeCondition()
								.biome(Identifier.fromNamespaceAndPath("minecraft", "forest")),
								1
						))
		);
		pack.addZombieNautilusVariant(
				Identifier.fromNamespaceAndPath("mymod", "warm_reef"),
				JZombieNautilusVariant.zombieNautilusVariant()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "entity/zombie_nautilus/warm_reef"))
						.model(JZombieNautilusVariant.ModelType.WARM)
						.spawnConditions(JSpawnPrioritySelectors.single(JBiomeSpawnCondition.biomeCondition()
								.biomeTag(Identifier.fromNamespaceAndPath("minecraft", "is_ocean")),
								1
						))
		);
		pack.addSimpleMobVariant(
				Identifier.fromNamespaceAndPath("minecraft","frog_variant"),
				Identifier.fromNamespaceAndPath("mymod","toxic"),
				JSimpleMobVariant.mobVariant()
						.assetId(Identifier.fromNamespaceAndPath("mymod","entity/frog/toxic"))
						.spawnConditions(JSpawnPrioritySelectors.single(JBiomeSpawnCondition.biomeCondition()
								.biome(Identifier.fromNamespaceAndPath("minecraft", "forest")),
								1
						))
		);
		var spawns = JSpawnPrioritySelectors.selectors()
				.add(JMoonBrightnessSpawnCondition.moonBrightness().range(MinMaxBounds.Doubles.atMost(0.25f)), 1)
				.freeze();

		pack.addChickenVariant(
				Identifier.fromNamespaceAndPath("mymod", "frost_chicken"),
				JChickenVariant.chickenVariant()
						.model(JChickenVariant.ModelType.COLD)
						.assetId(Identifier.fromNamespaceAndPath("mymod", "entity/chicken/frost"))
						.spawnConditions(spawns)
		);
		pack.addCowVariant(
				Identifier.fromNamespaceAndPath("mymod", "savanna"),
				JCowVariant.cowVariant()
						.model(JCowVariant.ModelType.WARM)
						.assetId(Identifier.fromNamespaceAndPath("mymod", "entity/cow/savanna"))
		);
		pack.addWolfSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "angryish"),
				JWolfSoundVariant.wolfSoundVariant()
						.ambientSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.ambient"))
						.deathSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.death"))
						.growlSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.growl"))
						.hurtSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.hurt"))
						.pantSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.pant"))
						.whineSound(Identifier.fromNamespaceAndPath("minecraft","entity.wolf_angry.whine"))
		);
		var catSounds = JCatSoundVariant.sounds()
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
				JCatSoundVariant.catSoundVariant()
						.adultSounds(catSounds)
						.babySounds(catSounds)
		);
		var chickenSounds = JChickenSoundVariant.sounds()
				.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.ambient"))
				.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.hurt"))
				.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.death"))
				.stepSound(Identifier.fromNamespaceAndPath("minecraft", "entity.chicken.step"));
		pack.addChickenSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "clucky"),
				JChickenSoundVariant.chickenSoundVariant()
						.adultSounds(chickenSounds)
						.babySounds(chickenSounds)
		);
		pack.addCowSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "soft"),
				JCowSoundVariant.cowSoundVariant()
						.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.ambient"))
						.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.hurt"))
						.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.death"))
						.stepSound(Identifier.fromNamespaceAndPath("minecraft", "entity.cow.step"))
		);
		var pigSounds = JPigSoundVariant.sounds()
				.ambientSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.ambient"))
				.hurtSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.hurt"))
				.deathSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.death"))
				.stepSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.step"))
				.eatSound(Identifier.fromNamespaceAndPath("minecraft", "entity.pig.ambient"));
		pack.addPigSoundVariant(
				Identifier.fromNamespaceAndPath("mymod", "snuffly"),
				JPigSoundVariant.pigSoundVariant()
						.adultSounds(pigSounds)
						.babySounds(pigSounds)
		);
		pack.addPaintingVariant(
				Identifier.fromNamespaceAndPath("mymod", "tiny_sky"),
				JPaintingVariant.paintingVariant()
						.size(2, 1)
						.assetId(Identifier.fromNamespaceAndPath("mymod", "tiny_sky"))
						.title("Tiny Sky")
						.author("ARRP")
		);
		pack.addTrimMaterial(
				Identifier.fromNamespaceAndPath("mymod", "copper"),
				JTrimMaterial.trimMaterial()
						.assets(JTrimMaterial.AssetGroup.assetGroup("copper")
								.overrideArmorAsset(Identifier.fromNamespaceAndPath("mymod", "test_armor"), "copper_test"))
						.description("Copper")
		);
		pack.addTrimPattern(
				Identifier.fromNamespaceAndPath("mymod", "sunburst"),
				JTrimPattern.trimPattern()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "sunburst"))
						.description("Sunburst Armor Trim")
						.decal(false)
		);
		pack.addBannerPattern(
				Identifier.fromNamespaceAndPath("mymod", "spark"),
				JBannerPattern.bannerPattern()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "spark"))
						.translationKey("block.mymod.banner.spark")
		);
		pack.addDecoratedPotPattern(
				Identifier.fromNamespaceAndPath("mymod", "spiral"),
				JDecoratedPotPattern.decoratedPotPattern()
						.assetId(Identifier.fromNamespaceAndPath("mymod", "spiral_pottery_pattern"))
		);
		pack.addDamageType(
				Identifier.fromNamespaceAndPath("mymod", "spark"),
				JDamageType.damageType()
						.messageId("spark")
						.scaling(JDamageType.Scaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER)
						.exhaustion(0.1F)
						.effects(JDamageType.Effects.HURT)
		);
		pack.addInstrument(
				Identifier.fromNamespaceAndPath("mymod", "small_horn"),
				JInstrument.instrument()
						.soundEvent(Identifier.fromNamespaceAndPath("minecraft", "item.goat_horn.sound.0"))
						.useDuration(7.0F)
						.range(64.0F)
						.description("Small Horn")
		);
		pack.addJukeboxSong(
				Identifier.fromNamespaceAndPath("mymod", "test_song"),
				JJukeboxSong.jukeboxSong()
						.soundEvent(Identifier.fromNamespaceAndPath("mymod", "music.test_song"))
						.description("Test Song")
						.lengthInSeconds(120.0F)
						.comparatorOutput(7)
		);
		pack.addConfiguredCarver(
				Identifier.fromNamespaceAndPath("mymod", "small_cave"),
				JConfiguredCarver.carver()
						.type("minecraft:cave")
						.probability(0.05F)
						.replaceable("#minecraft:overworld_carver_replaceables")
		);
		pack.addConfiguredCarver(
				Identifier.fromNamespaceAndPath("mymod", "canyon"),
				JConfiguredCarver.canyon()
						.config(JConfiguredCarver.Config.config()
								.debugSettings(JConfiguredCarver.DebugSettings.debugSettings()
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
								.shape(JConfiguredCarver.CanyonShape.canyonShape()
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
				JProcessorList.processorList()
						.rule(JProcessorList.Rule.replace("minecraft:cobblestone", "minecraft:mossy_cobblestone"))
		);
		pack.addTemplatePool(
				Identifier.fromNamespaceAndPath("mymod", "test_pool"),
				JTemplatePool.pool()
						.fallback("minecraft:empty")
						.single("mymod:test/start", "mymod:mossy_replace", JTemplatePool.Projection.RIGID, 1)
		);
		pack.addWorldPreset(
				Identifier.fromNamespaceAndPath("mymod", "single_biome"),
				JWorldPreset.preset()
						.overworld(JWorldPreset.Dimension.dimension()
								.type("minecraft:overworld")
								.noiseGenerator("minecraft:overworld", JWorldPreset.Generator.BiomeSource.fixed("minecraft:plains")))
		);
		pack.addFlatLevelGeneratorPreset(
				Identifier.fromNamespaceAndPath("mymod", "copper_flat"),
				JFlatLevelGeneratorPreset.preset()
						.display("minecraft:copper_block")
						.biome("minecraft:plains")
						.layer("minecraft:bedrock", 1)
						.layer("minecraft:stone", 3)
						.layer("minecraft:grass_block", 1)
		);
		pack.addVillagerTrade(
				Identifier.fromNamespaceAndPath("mymod", "toolsmith/1/copper_for_emeralds"),
				JVillagerTrade.trade()
						.wants("minecraft:emerald", 3)
						.gives("minecraft:copper_ingot", 8)
						.maxUses(12)
						.xp(2)
		);
		pack.addTradeSet(
				Identifier.fromNamespaceAndPath("mymod", "toolsmith_copper"),
				JTradeSet.tradeSet()
						.trade("mymod:toolsmith/1/copper_for_emeralds")
						.amount(JTradeSet.NumberProvider.uniform(1, 2))
						.allowDuplicates(false)
		);
		pack.addDialog(
				Identifier.fromNamespaceAndPath("mymod", "welcome"),
				JDialog.notice("Welcome", "Continue")
						.plainMessage("This dialog was generated by ARRP.")
						.input(JDialog.Input.text("name", "Name"))
		);
		pack.addWorldClock(
				Identifier.fromNamespaceAndPath("mymod", "example_clock"),
				JWorldClock.worldClock()
		);
		pack.addItemModelInfo(
				JItemInfo.item().model(JItemModel.model("test:block/model").tint(JTint.dye(0xFFFFFF))),
				Identifier.fromNamespaceAndPath("mymod", "test_block")
		);
		pack.dumpDirect(Path.of("aaaa"));

		JState iron_block = state(variant(JState.model(id("block/iron_block"))));
		JState oak_fence = state(multipart(JState.model(id("block/oak_fence_post"))),
				multipart(JState.model(id("block/oak_fence_side")).uvlock()).when(when().isTrue("north")),
				multipart(JState.model(id("block/oak_fence_side")).y(90).uvlock()).when(when().isTrue("east")),
				multipart(JState.model(id("block/oak_fence_side")).y(180).uvlock()).when(when().isTrue("south")),
				multipart(JState.model(id("block/oak_fence_side")).y(270).uvlock()).when(when().isTrue("west")));

		JModel model = JModel.model().textures(JModel.textures().var("all", "block/bamboo_stalk").particle("block/bamboo_stalk"))
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
				.registerTypeAdapter(JTextures.class, new JTextures.Serializer())
				.registerTypeAdapter(Identifier.class, new ResourceLocationTypeAdapter())
				.setPrettyPrinting()
				.create();

		JLang lang = JLang.lang().allPotionOf(Identifier.fromNamespaceAndPath("mod_id", "potion_id"), "Example");

		System.out.println(RuntimeResourcePackImpl.GSON.toJson(loot("minecraft:block").pool(pool().rolls(1)
				.entry(entry().type("minecraft:item").name("minecraft:diamond"))
				.condition(predicate("minecraft:survives_explosion")))));
		System.out.println(gson.toJson(iron_block));
		System.out.println(gson.toJson(oak_fence));
		System.out.println(gson.toJson(model));

		System.out.println(gson.toJson(lang));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JModelBasic.of("minecraft:item/template_spawn_egg")
				.tint(new JTintConstant(-278045))
				.tint(new JTintConstant(-5886604))
		)));


		JItemInfo itemInfo = JItemInfo.item().model(
				JItemModel.model("minecraft:item/template_spawn_egg")
						.tints(JTint.constant(-278045), JTint.constant(-5886604))
		).handAnimationOnSwap(true);
		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, itemInfo));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JItemModel.model("minecraft:item/bamboo"))));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JItemModel.rangeDispatch()
				.property(JPropertyUseCycle.useCycle().period(10f))
				.scale(0.1)
				.entry(JRangeEntry.of(0.25, JModelBasic.of("minecraft:item/brush_brushing_0")))
				.entry(JRangeEntry.of(0.5, JModelBasic.of("minecraft:item/brush_brushing_1")))
				.entry(JRangeEntry.of(0.75, JModelBasic.of("minecraft:item/brush_brushing_2")))
				.fallback(JModelBasic.of("minecraft:item/brush"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JItemModel.condition()
				.property(new JPropertyUsingItem())
				.onTrue(JModelBasic.of("minecraft:item/using_model"))
				.onFalse(JModelBasic.of("minecraft:item/default_model"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JItemModel.composite()
				.model(JModelBasic.of("minecraft:item/part1"))
				.model(JModelBasic.of("minecraft:item/part2"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JItemModel.rangeDispatch()
				.property(JPropertyDamage.of(true))
				.entry(JRangeEntry.of(0.25, JModelBasic.of("minecraft:item/damage_low")))
				.entry(JRangeEntry.of(0.5, JModelBasic.of("minecraft:item/damage_medium")))
				.entry(JRangeEntry.of(0.75, JModelBasic.of("minecraft:item/damage_high")))
				.fallback(JModelBasic.of("minecraft:item/damage_full"))
		)));

//		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JModelSpecial.head()
//				.kind(SkullBlock.Type.DRAGON)
//				.animation(0.5f)
//		).handAnimationOnSwap(false)));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JItemModel.condition()
				.property(new JPropertyKeybindDown("key.sneak"))
				.onTrue(JModelBasic.of("minecraft:item/sneaking_model"))
				.onFalse(JModelBasic.of("minecraft:item/normal_model"))
		)));

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(JModelBasic
				.of("minecraft:item/team_colored_item")
				.tint(JTintTeam.of(-1))
		)));


		// Create range dispatch entries
		List<JRangeEntry> entries = new ArrayList<>();
		for (int i = 0; i <= 31; i++) {
			entries.add(new JRangeEntry(
					i / 32.0, // normalized threshold
					JItemModel.model("minecraft:item/compass_" + (i < 10 ? "0" : "") + i)
			));
		}

		// Create range dispatch for "on_true"
		JModelRangeDispatch onTrueDispatch = JItemModel.rangeDispatch()
				.property(new JPropertyCompass().target("lodestone"))
				.scale(32.0)
				.entries(entries);

		// Create range dispatch for fallback
		JModelRangeDispatch fallbackDispatch = JItemModel.rangeDispatch()
				.property(new JPropertyCompass().target("none"))
				.scale(32.0)
				.entries(entries);

		// Create select model for "on_false"
		JItemModel onFalseSelect = JItemModel.select()
				.property(new JPropertyContextDimension())
				.addCase(JSelectCase.of("minecraft:overworld", JItemModel.rangeDispatch()
						.property(new JPropertyCompass().target("spawn"))
						.scale(32.0)
						.entries(entries)
				)).fallback(fallbackDispatch);

		// Create the full condition model
		JModelCondition compassModel = JItemModel.condition()
				.property(JPropertyHasComponent.of("minecraft:lodestone_tracker"))
				.onTrue(onTrueDispatch)
				.onFalse(onFalseSelect);

		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(compassModel)));

		// Create the condition property
		JPropertyUsingItem usingItemProperty = new JPropertyUsingItem();

		// Create the nested shield model
//		JItemModel shieldNestedModel = JModelSpecial.shield();
//
//		// Create the on_false model (not blocking)
//		JModelSpecial onFalseModel = JModelSpecial.specialModel("minecraft:item/shield", shieldNestedModel);
//
//		// Create the on_true model (blocking)
//		JModelSpecial onTrueModel = JModelSpecial.specialModel("minecraft:item/shield_blocking", shieldNestedModel);
//
//		// Create the condition model
//		JModelCondition conditionModel = JItemModel.condition()
//				.property(usingItemProperty)
//				.onFalse(onFalseModel)
//				.onTrue(onTrueModel);
//
//		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(conditionModel)));

		JPropertyComponent componentProperty = JPropertyComponent.component("minecraft:item_name");

		// Build the select model with cases
		JItemModel selectModel = JItemModel.select()
				.property(componentProperty)
				.addCase(JSelectCase.of("Diamond", JModelBasic.of("minecraft:item/diamond")))
				.addCase(JSelectCase.of("Netherite Ingot", JModelBasic.of("minecraft:item/netherite_ingot")))
				.addCase(JSelectCase.of("Dirt", JModelBasic.of("minecraft:block/dirt")))
				.addCase(JSelectCase.of("Coal", JItemModel.rangeDispatch()
						.property(JPropertyCount.count())
						.entry(JRangeEntry.of(10, JModelBasic.of("minecraft:item/charcoal")))
						.fallback(JModelBasic.of("minecraft:item/coal"))
				))
				.fallback(JModelBasic.of("minecraft:block/stone"));
		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(selectModel)));

		selectModel = JItemModel.select()
				.property(componentProperty)
				.addCase(JSelectCase.of("Diamond", JModelBasic.of("minecraft:item/diamond")))
				.addCase(JSelectCase.of("Netherite Ingot", JModelBasic.of("minecraft:item/netherite_ingot")))
				.addCase(JSelectCase.of("Dirt", JModelBasic.of("minecraft:block/dirt")))
				.addCase(JSelectCase.of("Coal", JItemModel.rangeDispatch()
						.property(JPropertyCount.count())
						.entry(JRangeEntry.of(10, JModelBasic.of("minecraft:item/charcoal")))
						.fallback(JModelBasic.of("minecraft:item/coal"))
				))
				.fallback(JModelBasic.of("minecraft:block/stone").tint(new JTintDye(0xFF00FF)));
		System.out.println(JsonBytes.encodeToPrettyString(JItemInfo.CODEC, JItemInfo.item().model(selectModel)));

		JEquipmentModel eq = JEquipmentModel.model()
				.addLayer("wings",
						JLayer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "elytra"))
								.usePlayerTexture(true)
				);
		System.out.println(JsonBytes.encodeToPrettyString(JEquipmentModel.CODEC, eq));

		JEquipmentModel armorModel = JEquipmentModel.model()
				// leather leggings, dyeable with default color
				.addLayer(LayerType.HUMANOID_LEGGINGS,
						JLayer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "leather_leggings"))
								.dyeable(Optional.of(0xA06540))   // default brown if undyed
				)
				// main body layer, dyeable
				.addLayer(LayerType.HUMANOID,
						JLayer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "leather"))
								.dyeable(Optional.empty())        // no default color
				)
				// overlay (non-dyeable)
				.addLayer(LayerType.HUMANOID,
						JLayer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "leather_overlay"))
								.usePlayerTexture(false)
				);
		System.out.println(JsonBytes.encodeToPrettyString(JEquipmentModel.CODEC, armorModel));

		JEquipmentModel saddleModel = JEquipmentModel.model()
				.addLayer(LayerType.HORSE_BODY,
						JLayer.layer()
								.texture(Identifier.fromNamespaceAndPath("minecraft", "saddle"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(JEquipmentModel.CODEC, saddleModel));

		JEquipmentModel backpack = JEquipmentModel.model()
				.addLayerCustom("backpack",
						JLayer.layer()
								.texture(Identifier.fromNamespaceAndPath("mymod", "backpack"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(JEquipmentModel.CODEC, backpack));

		JLayer wolfLayer = JLayer.layer()
				.texture(Identifier.fromNamespaceAndPath("mymod", "wolf_tag"))
				.usePlayerTexture(false);

		JLayer bodyLayer = JLayer.layer()
				.texture(Identifier.fromNamespaceAndPath("mymod", "wolf_body"))
				.dyeable(Optional.of(0xCCCCCC));

		JEquipmentModel wolfWithTag = JEquipmentModel.model()
				.addLayer(LayerType.WOLF_BODY, bodyLayer)
				.addLayerCustom("wolf_tag", wolfLayer);
		System.out.println(JsonBytes.encodeToPrettyString(JEquipmentModel.CODEC, wolfWithTag));

		JEquipmentModel model1 = JEquipmentModel.model()
				.addLayer(LayerType.HORSE_BODY,
						JLayer.layer().texture(Identifier.fromNamespaceAndPath("minecraft", "diamond"))
				)
				.addLayer(LayerType.HUMANOID,
						JLayer.layer().texture(Identifier.fromNamespaceAndPath("minecraft", "diamond"))
				)
				.addLayer(LayerType.HUMANOID_LEGGINGS,
						JLayer.layer().texture(Identifier.fromNamespaceAndPath("minecraft", "diamond"))
				);
		System.out.println(JsonBytes.encodeToPrettyString(JEquipmentModel.CODEC, model1));

		var baseModel = id("test", "furniture/aaaaaaaaa");

		JVariant variant = new JVariant()
				// north
				.put("facing=north,lit=false,occupied=false", JState.model(baseModel))
				.put("facing=north,lit=false,occupied=true",  JState.model(baseModel))
				.put("facing=north,lit=true,occupied=false",  JState.model(baseModel))
				.put("facing=north,lit=true,occupied=true",   JState.model(baseModel))
				// east
				.put("facing=east,lit=false,occupied=false",  JState.model(baseModel).y(90))
				.put("facing=east,lit=false,occupied=true",   JState.model(baseModel).y(90))
				.put("facing=east,lit=true,occupied=false",   JState.model(baseModel).y(90))
				.put("facing=east,lit=true,occupied=true",    JState.model(baseModel).y(90))
				// south
				.put("facing=south,lit=false,occupied=false", JState.model(baseModel).y(180))
				.put("facing=south,lit=false,occupied=true",  JState.model(baseModel).y(180))
				.put("facing=south,lit=true,occupied=false",  JState.model(baseModel).y(180))
				.put("facing=south,lit=true,occupied=true",   JState.model(baseModel).y(180))
				// west
				.put("facing=west,lit=false,occupied=false",  JState.model(baseModel).y(270))
				.put("facing=west,lit=false,occupied=true",   JState.model(baseModel).y(270))
				.put("facing=west,lit=true,occupied=false",   JState.model(baseModel).y(270))
				.put("facing=west,lit=true,occupied=true",    JState.model(baseModel).y(270));
		System.out.println(JsonBytes.encodeToPrettyString(JState.CODEC, state(variant)));

		{
			// Fence-like: post always, arms when north/east/south/west are true
			Identifier POST = Identifier.tryParse("block/oak_fence_post");
			Identifier ARM  = Identifier.tryParse("block/oak_fence_side");

			JState fenceLike = state(Connectables.fenceLike(
					POST, ARM,
					"north", "east", "south", "west",
					true // uvlock
			));
			System.out.println("// Connectables.fenceLikeState");
			System.out.println(JsonBytes.encodeToPrettyString(JState.CODEC, fenceLike));
		}

		{
			// Pane-like: optional center + side per direction
			Identifier CENTER = Identifier.tryParse("block/glass_pane_noside");
			Identifier SIDE   = Identifier.tryParse("block/glass_pane_side");
			JState paneLike = state(Connectables.paneLike(
					CENTER, SIDE,
					"north", "east", "south", "west",
					true
			));
			System.out.println("// Connectables.paneLike");
			System.out.println(JsonBytes.encodeToPrettyString(JState.CODEC, paneLike));
		}

		{
			// Counter-like: top always, edges per side, corners when two sides meet
			Identifier TOP    = Identifier.tryParse("test:block/counter/top");
			Identifier EDGE   = Identifier.tryParse("test:block/counter/edge");
			Identifier INNER  = Identifier.tryParse("test:block/counter/inner_corner");
			Identifier OUTER  = Identifier.tryParse("test:block/counter/outer_corner");

			JState counterLike = state(Connectables.counterLike(
					TOP, EDGE, INNER, OUTER,
					"north", "east", "south", "west",
					true
			));
			System.out.println("// Connectables.counterLike");
			System.out.println(JsonBytes.encodeToPrettyString(JState.CODEC, counterLike));

			SkyIslandsWorldgen.main(args);

			JEnvironmentAttributes environmentAttributes = new JEnvironmentAttributes()
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
			System.out.println(JsonBytes.encodeToPrettyString(JEnvironmentAttributes.CODEC, environmentAttributes));
			System.out.println(JsonBytes.encodeToPrettyString(JWorldClock.CODEC, JWorldClock.worldClock()));

			System.out.println(JsonBytes.encodeToPrettyString(JDimensionType.CODEC, JDimensionType.dimensionType()
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
					.skybox(JDimensionType.Skybox.OVERWORLD)
					.cardinalLight(JDimensionType.CardinalLightType.DEFAULT)
					.attributes(environmentAttributes)
//					.attribute("minecraft:gameplay/bed_rule", BedRule.CODEC, BedRule.CAN_SLEEP_WHEN_DARK)
//					.attribute("minecraft:audio/background_music", BackgroundMusic.CODEC, BackgroundMusic.OVERWORLD)
//					.attribute("minecraft:audio/ambient_sounds", AmbientSounds.CODEC, AmbientSounds.LEGACY_CAVE_SETTINGS)
			));
		}
	}
}
