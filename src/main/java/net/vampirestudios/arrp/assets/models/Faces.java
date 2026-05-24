package net.vampirestudios.arrp.assets.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class Faces {
	public static final Codec<Faces> CODEC = RecordCodecBuilder.create(i -> i.group(
			Face.CODEC.optionalFieldOf("up").forGetter(f -> Optional.ofNullable(f.up)),
			Face.CODEC.optionalFieldOf("down").forGetter(f -> Optional.ofNullable(f.down)),
			Face.CODEC.optionalFieldOf("north").forGetter(f -> Optional.ofNullable(f.north)),
			Face.CODEC.optionalFieldOf("south").forGetter(f -> Optional.ofNullable(f.south)),
			Face.CODEC.optionalFieldOf("east").forGetter(f -> Optional.ofNullable(f.east)),
			Face.CODEC.optionalFieldOf("west").forGetter(f -> Optional.ofNullable(f.west))
	).apply(i, (up, down, north, south, east, west) -> {
		Faces faces = new Faces();
		up.ifPresent(faces::up);
		down.ifPresent(faces::down);
		north.ifPresent(faces::north);
		south.ifPresent(faces::south);
		east.ifPresent(faces::east);
		west.ifPresent(faces::west);
		return faces;
	}));

	private Face up;
	private Face down;
	private Face north;
	private Face south;
	private Face east;
	private Face west;

	/**
	 * @see Model#faces()
	 */
	public Faces() {}

	public Faces up(Face face)    { this.up    = face; return this; }
	public Faces down(Face face)  { this.down  = face; return this; }
	public Faces north(Face face) { this.north = face; return this; }
	public Faces south(Face face) { this.south = face; return this; }
	public Faces east(Face face)  { this.east  = face; return this; }
	public Faces west(Face face)  { this.west  = face; return this; }

	public static Faces allSame(Face face) {
		Faces faces = new Faces();
		faces.up(face);
		faces.down(face);
		faces.north(face);
		faces.south(face);
		faces.east(face);
		faces.west(face);
		return faces;
	}
}
