package test;

import net.minecraft.util.EasingType;
import net.minecraft.world.attribute.modifier.BooleanModifier;
import net.minecraft.world.attribute.modifier.ColorModifier;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.level.MoonPhase;
import net.vampirestudios.packwright.assets.timeline.Timeline;
import net.vampirestudios.packwright.data.worldgen.ClockTimeMarkers;
import net.vampirestudios.packwright.data.worldgen.EnvironmentAttributes;
import net.vampirestudios.packwright.util.JsonBytes;

import static net.minecraft.world.timeline.Timelines.NIGHT_CLOUD_COLOR_MULTIPLIER;

public final class BuiltinTimelines {

    public static Timeline buildDayTimelineLike() {
		return new Timeline()
				.setPeriodTicks(24000)
				.addTimerMarker(ClockTimeMarkers.DAY, 1000, true)
				.addTimerMarker(ClockTimeMarkers.NOON, 6000, true)
				.addTimerMarker(ClockTimeMarkers.NIGHT, 13000, true)
				.addTimerMarker(ClockTimeMarkers.MIDNIGHT, 18000, true)
				.addTimerMarker(ClockTimeMarkers.WAKE_UP_FROM_SLEEP, 0)
				.addTimerMarker(ClockTimeMarkers.ROLL_VILLAGE_SIEGE, 18000)
				.addModifierTrack(
						EnvironmentAttributes.MONSTERS_BURN,
						BooleanModifier.OR,
						track -> track.addKeyframe(12542, false).addKeyframe(23460, true)
				)
				.addModifierTrack(
						EnvironmentAttributes.SKY_LIGHT_LEVEL,
						FloatModifier.MULTIPLY,
						track -> track.addKeyframe(133, 1.0F).addKeyframe(11867, 1.0F).addKeyframe(13670, 0.26666668F).addKeyframe(22330, 0.26666668F)
				)
				.addModifierTrack(
						EnvironmentAttributes.SKY_LIGHT_FACTOR,
						FloatModifier.MULTIPLY,
						track -> track.addKeyframe(730, 1.0F).addKeyframe(11270, 1.0F).addKeyframe(13140, 0.24F).addKeyframe(22860, 0.24F)
				)
				.addModifierTrack(EnvironmentAttributes.FOG_COLOR, ColorModifier.MULTIPLY_RGB, t -> t
						.addKeyframe(133,   "#ffffff")
						.addKeyframe(11867, "#ffffff")
						.addKeyframe(13670, "#0f0f16")
						.addKeyframe(22330, "#0f0f16")
				)
				.addModifierTrack(
						EnvironmentAttributes.CLOUD_COLOR,
						ColorModifier.MULTIPLY_ARGB,
						track -> track.addKeyframe(133, -1)
								.addKeyframe(11867, -1)
								.addKeyframe(13670, NIGHT_CLOUD_COLOR_MULTIPLIER)
								.addKeyframe(22330, NIGHT_CLOUD_COLOR_MULTIPLIER)
				)
				.addTrack(
						EnvironmentAttributes.SUNRISE_SUNSET_COLOR,
						track -> track.addKeyframe(71, "#5fefa333")
								.addKeyframe(310, "#29f5ba33")
								.addKeyframe(565, "#06fbd433")
								.addKeyframe(730, "#00ffe533")
								.addKeyframe(11270, "#00ffe533")
								.addKeyframe(11397, "#04fcd833")
								.addKeyframe(11522, "#0ff9cb33")
								.addKeyframe(11690, "#29f5ba33")
								.addKeyframe(11929, "#5fefa333")
								.addKeyframe(12243, "#b1e78733")
								.addKeyframe(12358, "#cce47e33")
								.addKeyframe(12512, "#e9e07233")
								.addKeyframe(12613, "#f6dd6b33")
								.addKeyframe(12732, "#feda6333")
								.addKeyframe(12841, "#fed75c33")
								.addKeyframe(13035, "#ecd25133")
								.addKeyframe(13252, "#c1cc4733")
								.addKeyframe(13775, "#36be3733")
								.addKeyframe(13888, "#1fbb3533")
								.addKeyframe(14039, "#09b73333")
								.addKeyframe(14192, "#00b33333")
								.addKeyframe(21807, "#00b23333")
								.addKeyframe(21961, "#09b73333")
								.addKeyframe(22112, "#1fbb3533")
								.addKeyframe(22225, "#36be3733")
								.addKeyframe(22748, "#c1cc4733")
								.addKeyframe(22965, "#ecd25133")
								.addKeyframe(23159, "#fed75c33")
								.addKeyframe(23272, "#feda6333")
								.addKeyframe(23488, "#e9e07233")
								.addKeyframe(23642, "#cce47e33")
								.addKeyframe(23757, "#b1e78733")
				)
				.addTrack(EnvironmentAttributes.SUN_ANGLE, t -> t
						.ease(EasingType.symmetricCubicBezier(0.362f, 0.241f))
						.addKeyframe(6000, 360.0f)
						.addKeyframe(6000, 0.0f)
				);
    }

    public static Timeline buildMoonTimelineLike() {
		return new Timeline()
				.setPeriodTicks(192000)
				.addModifierTrack(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, FloatModifier.MAXIMUM, t -> t
						.ease(EasingType.CONSTANT)
						.addKeyframe(0,      0.5f)
						.addKeyframe(24000,  0.375f)
						.addKeyframe(48000,  0.25f)
						.addKeyframe(72000,  0.125f)
						.addKeyframe(96000,  0.0f)
						.addKeyframe(120000, 0.125f)
						.addKeyframe(144000, 0.25f)
						.addKeyframe(168000, 0.375f)
				).addTrack(EnvironmentAttributes.MOON_PHASE, t -> t
						.addKeyframe(0, MoonPhase.FULL_MOON.getSerializedName())
						.addKeyframe(24000,  MoonPhase.WANING_GIBBOUS.getSerializedName())
						.addKeyframe(48000,  MoonPhase.THIRD_QUARTER.getSerializedName())
						.addKeyframe(72000,  MoonPhase.WANING_CRESCENT.getSerializedName())
						.addKeyframe(96000,  MoonPhase.NEW_MOON.getSerializedName())
						.addKeyframe(120000, MoonPhase.WAXING_CRESCENT.getSerializedName())
						.addKeyframe(144000, MoonPhase.FIRST_QUARTER.getSerializedName())
						.addKeyframe(168000, MoonPhase.WAXING_GIBBOUS.getSerializedName())
				);
    }

    public static Timeline buildVillagerScheduleLike() {
		return new Timeline()
				.setPeriodTicks(24000)
				.addTrack(
						EnvironmentAttributes.VILLAGER_ACTIVITY,
						track -> track.addKeyframe(10, VillagerActivity.IDLE)
								.addKeyframe(2000, VillagerActivity.WORK)
								.addKeyframe(9000, VillagerActivity.MEET)
								.addKeyframe(11000, VillagerActivity.IDLE)
								.addKeyframe(12000, VillagerActivity.REST)
				)
				.addTrack(
						EnvironmentAttributes.BABY_VILLAGER_ACTIVITY,
						track -> track.addKeyframe(10, VillagerActivity.IDLE)
								.addKeyframe(3000, VillagerActivity.PLAY)
								.addKeyframe(6000, VillagerActivity.IDLE)
								.addKeyframe(10000, VillagerActivity.PLAY)
								.addKeyframe(12000, VillagerActivity.REST)
				);
    }

    public static Timeline buildEarlyGameLike() {
		return new Timeline()
				.addModifierTrack(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, BooleanModifier.AND, t -> t
						.addKeyframe(0,      false)
						.addKeyframe(120000, true)
				);
    }

    public static void dumpDayTimelineJson() {
        Timeline day = buildDayTimelineLike();
        System.out.println("day timeline:");
        System.out.println(JsonBytes.encodeToPrettyString(Timeline.CODEC, day));
    }
}
