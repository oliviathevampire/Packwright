package net.vampirestudios.packwright.data.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;

import java.util.List;

/** a single enchantment, a {@code #tag}, or an explicit list of enchantments */
public final class Enchantments {
	public static final Codec<Enchantments> CODEC = Codec.either(Codec.STRING, Codec.STRING.listOf())
			.comapFlatMap(
					either -> either.map(Enchantments::fromString, Enchantments::fromList),
					value -> value.tag != null
							? Either.left("#" + value.tag)
							: (value.ids.size() == 1
							   ? Either.left(value.ids.getFirst().toString())
							   : Either.right(value.ids.stream().map(Identifier::toString).toList())
							)
			);

	private final List<Identifier> ids;
	private final Identifier tag;

	private Enchantments(List<Identifier> ids, Identifier tag) {
		this.ids = ids;
		this.tag = tag;
	}

	public static Enchantments enchantment(Identifier id) {
		return new Enchantments(List.of(id), null);
	}

	public static Enchantments tag(Identifier tag) {
		return new Enchantments(List.of(), tag);
	}

	public static Enchantments list(List<Identifier> ids) {
		return new Enchantments(List.copyOf(ids), null);
	}

	public List<Identifier> getIds() { return ids; }
	public Identifier getTag() { return tag; }

	private static DataResult<Enchantments> fromString(String value) {
		if (value == null || value.isBlank()) {
			return DataResult.error(() -> "Enchantments cannot be empty");
		}
		try {
			return value.startsWith("#")
					? DataResult.success(Enchantments.tag(Identifier.parse(value.substring(1))))
					: DataResult.success(Enchantments.enchantment(Identifier.parse(value)));
		} catch (Exception e) {
			return DataResult.error(() -> "Invalid enchantment id: " + value);
		}
	}

	private static DataResult<Enchantments> fromList(List<String> values) {
		if (values == null || values.isEmpty()) {
			return DataResult.error(() -> "Enchantments list cannot be empty");
		}
		try {
			return DataResult.success(Enchantments.list(values.stream().map(Identifier::parse).toList()));
		} catch (Exception e) {
			return DataResult.error(() -> "Invalid enchantment id in list: " + values);
		}
	}
}
