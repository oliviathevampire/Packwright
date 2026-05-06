package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class JPaintingVariant {
	public static final Codec<JPaintingVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.intRange(1, 16).fieldOf("width").forGetter(x -> x.width),
			Codec.intRange(1, 16).fieldOf("height").forGetter(x -> x.height),
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			Codec.STRING.optionalFieldOf("title").forGetter(x -> Optional.ofNullable(x.title)),
			Codec.STRING.optionalFieldOf("author").forGetter(x -> Optional.ofNullable(x.author))
	).apply(i, (width, height, assetId, title, author) -> {
		JPaintingVariant out = new JPaintingVariant();
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

	public static JPaintingVariant paintingVariant() {
		return new JPaintingVariant();
	}

	public JPaintingVariant size(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public JPaintingVariant assetId(Identifier id) { this.assetId = id; return this; }
	public JPaintingVariant title(String title) { this.title = title; return this; }
	public JPaintingVariant author(String author) { this.author = author; return this; }

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Identifier getAssetId() { return assetId; }
	public String getTitle() { return title; }
	public String getAuthor() { return author; }
}
