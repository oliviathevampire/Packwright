package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.assets.timeline.Timeline;

public final class BuiltinTimelines {

    public static Timeline buildDayTimelineLike() {
        Timeline timeline = new Timeline()
                .period(24000);

        // gameplay: monsters burn in daylight
        timeline.track("minecraft:gameplay/monsters_burn",
                new Timeline.Track()
                        .modifier("or")
                        .addKeyframe(new Timeline.Keyframe(12542, new JsonPrimitive(false)))
                        .addKeyframe(new Timeline.Keyframe(23460, new JsonPrimitive(true)))
        );

		timeline.track("minecraft:gameplay/monsters_burn",
				Timeline.Track.boolToggle("or", 12542, 23460, false, true)
		);

        // gameplay: sky light factor (scaled)
        timeline.track("minecraft:visual/sky_light_factor",
                new Timeline.Track()
                        .modifier("multiply")
                        .addKeyframe(new Timeline.Keyframe(730,   new JsonPrimitive(1.0f)))
                        .addKeyframe(new Timeline.Keyframe(11270, new JsonPrimitive(1.0f)))
                        .addKeyframe(new Timeline.Keyframe(13140, new JsonPrimitive(0.24f)))
                        .addKeyframe(new Timeline.Keyframe(22860, new JsonPrimitive(0.24f)))
        );

        // visual: fog color
        timeline.track("minecraft:visual/fog_color",
                new Timeline.Track()
                        .modifier("multiply")
                        .addKeyframe(new Timeline.Keyframe(133,   new JsonPrimitive("#ffffff")))
                        .addKeyframe(new Timeline.Keyframe(11867, new JsonPrimitive("#ffffff")))
                        .addKeyframe(new Timeline.Keyframe(13670, new JsonPrimitive("#0f0f16")))
                        .addKeyframe(new Timeline.Keyframe(22330, new JsonPrimitive("#0f0f16")))
        );

        // visual: sunrise_sunset_color (lots of keyframes)
        Timeline.Track sunrise = new Timeline.Track();
        sunrise.addKeyframe(new Timeline.Keyframe(71,   new JsonPrimitive("#5fefa333")));
        sunrise.addKeyframe(new Timeline.Keyframe(310,  new JsonPrimitive("#29f5ba33")));
        sunrise.addKeyframe(new Timeline.Keyframe(565,  new JsonPrimitive("#06fbd433")));
        // ... (you can add every one from day.json if you want)
        timeline.track("minecraft:visual/sunrise_sunset_color", sunrise);

        // visual: star_brightness, etc... same pattern


		Timeline.Track sunAngleTrack = new Timeline.Track();

		// cubic_bezier ease as object like Mojang:
		JsonObject easeObj = new JsonObject();
		JsonArray bezier = new JsonArray();
		bezier.add(0.362);
		bezier.add(0.241);
		bezier.add(0.638);
		bezier.add(0.759);
		easeObj.add("cubic_bezier", bezier);

		sunAngleTrack
				.ease(easeObj)
				.addKeyframe(new Timeline.Keyframe(6000, new JsonPrimitive(360.0f)))
				.addKeyframe(new Timeline.Keyframe(6000, new JsonPrimitive(0.0f)));

		timeline.track("minecraft:visual/sun_angle", sunAngleTrack);

        return timeline;
    }

	public static Timeline buildMoonTimelineLike() {
		Timeline timeline = new Timeline()
				.period(192000); // 8 Minecraft days

		timeline.track("minecraft:gameplay/surface_slime_spawn_chance",
				Timeline.Track.numericCurve("maximum",
						0,      0.5f,
						24000,  0.375f,
						48000,  0.25f,
						72000,  0.125f,
						96000,  0.0f,
						120000, 0.125f,
						144000, 0.25f,
						168000, 0.375f
				)
		);

		// Moon phase as string enums
		Timeline.Track phaseTrack = new Timeline.Track();
		phaseTrack.addKeyframe(new Timeline.Keyframe(0,      new JsonPrimitive("full_moon")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(24000,  new JsonPrimitive("waning_gibbous")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(48000,  new JsonPrimitive("third_quarter")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(72000,  new JsonPrimitive("waning_crescent")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(96000,  new JsonPrimitive("new_moon")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(120000, new JsonPrimitive("waxing_crescent")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(144000, new JsonPrimitive("first_quarter")));
		phaseTrack.addKeyframe(new Timeline.Keyframe(168000, new JsonPrimitive("waxing_gibbous")));

		timeline.track("minecraft:visual/moon_phase", phaseTrack);

		return timeline;
	}

	public static Timeline buildVillagerScheduleLike() {
		Timeline timeline = new Timeline()
				.period(24000);

		// Adult villager schedule
		Timeline.Track adult = new Timeline.Track();
		adult.addKeyframe(new Timeline.Keyframe(10,    new JsonPrimitive("minecraft:idle")));
		adult.addKeyframe(new Timeline.Keyframe(2000,  new JsonPrimitive("minecraft:work")));
		adult.addKeyframe(new Timeline.Keyframe(9000,  new JsonPrimitive("minecraft:meet")));
		adult.addKeyframe(new Timeline.Keyframe(11000, new JsonPrimitive("minecraft:idle")));
		adult.addKeyframe(new Timeline.Keyframe(12000, new JsonPrimitive("minecraft:rest")));
		timeline.track("minecraft:gameplay/villager_activity", adult);

		// Baby villager schedule
		Timeline.Track baby = new Timeline.Track();
		baby.addKeyframe(new Timeline.Keyframe(10,    new JsonPrimitive("minecraft:idle")));
		baby.addKeyframe(new Timeline.Keyframe(3000,  new JsonPrimitive("minecraft:play")));
		baby.addKeyframe(new Timeline.Keyframe(6000,  new JsonPrimitive("minecraft:idle")));
		baby.addKeyframe(new Timeline.Keyframe(10000, new JsonPrimitive("minecraft:play")));
		baby.addKeyframe(new Timeline.Keyframe(12000, new JsonPrimitive("minecraft:rest")));
		timeline.track("minecraft:gameplay/baby_villager_activity", baby);

		return timeline;
	}

	public static Timeline buildEarlyGameLike() {
		Timeline timeline = new Timeline();
		// period_ticks omitted → one-shot style

		Timeline.Track patrolTrack = new Timeline.Track()
				.modifier("and"); // from json: "modifier": "and"

		patrolTrack.addKeyframe(new Timeline.Keyframe(0,      new JsonPrimitive(false)));
		patrolTrack.addKeyframe(new Timeline.Keyframe(120000, new JsonPrimitive(true)));

		timeline.track("minecraft:gameplay/can_pillager_patrol_spawn", patrolTrack);
		return timeline;
	}

    public static void dumpDayTimelineJson() {
        Timeline day = buildDayTimelineLike();
		System.out.println("day timeline:");
		System.out.println(JsonBytes.encodeToPrettyString(Timeline.CODEC, day));
    }
}
