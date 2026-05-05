package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JBannerPattern {
	public static final Codec<JBannerPattern> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
			Codec.STRING.fieldOf("translation_key").forGetter(x -> x.translationKey)
	).apply(i, (assetId, translationKey) -> {
		JBannerPattern out = new JBannerPattern();
		out.assetId = assetId;
		out.translationKey = translationKey;
		return out;
	}));

	private Identifier assetId;
	private String translationKey;

	public static JBannerPattern bannerPattern() {
		return new JBannerPattern();
	}

	public JBannerPattern assetId(Identifier id) { this.assetId = id; return this; }
	public JBannerPattern translationKey(String key) { this.translationKey = key; return this; }

	public Identifier getAssetId() { return assetId; }
	public String getTranslationKey() { return translationKey; }
}
