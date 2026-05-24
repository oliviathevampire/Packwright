package net.vampirestudios.arrp.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Advancement {
	private static final Codec<Map<String, Criterion>> CRITERIA = Codec.unboundedMap(Codec.STRING, Criterion.CODEC);
	public static final Codec<Advancement> CODEC = RecordCodecBuilder.<Advancement>create(i -> i.group(
			Identifier.CODEC.optionalFieldOf("parent").forGetter(a -> Optional.ofNullable(a.parent)),
			Display.CODEC.optionalFieldOf("display").forGetter(a -> Optional.ofNullable(a.display)),
			CRITERIA.fieldOf("criteria").forGetter(a -> a.criteria),
			Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("requirements").forGetter(a -> Optional.ofNullable(a.requirements)),
			Rewards.CODEC.optionalFieldOf("rewards").forGetter(a -> Optional.ofNullable(a.rewards)),
			Codec.BOOL.optionalFieldOf("sends_telemetry_event").forGetter(a -> Optional.ofNullable(a.sendsTelemetryEvent))
	).apply(i, (op, od, criteria, oreq, orw, otel) -> {
		Advancement a = new Advancement();
		a.parent = op.orElse(null);
		a.display = od.orElse(null);
		a.criteria = criteria != null ? criteria : new LinkedHashMap<>();
		a.requirements = oreq.orElse(null);
		a.rewards = orw.orElse(null);
		a.sendsTelemetryEvent = otel.orElse(null);
		return a;
	})).validate(a -> {
		if ((a.requirements == null || a.requirements.isEmpty()) && !a.criteria.isEmpty()) {
			a.requirements = a.criteria.keySet().stream().map(List::of).toList();
		}
		return a.criteria.isEmpty()
				? DataResult.error(() -> "advancement: 'criteria' must not be empty")
				: DataResult.success(a);
	});

	private Identifier parent;
	private Display display;
	private Map<String, Criterion> criteria = new LinkedHashMap<>();
	private List<List<String>> requirements;
	private Rewards rewards;
	private Boolean sendsTelemetryEvent;

	public Advancement parent(Identifier id) {
		this.parent = id;
		return this;
	}

	public Advancement parent(String id) {
		return parent(Identifier.tryParse(id));
	}

	public Advancement display(Display d) {
		this.display = d;
		return this;
	}

	public Advancement criterion(String key, Criterion c) {
		this.criteria.put(key, c);
		return this;
	}

	public Advancement requirements(List<List<String>> req) {
		this.requirements = req;
		return this;
	}

	public Advancement rewards(Rewards r) {
		this.rewards = r;
		return this;
	}

	public Advancement telemetry(boolean v) {
		this.sendsTelemetryEvent = v;
		return this;
	}
}
