package net.vampirestudios.arrp.json.models;

import net.vampirestudios.arrp.json.loot.JCondition;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A block/item model. Static-import factory methods from this class for brevity.
 *
 * Backward-compatible extensions:
 * - Static templates: cubeAll, cube, cubeColumn, cubeBottomTop, cross, itemGenerated, itemHandheld
 * - Fluent texture setters: all/side/top/bottom/end/crossTex/north/south/west/east/up/down/layer/layer0
 * - AO toggle: ambientOcclusion(boolean)
 * - Keep-elements factories: modelKeepElements(...)
 */
public class JModel implements Cloneable {
	private String parent;
	// true is default; null => omit (vanilla default true)
	private Boolean ambientocclusion;
	// transforms
	private JDisplay display;
	// texture variables
	private JTextures textures;
	// explicit element geometry (optional; omit to inherit from parent)
	private List<JElement> elements;
	// item overrides
	private List<JOverride> overrides;

	/* ---------------------------------------------------------
	 * Existing constructors / factories
	 * --------------------------------------------------------- */

	/** @see #model(String) @see #model() */
	public JModel() {}

	/** @return a new model that does not override its parent's elements */
	public static JModel modelKeepElements() {
		JModel model = new JModel();
		model.elements = null;
		return model;
	}

	/** @return a new model that does not override its parent's elements */
	public static JModel modelKeepElements(String parent) {
		JModel model = new JModel();
		model.parent = parent;
		model.elements = null;
		return model;
	}

	public static JModel modelKeepElements(Identifier identifier) {
		return modelKeepElements(identifier.toString());
	}

	public static JModel model() {
		return new JModel();
	}

	public static JModel model(String parent) {
		JModel model = new JModel();
		model.parent = parent;
		return model;
	}

	public static JModel model(Identifier identifier) {
		return model(identifier.toString());
	}

	public static JOverride override(JCondition predicate, Identifier model) {
		return new JOverride(predicate, model.toString());
	}

	public static JCondition condition() {
		return new JCondition(null);
	}

	public static JDisplay display() {
		return new JDisplay();
	}

	public static JElement element() {
		return new JElement();
	}

	public static JFace face(String texture) {
		return new JFace(texture);
	}

	public static JFaces faces() {
		return new JFaces();
	}

	public static JPosition position() {
		return new JPosition();
	}

	public static JRotation rotation(Direction.Axis axis) {
		return new JRotation(axis);
	}

	public static JTextures textures() {
		return new JTextures();
	}

	/* ---------------------------------------------------------
	 * NEW: Template factories (concise, common vanilla parents)
	 * --------------------------------------------------------- */

	/** minecraft:block/cube_all with "all" texture set. */
	public static JModel cubeAll(String allTex) {
		return JModel.model("minecraft:block/cube_all")
				.textures(JModel.textures().var("all", allTex));
	}
	public static JModel cubeAll(Identifier allTex) { return cubeAll(allTex.toString()); }

	/** minecraft:block/cube with per-face texture variables. */
	public static JModel cube(String down, String up, String north, String south, String west, String east) {
		return JModel.model("minecraft:block/cube").textures(
				JModel.textures()
						.var("down",  down).var("up",    up)
						.var("north", north).var("south", south)
						.var("west",  west).var("east",  east)
		);
	}
	public static JModel cube(Identifier down, Identifier up, Identifier north, Identifier south, Identifier west, Identifier east) {
		return cube(down.toString(), up.toString(), north.toString(), south.toString(), west.toString(), east.toString());
	}

	/** minecraft:block/cube_column (side + end). */
	public static JModel cubeColumn(String side, String end) {
		return JModel.model("minecraft:block/cube_column")
				.textures(JModel.textures().var("side", side).var("end", end));
	}
	public static JModel cubeColumn(Identifier side, Identifier end) { return cubeColumn(side.toString(), end.toString()); }

	/** minecraft:block/cube_bottom_top (side + bottom + top). */
	public static JModel cubeBottomTop(String side, String bottom, String top) {
		return JModel.model("minecraft:block/cube_bottom_top")
				.textures(JModel.textures().var("side", side).var("bottom", bottom).var("top", top));
	}
	public static JModel cubeBottomTop(Identifier side, Identifier bottom, Identifier top) {
		return cubeBottomTop(side.toString(), bottom.toString(), top.toString());
	}

	/** minecraft:block/cross (for plants/billboards). */
	public static JModel cross(String crossTex) {
		return JModel.model("minecraft:block/cross")
				.textures(JModel.textures().var("cross", crossTex));
	}
	public static JModel cross(Identifier crossTex) { return cross(crossTex.toString()); }

	/** minecraft:item/generated with layer0. */
	public static JModel itemGenerated(String layer0) {
		return JModel.model("minecraft:item/generated")
				.textures(JModel.textures().var("layer0", layer0));
	}
	public static JModel itemGenerated(Identifier layer0) { return itemGenerated(layer0.toString()); }

	/** minecraft:item/handheld with layer0 (tools). */
	public static JModel itemHandheld(String layer0) {
		return JModel.model("minecraft:item/handheld")
				.textures(JModel.textures().var("layer0", layer0));
	}
	public static JModel itemHandheld(Identifier layer0) { return itemHandheld(layer0.toString()); }

	/* ---------------------------------------------------------
	 * Fluent helpers
	 * --------------------------------------------------------- */

	public JModel addOverride(JOverride override) {
		if (this.overrides == null) this.overrides = new ArrayList<>();
		this.overrides.add(override);
		return this;
	}

	public JModel parent(String parent) {
		this.parent = parent;
		return this;
	}

	/** Explicitly set ambient occlusion (default true). null = omit. */
	public JModel ambientOcclusion(boolean enable) {
		this.ambientocclusion = enable ? null : Boolean.FALSE;
		return this;
	}

	public JModel noAmbientOcclusion() {
		this.ambientocclusion = Boolean.FALSE;
		return this;
	}

	public JModel display(JDisplay display) {
		this.display = display;
		return this;
	}

	public JModel textures(JTextures textures) {
		this.textures = textures;
		return this;
	}

	public JModel element(JElement... elements) {
		if (this.elements == null) {
			this.elements = new ArrayList<>();
		}
		this.elements.addAll(Arrays.asList(elements));
		return this;
	}

	/* ---- Fluent texture setters (sugar over textures().var(...)) ---- */

	public JModel all(String tex) { ensureTex().var("all", tex); return this; }
	public JModel all(Identifier tex) { return all(tex.toString()); }

	public JModel side(String tex)   { ensureTex().var("side",   tex); return this; }
	public JModel side(Identifier tex){ return side(tex.toString()); }

	public JModel top(String tex)    { ensureTex().var("top",    tex); return this; }
	public JModel top(Identifier tex){ return top(tex.toString()); }

	public JModel bottom(String tex) { ensureTex().var("bottom", tex); return this; }
	public JModel bottom(Identifier tex){ return bottom(tex.toString()); }

	public JModel end(String tex)    { ensureTex().var("end",    tex); return this; }
	public JModel end(Identifier tex){ return end(tex.toString()); }

	public JModel crossTex(String tex){ ensureTex().var("cross", tex); return this; }
	public JModel crossTex(Identifier tex){ return crossTex(tex.toString()); }

	public JModel north(String tex)  { ensureTex().var("north",  tex); return this; }
	public JModel north(Identifier tex){ return north(tex.toString()); }

	public JModel south(String tex)  { ensureTex().var("south",  tex); return this; }
	public JModel south(Identifier tex){ return south(tex.toString()); }

	public JModel west(String tex)   { ensureTex().var("west",   tex); return this; }
	public JModel west(Identifier tex){ return west(tex.toString()); }

	public JModel east(String tex)   { ensureTex().var("east",   tex); return this; }
	public JModel east(Identifier tex){ return east(tex.toString()); }

	public JModel up(String tex)     { ensureTex().var("up",     tex); return this; }
	public JModel up(Identifier tex) { return up(tex.toString()); }

	public JModel down(String tex)   { ensureTex().var("down",   tex); return this; }
	public JModel down(Identifier tex){ return down(tex.toString()); }

	/** Set arbitrary layerN texture vars (for item models). */
	public JModel layer(int index, String tex) {
		ensureTex().var("layer" + index, tex);
		return this;
	}
	public JModel layer0(String tex) { return layer(0, tex); }
	public JModel layer(int index, Identifier tex) { return layer(index, tex.toString()); }
	public JModel layer0(Identifier tex) { return layer(0, tex.toString()); }

	private JTextures ensureTex() {
		if (this.textures == null) this.textures = new JTextures();
		return this.textures;
	}

	@Override
	public JModel clone() {
		try {
			return (JModel) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
