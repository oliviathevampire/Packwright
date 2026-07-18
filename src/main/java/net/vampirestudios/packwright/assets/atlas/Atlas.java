package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * An atlas configuration file ({@code assets/<namespace>/atlases/<name>.json}).
 * Atlas files with the same name are merged across packs, so a mod usually adds
 * sources to a vanilla atlas like {@code minecraft:blocks} rather than creating
 * its own.
 *
 * @see #atlas()
 * @see AtlasSource
 */
public class Atlas {
	public static final Codec<Atlas> CODEC = RecordCodecBuilder.create(i -> i.group(
			AtlasSource.CODEC.listOf().fieldOf("sources").forGetter(a -> List.copyOf(a.sources))
	).apply(i, sources -> {
		Atlas atlas = new Atlas();
		atlas.sources.addAll(sources);
		return atlas;
	}));

	private final List<AtlasSource> sources = new ArrayList<>();

	public static Atlas atlas() {
		return new Atlas();
	}

	public Atlas source(AtlasSource source) {
		this.sources.add(source);
		return this;
	}

	public List<AtlasSource> getSources() { return sources; }
}
