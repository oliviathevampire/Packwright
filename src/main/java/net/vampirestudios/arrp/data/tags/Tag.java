package net.vampirestudios.arrp.data.tags;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;

public class Tag {
	public static final Codec<Tag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("replace").orElse(false).forGetter(Tag::getReplace),
			Codec.STRING.listOf().fieldOf("values").forGetter(Tag::getValues)
	).apply(instance, Tag::new));

	private boolean replace;
	private List<String> values = new ArrayList<>();

	/**
	 * @see #tag()
	 * @see #tag(Identifier)
	 */
	public Tag() {

	}

	public Tag(boolean replace, List<String> values) {
		this.replace = replace;
		this.values = values;
	}

	public static Tag replacingTag() {
		return tag().replace();
	}

	public static Tag tag() {
		return new Tag();
	}

	public static Tag of(Identifier... identifiers) {
		Tag tag = new Tag();
		for (Identifier id : identifiers) tag.add(id);
		return tag;
	}

	public Tag replace() {
		return replace(true);
	}

	public Tag replace(boolean replace) {
		this.replace = replace;
		return this;
	}

	/**
	 * add a normal item to the tag
	 */
	public Tag add(Identifier identifier) {
		this.values.add(identifier.toString());
		return this;
	}

	/**
	 * add a tag to the tag
	 */
	public Tag tag(Identifier tag) {
		this.values.add('#' + tag.getNamespace() + ':' + tag.getPath());
		return this;
	}

	@Override
	public Tag clone() {
		try {
			return (Tag) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	public boolean getReplace() {
		return replace;
	}

	public List<String> getValues() {
		return List.copyOf(values);
	}
}
