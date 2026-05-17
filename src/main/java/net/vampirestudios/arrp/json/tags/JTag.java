package net.vampirestudios.arrp.json.tags;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;

public class JTag {
	public static final Codec<JTag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("replace").orElse(false).forGetter(JTag::getReplace),
			Codec.STRING.listOf().fieldOf("values").forGetter(JTag::getValues)
	).apply(instance, JTag::new));

	private boolean replace;
	private List<String> values = new ArrayList<>();

	/**
	 * @see #tag()
	 * @see #tag(Identifier)
	 */
	public JTag() {

	}

	public JTag(boolean replace, List<String> values) {
		this.replace = replace;
		this.values = values;
	}

	public static JTag replacingTag() {
		return tag().replace();
	}

	public static JTag tag() {
		return new JTag();
	}

	public static JTag of(Identifier... identifiers) {
		JTag tag = new JTag();
		for (Identifier id : identifiers) tag.add(id);
		return tag;
	}

	public JTag replace() {
		return replace(true);
	}

	public JTag replace(boolean replace) {
		this.replace = replace;
		return this;
	}

	/**
	 * add a normal item to the tag
	 */
	public JTag add(Identifier identifier) {
		this.values.add(identifier.toString());
		return this;
	}

	/**
	 * add a tag to the tag
	 */
	public JTag tag(Identifier tag) {
		this.values.add('#' + tag.getNamespace() + ':' + tag.getPath());
		return this;
	}

	@Override
	public JTag clone() {
		try {
			return (JTag) super.clone();
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
