package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Copies rectangular regions from one big texture into separate sprites.
 * Region coordinates are divided by {@code divisor_x}/{@code divisor_y} and
 * then multiplied by the actual image size, so regions can be specified in a
 * resolution-independent grid.
 */
public final class SourceUnstitch extends AtlasSource {
	public static final String TYPE = "unstitch";

	/**
	 * a rectangular region of the source image, in grid units
	 * (divided by the divisors of the owning {@link SourceUnstitch})
	 */
	public record Region(Identifier sprite, double x, double y, double width, double height) {
		public static final Codec<Region> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("sprite").forGetter(Region::sprite),
				Codec.DOUBLE.fieldOf("x").forGetter(Region::x),
				Codec.DOUBLE.fieldOf("y").forGetter(Region::y),
				Codec.DOUBLE.fieldOf("width").forGetter(Region::width),
				Codec.DOUBLE.fieldOf("height").forGetter(Region::height)
		).apply(i, Region::new));
	}

	public static final Codec<SourceUnstitch> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("resource").forGetter(SourceUnstitch::getResource),
			Codec.DOUBLE.fieldOf("divisor_x").forGetter(SourceUnstitch::getDivisorX),
			Codec.DOUBLE.fieldOf("divisor_y").forGetter(SourceUnstitch::getDivisorY),
			Region.CODEC.listOf().fieldOf("regions").forGetter(s -> List.copyOf(s.regions))
	).apply(i, (resource, divisorX, divisorY, regions) -> {
		SourceUnstitch out = new SourceUnstitch(resource, divisorX, divisorY);
		out.regions.addAll(regions);
		return out;
	}));

	static {
		AtlasSource.register(TYPE, CODEC);
	}

	private final Identifier resource;
	private final double divisorX;
	private final double divisorY;
	private final List<Region> regions = new ArrayList<>();

	public SourceUnstitch(Identifier resource, double divisorX, double divisorY) {
		super(TYPE);
		this.resource = resource;
		this.divisorX = divisorX;
		this.divisorY = divisorY;
	}

	public SourceUnstitch region(Identifier sprite, double x, double y, double width, double height) {
		this.regions.add(new Region(sprite, x, y, width, height));
		return this;
	}

	public Identifier getResource() { return resource; }
	public double getDivisorX() { return divisorX; }
	public double getDivisorY() { return divisorY; }
	public List<Region> getRegions() { return regions; }
}
