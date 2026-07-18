package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * Includes all providers of another font file. Each font is only ever loaded
 * once per pack, so this can be used to share providers between fonts.
 */
public final class ProviderReference extends FontProvider {
	public static final String TYPE = "reference";
	public static final Codec<ProviderReference> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(ProviderReference::getId)
	).apply(i, ProviderReference::new));

	static {
		FontProvider.register(TYPE, CODEC);
	}

	private final Identifier id;

	public ProviderReference(Identifier id) {
		super(TYPE);
		this.id = id;
	}

	public Identifier getId() { return id; }
}
