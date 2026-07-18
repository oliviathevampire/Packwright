package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * Removes sprites matching the given pattern from the atlas. Only affects
 * sources listed before this one. {@code namespace} and {@code path} are
 * regular expressions; an absent field matches everything.
 */
public final class SourceFilter extends AtlasSource {
	public static final String TYPE = "filter";

	private record Pattern(Optional<String> namespace, Optional<String> path) {
		static final Codec<Pattern> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.optionalFieldOf("namespace").forGetter(Pattern::namespace),
				Codec.STRING.optionalFieldOf("path").forGetter(Pattern::path)
		).apply(i, Pattern::new));
	}

	public static final Codec<SourceFilter> CODEC = RecordCodecBuilder.create(i -> i.group(
			Pattern.CODEC.fieldOf("pattern").forGetter(s ->
					new Pattern(Optional.ofNullable(s.namespace), Optional.ofNullable(s.path)))
	).apply(i, pattern -> {
		SourceFilter out = new SourceFilter();
		pattern.namespace().ifPresent(v -> out.namespace = v);
		pattern.path().ifPresent(v -> out.path = v);
		return out;
	}));

	static {
		AtlasSource.register(TYPE, CODEC);
	}

	private String namespace;
	private String path;

	public SourceFilter() {
		super(TYPE);
	}

	/** regular expression matched against sprite namespaces */
	public SourceFilter namespace(String namespaceRegex) {
		this.namespace = namespaceRegex;
		return this;
	}

	/** regular expression matched against sprite paths */
	public SourceFilter path(String pathRegex) {
		this.path = pathRegex;
		return this;
	}

	public String getNamespace() { return namespace; }
	public String getPath() { return path; }
}
