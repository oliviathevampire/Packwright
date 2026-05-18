package net.vampirestudios.arrp.assets.blockstates;

import java.util.HashMap;
import java.util.Map;

/**
 * Vanilla-shaped blockstate helpers for ARRP.
 * These write variants/multipart entries; you still add models separately.
 *
 * All methods are PROPERTY-NAME AGNOSTIC: pass your base property map
 * (e.g., Map.of("mat", idx)), and we append shape-specific properties.
 */
public final class BlockstateTemplates {
	private BlockstateTemplates() {
	}

	/* -----------------------------------------------------------
	 * SLAB (variants)
	 * properties used: "type" = bottom|top|double
	 * ----------------------------------------------------------- */

	/**
	 * Adds three variants for a slab: bottom, top, double.
	 * @param into    target Variant
	 * @param base    base property map (e.g., {"mat": <idx>})
	 * @param bottom  model for type=bottom
	 * @param top     model for type=top
	 * @param full    model for type=double (usually the full block model)
	 */
	public static void addSlab(Variant into,
							   Map<String, ?> base,
							   SimpleModel bottom,
							   SimpleModel top,
							   SimpleModel full) {
		into.put(plus(base, "type", "bottom"), bottom);
		into.put(plus(base, "type", "top"), top);
		into.put(plus(base, "type", "double"), full);
	}

	/* -----------------------------------------------------------
	 * STAIRS (variants)
	 * properties used:
	 *  - "facing" = north|south|west|east
	 *  - "half"   = top|bottom
	 *  - "shape"  = straight|inner_left|inner_right|outer_left|outer_right
	 * ----------------------------------------------------------- */

	/**
	 * Adds the full vanilla stairs state-space (40 combos per base) with the provided models.
	 * @param into      target Variant
	 * @param base      base property map (e.g., {"mat": <idx>})
	 * @param straight  model for straight runs
	 * @param inner     model for inner corners
	 * @param outer     model for outer corners
	 */
	public static void addStairs(Variant into,
								 Map<String, ?> base,
								 SimpleModel straight,
								 SimpleModel inner,
								 SimpleModel outer) {
		final String[] FAC = {"north", "south", "west", "east"};
		final String[] HAL = {"top", "bottom"};
		final String[] SHP = {"straight", "inner_left", "inner_right", "outer_left", "outer_right"};

		for (String f : FAC) {
			for (String h : HAL) {
				for (String s : SHP) {
					SimpleModel ref = switch (s) {
						case "inner_left", "inner_right" -> inner;
						case "outer_left", "outer_right" -> outer;
						default -> straight;
					};
					into.put(plus(base, "facing", f, "half", h, "shape", s), ref);
				}
			}
		}
	}

	/* -----------------------------------------------------------
	 * WALL (multipart)
	 * properties used in "when":
	 *  - "north","east","south","west" = low|tall (vanilla wall connections)
	 * Always includes a post part.
	 * Rotation is applied via y = 0/90/180/270 with uvlock().
	 * ----------------------------------------------------------- */

	/**
	 * Adds multipart rules for a wall (post + 4 low + 4 tall).
	 * @param multipart target Multipart container (created by caller)
	 * @param base      base property map (e.g., {"mat": <idx>})
	 * @param post      post model (at the center)
	 * @param side      side (low) model
	 * @param sideTall  side (tall) model
	 */
	public static void addWall(Multipart multipart,
							   Map<String, ?> base,
							   SimpleModel post,
							   SimpleModel side,
							   SimpleModel sideTall) {
		// post (always)
		multipart.when(base).addModel(post);

		// sides (low)
		multipart.when(plus(base, "north", "low")).addModel(side.uvlock().y(0));
		multipart.when(plus(base, "east", "low")).addModel(side.uvlock().y(90));
		multipart.when(plus(base, "south", "low")).addModel(side.uvlock().y(180));
		multipart.when(plus(base, "west", "low")).addModel(side.uvlock().y(270));

		// sides (tall)
		multipart.when(plus(base, "north", "tall")).addModel(sideTall.uvlock().y(0));
		multipart.when(plus(base, "east", "tall")).addModel(sideTall.uvlock().y(90));
		multipart.when(plus(base, "south", "tall")).addModel(sideTall.uvlock().y(180));
		multipart.when(plus(base, "west", "tall")).addModel(sideTall.uvlock().y(270));
	}

	/* -----------------------------------------------------------
	 * Utilities
	 * ----------------------------------------------------------- */

	/** Shallow copy + single k=v. */
	public static Map<String, Object> plus(Map<String, ?> base, String k, Object v) {
		HashMap<String, Object> out = new HashMap<>(base.size() + 1);
		out.putAll(base);
		out.put(k, v);
		return out;
	}

	/** Shallow copy + three kv pairs (handy for stairs). */
	public static Map<String, Object> plus(Map<String, ?> base,
										   String k1, Object v1,
										   String k2, Object v2,
										   String k3, Object v3) {
		HashMap<String, Object> out = new HashMap<>(base.size() + 3);
		out.putAll(base);
		out.put(k1, v1);
		out.put(k2, v2);
		out.put(k3, v3);
		return out;
	}

	/* -----------------------------------------------------------
	 * SIMPLE / CUBE-ALL HELPERS
	 * ----------------------------------------------------------- */

	/** One composite-variant entry using a single model. */
	public static void addSingle(Variant into, Map<String, ?> base, SimpleModel model) {
		into.put(base, model);
	}

	/** One composite-variant with multiple models (uniform random). */
	public static void addSingle(Variant into, Map<String, ?> base, SimpleModel... models) {
		into.put(base, models);
	}

	/** One composite-variant with weighted randomness via duplication. */
	public static void addSingleWeighted(Variant into, Map<String, ?> base, SimpleModel model, int weight) {
		into.putWeighted(base, model, weight); // uses your Variant helper
	}

	/**
	 * “Cube-all” shortcut:
	 *   - emits a single variant key (e.g., {"mat": idx}) → modelId
	 *   - use when the block has no rotated/oriented states.
	 *   - model should already be cube-all with a single texture.
	 */
	public static void cubeAll(Variant into, Map<String, ?> base, SimpleModel cubeModel) {
		into.put(base, cubeModel);
	}

	/**
	 * Multipart “always on” part for cube-all or decorations.
	 * Useful for walls/posts that always render regardless of other props.
	 */
	public static void cubeAll(Multipart multipart, Map<String, ?> base, SimpleModel cubeModel) {
		multipart.when(base).addModel(cubeModel);
	}

	/** Negated convenience for multipart: emit when NOT prop=value. */
	/*public static void whenNot(Multipart multipart, Map<String, ?> base, String prop, String value, SimpleModel model) {
		Map<String, ?> cond = new HashMap<>(base);
		cond.put(prop, "!" + value);
		multipart.when(cond).addModel(model);
	}*/

	/* -----------------------------------------------------------
	 * STAIRS (variants) — rotated models
	 * Base orientation assumption:
	 *   - models point to EAST, BOTTOM, STRAIGHT in their .json (vanilla default)
	 * ----------------------------------------------------------- */
	public static void addStairsRotated(Variant into,
										Map<String, ?> base,
										SimpleModel straight,
										SimpleModel inner,
										SimpleModel outer) {
		final String[] FAC = {"east","south","west","north"};
		final String[] HAL = {"bottom","top"};
		final String[] SHP = {"straight","inner_left","inner_right","outer_left","outer_right"};

		for (String f : FAC) {
			int y = StairsRot.yForFacing(f);
			for (String h : HAL) {
				int x = StairsRot.xForHalf(h);
				for (String s : SHP) {
					SimpleModel ref = switch (s) {
						case "inner_left","inner_right" -> inner;
						case "outer_left","outer_right" -> outer;
						default -> straight;
					};
					// vanilla: uvlock recommended for rotated cube-like textures
					into.put(
							plus(base, "facing", f, "half", h, "shape", s),
							ref.y(y).x(x).uvlock()
					);
				}
			}
		}
	}

	/* -----------------------------------------------------------
	 * ROTATION BASE (authoring convention)
	 * ----------------------------------------------------------- */
	public enum AuthoredFacing { NORTH, EAST }

	/* -----------------------------------------------------------
	 * PILLAR (axis = x|y|z). Model authored upright (axis=Y).
	 * ----------------------------------------------------------- */
	public static void addPillarAxis(Variant into,
									 Map<String, ?> base,
									 SimpleModel upright /* axis=y */) {
		// y (upright)
		into.put(plus(base, "axis", "y"), upright);

		// x (laying east-west): rotate around X
		into.put(plus(base, "axis", "x"), upright.x(90));

		// z (laying north-south): rotate around X then Y
		into.put(plus(base, "axis", "z"), upright.x(90).y(90));
	}

	/* -----------------------------------------------------------
	 * HORIZONTAL FACING (furnace, dispenser, etc.)
	 * Model authored facing NORTH or EAST (choose).
	 * ----------------------------------------------------------- */
	public static void addHorizontalFacing(Variant into,
										   Map<String, ?> base,
										   SimpleModel frontModel,
										   AuthoredFacing authored) {
		// y-rotations for a model authored toward NORTH or EAST
		int yNorth = (authored == AuthoredFacing.NORTH) ? 0  : 270;
		int yEast  = (authored == AuthoredFacing.NORTH) ? 90 : 0;
		int ySouth = (authored == AuthoredFacing.NORTH) ? 180: 90;
		int yWest  = (authored == AuthoredFacing.NORTH) ? 270: 180;

		into.put(plus(base, "facing", "north"), frontModel.y(yNorth).uvlock());
		into.put(plus(base, "facing", "east"),  frontModel.y(yEast).uvlock());
		into.put(plus(base, "facing", "south"), frontModel.y(ySouth).uvlock());
		into.put(plus(base, "facing", "west"),  frontModel.y(yWest).uvlock());
	}

	/* -----------------------------------------------------------
	 * 6-WAY DIRECTIONAL FACING (facing = up|down|north|south|east|west)
	 * Model authored facing NORTH with “front” on +Z and “up” on +Y.
	 * ----------------------------------------------------------- */
	public static void addDirectionalFacing(Variant into,
											Map<String, ?> base,
											SimpleModel model,
											AuthoredFacing authored) {
		// Normalize to "authored == NORTH" by pre-rotating once if authored EAST.
		SimpleModel baseModel = (authored == AuthoredFacing.EAST) ? model.y(270) : model;

		// Horizontal
		into.put(plus(base, "facing", "north"), baseModel.y(0).uvlock());
		into.put(plus(base, "facing", "east"),  baseModel.y(90).uvlock());
		into.put(plus(base, "facing", "south"), baseModel.y(180).uvlock());
		into.put(plus(base, "facing", "west"),  baseModel.y(270).uvlock());

		// Vertical: rotate about X
		// facing=up → tip “front” upward: x=270
		into.put(plus(base, "facing", "up"),   baseModel.x(270).uvlock());
		// facing=down → tip downward: x=90
		into.put(plus(base, "facing", "down"), baseModel.x(90).uvlock());
	}

	/* -----------------------------------------------------------
	 * ANY-DIRECTION ALIAS (semantic sugar)
	 * ----------------------------------------------------------- */
	public static void addEveryDirection(Variant into,
										 Map<String, ?> base,
										 SimpleModel model,
										 AuthoredFacing authored) {
		addDirectionalFacing(into, base, model, authored);
	}
}
