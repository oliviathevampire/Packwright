package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.util.DynamicMap;

import java.util.Optional;

/**
 * {@code title}/{@code author} are vanilla text components (plain, or translatable via
 * {@code {"translate": "..."}}), but this project can't depend on vanilla's actual
 * {@code Component}/{@code ComponentSerialization.CODEC} to build or encode one — that requires
 * the game's registries to be bootstrapped ({@code Bootstrap.bootStrap()}), which this
 * standalone data-generation tool never runs (fails with e.g. "Not bootstrapped (called from
 * registry minecraft:game_event)"). {@link DynamicMap} builds the same JSON shape by hand
 * instead, matching the pattern already used for e.g. {@code LootFunction.title(String)}.
 */
public class PaintingVariant {
	public static final Codec<PaintingVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.intRange(1, 16).fieldOf("width").forGetter(x -> x.width),
			Codec.intRange(1, 16).fieldOf("height").forGetter(x -> x.height),
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			DynamicMap.CODEC.optionalFieldOf("title").forGetter(x -> Optional.ofNullable(x.title)),
			DynamicMap.CODEC.optionalFieldOf("author").forGetter(x -> Optional.ofNullable(x.author))
	).apply(i, (width, height, assetId, title, author) -> {
		PaintingVariant out = new PaintingVariant();
		out.width = width;
		out.height = height;
		out.assetId = assetId;
		title.ifPresent(out::title);
		author.ifPresent(out::	author);
		return out;
	}));

	private int width;
	private int height;
	private Identifier assetId;
	private DynamicMap title;
	private DynamicMap author;

	public static PaintingVariant paintingVariant() {
		return new PaintingVariant();
	}

	public PaintingVariant size(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public PaintingVariant assetId(Identifier id) { this.assetId = id; return this; }
	/** an advanced/translatable title; build with e.g. {@code DynamicMap.object().set("translate", "...")} */
	public PaintingVariant title(DynamicMap title) { this.title = title; return this; }
	public PaintingVariant title(String plainText) { this.title = DynamicMap.object().set("text", plainText); return this; }
	/** an advanced/translatable author; build with e.g. {@code DynamicMap.object().set("translate", "...")} */
	public PaintingVariant author(DynamicMap author) { this.author = author; return this; }
	public PaintingVariant author(String plainText) { this.author = DynamicMap.object().set("text", plainText); return this; }

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Identifier getAssetId() { return assetId; }
	public DynamicMap getTitle() { return title; }
	public DynamicMap getAuthor() { return author; }
}
