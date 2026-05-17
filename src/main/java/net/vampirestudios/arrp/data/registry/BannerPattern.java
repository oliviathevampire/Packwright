package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class BannerPattern {
	public static final Codec<BannerPattern> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			Codec.STRING.fieldOf("translation_key").forGetter(x -> x.translationKey)
	).apply(i, (assetId, translationKey) -> {
		BannerPattern out = new BannerPattern();
		out.assetId = assetId;
		out.translationKey = translationKey;
		return out;
	}));

	private Identifier assetId;
	private String translationKey;

	public static BannerPattern bannerPattern() {
		return new BannerPattern();
	}

	public BannerPattern assetId(Identifier id) { this.assetId = id; return this; }
	public BannerPattern translationKey(String key) { this.translationKey = key; return this; }

	public Identifier getAssetId() { return assetId; }
	public String getTranslationKey() { return translationKey; }
}
