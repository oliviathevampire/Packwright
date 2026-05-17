package net.vampirestudios.arrp.assets.models;

import net.vampirestudios.arrp.data.loot.Condition;
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
public class Model implements Cloneable {
	private String parent;
	// true is default; null => omit (vanilla default true)
	private Boolean ambientocclusion;
	// transforms
	private Display display;
	// texture variables
	private Textures textures;
	// explicit element geometry (optional; omit to inherit from parent)
	private List<Element> elements;
	// item overrides
	private List<ModelOverride> overrides;

	/* ---------------------------------------------------------
	 * Existing constructors / factories
	 * --------------------------------------------------------- */

	/** @see #model(String) @see #model() */
	public Model() {}

	/** @return a new model that does not override its parent's elements */
	public static Model modelKeepElements() {
		Model model = new Model();
		model.elements = null;
		return model;
	}

	/** @return a new model that does not override its parent's elements */
	public static Model modelKeepElements(String parent) {
		Model model = new Model();
		model.parent = parent;
		model.elements = null;
		return model;
	}

	public static Model modelKeepElements(Identifier identifier) {
		return modelKeepElements(identifier.toString());
	}

	public static Model model() {
		return new Model();
	}

	public static Model model(String parent) {
		Model model = new Model();
		model.parent = parent;
		return model;
	}

	public static Model model(Identifier identifier) {
		return model(identifier.toString());
	}

	public static ModelOverride override(Condition predicate, Identifier model) {
		return new ModelOverride(predicate, model.toString());
	}

	public static Condition condition() {
		return new Condition(null);
	}

	public static Display display() {
		return new Display();
	}

	public static Element element() {
		return new Element();
	}

	public static Face face(String texture) {
		return new Face(texture);
	}

	public static Faces faces() {
		return new Faces();
	}

	public static Position position() {
		return new Position();
	}

	public static Rotation rotation(Direction.Axis axis) {
		return new Rotation(axis);
	}

	public static Textures textures() {
		return new Textures();
	}

	/* ---------------------------------------------------------
	 * NEW: Template factories (concise, common vanilla parents)
	 * --------------------------------------------------------- */

	/** minecraft:block/cube_all with "all" texture set. */
	public static Model cubeAll(String allTex) {
		return Model.model("minecraft:block/cube_all")
				.textures(Model.textures().var("all", allTex));
	}
	public static Model cubeAll(Identifier allTex) { return cubeAll(allTex.toString()); }

	/** minecraft:block/cube with per-face texture variables. */
	public static Model cube(String down, String up, String north, String south, String west, String east) {
		return Model.model("minecraft:block/cube").textures(
				Model.textures()
						.var("down",  down).var("up",    up)
						.var("north", north).var("south", south)
						.var("west",  west).var("east",  east)
		);
	}
	public static Model cube(Identifier down, Identifier up, Identifier north, Identifier south, Identifier west, Identifier east) {
		return cube(down.toString(), up.toString(), north.toString(), south.toString(), west.toString(), east.toString());
	}

	/** minecraft:block/cube_column (side + end). */
	public static Model cubeColumn(String side, String end) {
		return Model.model("minecraft:block/cube_column")
				.textures(Model.textures().var("side", side).var("end", end));
	}
	public static Model cubeColumn(Identifier side, Identifier end) { return cubeColumn(side.toString(), end.toString()); }

	/** minecraft:block/cube_bottom_top (side + bottom + top). */
	public static Model cubeBottomTop(String side, String bottom, String top) {
		return Model.model("minecraft:block/cube_bottom_top")
				.textures(Model.textures().var("side", side).var("bottom", bottom).var("top", top));
	}
	public static Model cubeBottomTop(Identifier side, Identifier bottom, Identifier top) {
		return cubeBottomTop(side.toString(), bottom.toString(), top.toString());
	}

	/** minecraft:block/cross (for plants/billboards). */
	public static Model cross(String crossTex) {
		return Model.model("minecraft:block/cross")
				.textures(Model.textures().var("cross", crossTex));
	}
	public static Model cross(Identifier crossTex) { return cross(crossTex.toString()); }

	/** minecraft:item/generated with layer0. */
	public static Model itemGenerated(String layer0) {
		return Model.model("minecraft:item/generated")
				.textures(Model.textures().var("layer0", layer0));
	}
	public static Model itemGenerated(Identifier layer0) { return itemGenerated(layer0.toString()); }

	/** minecraft:item/handheld with layer0 (tools). */
	public static Model itemHandheld(String layer0) {
		return Model.model("minecraft:item/handheld")
				.textures(Model.textures().var("layer0", layer0));
	}
	public static Model itemHandheld(Identifier layer0) { return itemHandheld(layer0.toString()); }

	/* ---------------------------------------------------------
	 * Fluent helpers
	 * --------------------------------------------------------- */

	public Model addOverride(ModelOverride override) {
		if (this.overrides == null) this.overrides = new ArrayList<>();
		this.overrides.add(override);
		return this;
	}

	public Model parent(String parent) {
		this.parent = parent;
		return this;
	}

	/** Explicitly set ambient occlusion (default true). null = omit. */
	public Model ambientOcclusion(boolean enable) {
		this.ambientocclusion = enable ? null : Boolean.FALSE;
		return this;
	}

	public Model noAmbientOcclusion() {
		this.ambientocclusion = Boolean.FALSE;
		return this;
	}

	public Model display(Display display) {
		this.display = display;
		return this;
	}

	public Model textures(Textures textures) {
		this.textures = textures;
		return this;
	}

	public Model element(Element... elements) {
		if (this.elements == null) {
			this.elements = new ArrayList<>();
		}
		this.elements.addAll(Arrays.asList(elements));
		return this;
	}

	/* ---- Fluent texture setters (sugar over textures().var(...)) ---- */

	public Model all(String tex) { ensureTex().var("all", tex); return this; }
	public Model all(Identifier tex) { return all(tex.toString()); }

	public Model side(String tex)   { ensureTex().var("side",   tex); return this; }
	public Model side(Identifier tex){ return side(tex.toString()); }

	public Model top(String tex)    { ensureTex().var("top",    tex); return this; }
	public Model top(Identifier tex){ return top(tex.toString()); }

	public Model bottom(String tex) { ensureTex().var("bottom", tex); return this; }
	public Model bottom(Identifier tex){ return bottom(tex.toString()); }

	public Model end(String tex)    { ensureTex().var("end",    tex); return this; }
	public Model end(Identifier tex){ return end(tex.toString()); }

	public Model crossTex(String tex){ ensureTex().var("cross", tex); return this; }
	public Model crossTex(Identifier tex){ return crossTex(tex.toString()); }

	public Model north(String tex)  { ensureTex().var("north",  tex); return this; }
	public Model north(Identifier tex){ return north(tex.toString()); }

	public Model south(String tex)  { ensureTex().var("south",  tex); return this; }
	public Model south(Identifier tex){ return south(tex.toString()); }

	public Model west(String tex)   { ensureTex().var("west",   tex); return this; }
	public Model west(Identifier tex){ return west(tex.toString()); }

	public Model east(String tex)   { ensureTex().var("east",   tex); return this; }
	public Model east(Identifier tex){ return east(tex.toString()); }

	public Model up(String tex)     { ensureTex().var("up",     tex); return this; }
	public Model up(Identifier tex) { return up(tex.toString()); }

	public Model down(String tex)   { ensureTex().var("down",   tex); return this; }
	public Model down(Identifier tex){ return down(tex.toString()); }

	/** Set arbitrary layerN texture vars (for item models). */
	public Model layer(int index, String tex) {
		ensureTex().var("layer" + index, tex);
		return this;
	}
	public Model layer0(String tex) { return layer(0, tex); }
	public Model layer(int index, Identifier tex) { return layer(index, tex.toString()); }
	public Model layer0(Identifier tex) { return layer(0, tex.toString()); }

	private Textures ensureTex() {
		if (this.textures == null) this.textures = new Textures();
		return this.textures;
	}

	@Override
	public Model clone() {
		try {
			return (Model) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
