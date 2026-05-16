package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.List;

public record JPattern(String... rows) {
	public static final Codec<JPattern> CODEC = Codec.STRING.listOf().xmap(
			rows -> new JPattern(rows.toArray(String[]::new)),
			JPattern::getRows
	);

	public static JPattern pattern(String... rows) {
		return new JPattern(rows);
	}

	public List<String> getRows() {
		return Arrays.stream(rows).toList();
	}
}
