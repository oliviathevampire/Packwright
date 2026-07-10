package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class PaintingVariant {
	public static final Codec<PaintingVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.intRange(1, 16).fieldOf("width").forGetter(x -> x.width),
			Codec.intRange(1, 16).fieldOf("height").forGetter(x -> x.height),
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			Codec.STRING.optionalFieldOf("title").forGetter(x -> Optional.ofNullable(x.title)),
			Codec.STRING.optionalFieldOf("author").forGetter(x -> Optional.ofNullable(x.author))
	).apply(i, (width, height, assetId, title, author) -> {
		PaintingVariant out = new PaintingVariant();
		out.width = width;
		out.height = height;
		out.assetId = assetId;
		title.ifPresent(out::title);
		author.ifPresent(out::author);
		return out;
	}));

	private int width;
	private int height;
	private Identifier assetId;
	private String title;
	private String author;

	public static PaintingVariant paintingVariant() {
		return new PaintingVariant();
	}

	public PaintingVariant size(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public PaintingVariant assetId(Identifier id) { this.assetId = id; return this; }
	public PaintingVariant title(String title) { this.title = title; return this; }
	public PaintingVariant author(String author) { this.author = author; return this; }

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Identifier getAssetId() { return assetId; }
	public String getTitle() { return title; }
	public String getAuthor() { return author; }
}
