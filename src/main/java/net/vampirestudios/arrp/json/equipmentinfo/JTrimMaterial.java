package net.vampirestudios.arrp.json.equipmentinfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class JTrimMaterial {
	public static final Codec<JTrimMaterial> CODEC = RecordCodecBuilder.create(i -> i.group(
			AssetGroup.MAP_CODEC.forGetter(x -> x.assets),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description)
	).apply(i, (assets, description) -> {
		JTrimMaterial out = new JTrimMaterial();
		out.assets = assets;
		out.description = description;
		return out;
	}));

	private AssetGroup assets;
	private String description;

	public static JTrimMaterial trimMaterial() {
		return new JTrimMaterial();
	}

	public JTrimMaterial assets(AssetGroup assets) { this.assets = assets; return this; }
	public JTrimMaterial assetName(String assetName) { this.assets = AssetGroup.assetGroup(assetName); return this; }
	public JTrimMaterial description(String description) { this.description = description; return this; }

	public AssetGroup getAssets() { return assets; }
	public String getDescription() { return description; }

	public static class AssetGroup {
		public static final MapCodec<AssetGroup> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				AssetInfo.CODEC.fieldOf("asset_name").forGetter(x -> x.base),
				Codec.unboundedMap(Identifier.CODEC, AssetInfo.CODEC)
						.optionalFieldOf("override_armor_assets", Map.of())
						.forGetter(x -> x.overrides)
		).apply(i, (base, overrides) -> {
			AssetGroup out = new AssetGroup();
			out.base = base;
			out.overrides.putAll(overrides);
			return out;
		}));
		public static final Codec<AssetGroup> CODEC = MAP_CODEC.codec();

		private AssetInfo base;
		private final Map<Identifier, AssetInfo> overrides = new LinkedHashMap<>();

		public static AssetGroup assetGroup(String assetName) {
			return new AssetGroup().assetName(assetName);
		}

		public AssetGroup assetName(String assetName) {
			this.base = new AssetInfo(assetName);
			return this;
		}

		public AssetGroup assetName(AssetInfo assetName) {
			this.base = assetName;
			return this;
		}

		public AssetGroup overrideArmorAsset(Identifier equipmentAsset, String assetName) {
			this.overrides.put(equipmentAsset, new AssetInfo(assetName));
			return this;
		}

		public AssetGroup overrideArmorAsset(Identifier equipmentAsset, AssetInfo assetName) {
			this.overrides.put(equipmentAsset, assetName);
			return this;
		}

		public AssetInfo getBase() { return base; }
		public Map<Identifier, AssetInfo> getOverrides() { return overrides; }
	}

	public static class AssetInfo {
		public static final Codec<AssetInfo> CODEC = Codec.STRING.xmap(AssetInfo::new, AssetInfo::getSuffix);

		private final String suffix;

		public AssetInfo(String suffix) {
			if (!Identifier.isValidPath(suffix)) {
				throw new IllegalArgumentException("Invalid trim asset suffix: " + suffix);
			}
			this.suffix = suffix;
		}

		public String getSuffix() {
			return suffix;
		}
	}
}
