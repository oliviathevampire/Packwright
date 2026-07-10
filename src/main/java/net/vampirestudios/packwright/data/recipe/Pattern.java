package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.List;

public record Pattern(String... rows) {
	public static final Codec<Pattern> CODEC = Codec.STRING.listOf().xmap(
			rows -> new Pattern(rows.toArray(String[]::new)),
			Pattern::getRows
	);

	public static Pattern pattern(String... rows) {
		return new Pattern(rows);
	}

	public List<String> getRows() {
		return Arrays.stream(rows).toList();
	}
}
