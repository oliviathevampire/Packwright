package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Lists all files in a directory and its subdirectories, across all namespaces.
 * {@code source} is the directory inside {@code textures/} to read from,
 * {@code prefix} is prepended to the sprite name.
 */
public final class SourceDirectory extends AtlasSource {
	public static final String TYPE = "directory";
	public static final Codec<SourceDirectory> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("source").forGetter(SourceDirectory::getSource),
			Codec.STRING.fieldOf("prefix").forGetter(SourceDirectory::getPrefix)
	).apply(i, SourceDirectory::new));

	static {
		AtlasSource.register(TYPE, CODEC);
	}

	private final String source;
	private final String prefix;

	public SourceDirectory(String source, String prefix) {
		super(TYPE);
		this.source = source;
		this.prefix = prefix;
	}

	public String getSource() { return source; }
	public String getPrefix() { return prefix; }
}
