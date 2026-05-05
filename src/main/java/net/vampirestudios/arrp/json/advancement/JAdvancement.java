// src/main/java/arrp/adv/JAdvancement.java
package net.vampirestudios.arrp.json.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class JAdvancement {
	// ---- Codecs ----
	private static final Codec<Map<String, JCriterion>> CRITERIA = Codec.unboundedMap(Codec.STRING, JCriterion.CODEC);
	public static final Codec<JAdvancement> CODEC = RecordCodecBuilder.<JAdvancement>create(i -> i.group(
			Codec.STRING.optionalFieldOf("parent").forGetter(a -> Optional.ofNullable(a.parent)),
			JDisplay.CODEC.optionalFieldOf("display").forGetter(a -> Optional.ofNullable(a.display)),
			CRITERIA.fieldOf("criteria").forGetter(a -> a.criteria),
			Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("requirements").forGetter(a -> Optional.ofNullable(a.requirements)),
			JRewards.CODEC.optionalFieldOf("rewards").forGetter(a -> Optional.ofNullable(a.rewards)),
			Codec.BOOL.optionalFieldOf("sends_telemetry_event").forGetter(a -> Optional.ofNullable(a.sendsTelemetryEvent))
	).apply(i, (op, od, criteria, oreq, orw, otel) -> {
		JAdvancement a = new JAdvancement();
		a.parent = op.orElse(null);
		a.display = od.orElse(null);
		a.criteria = criteria != null ? criteria : new LinkedHashMap<>();
		a.requirements = oreq.orElse(null);
		a.rewards = orw.orElse(null);
		a.sendsTelemetryEvent = otel.orElse(null);
		return a;
	})).validate(a -> {
		// requirements default: AND of each criterion
		if ((a.requirements == null || a.requirements.isEmpty()) && !a.criteria.isEmpty()) {
			a.requirements = a.criteria.keySet().stream().map(List::of).toList();
		}
		return a.criteria.isEmpty()
				? DataResult.error(() -> "advancement: 'criteria' must not be empty")
				: DataResult.success(a);
	});
	public String parent;                              // optional
	public JDisplay display;                           // optional
	public Map<String, JCriterion> criteria = new LinkedHashMap<>(); // required by vanilla
	public List<List<String>> requirements;            // optional (defaults to AND of all criteria)
	public JRewards rewards;                           // optional
	public Boolean sendsTelemetryEvent;                // optional (pre-1.21 era; keep optional)

	// ---- Builders ----
	public JAdvancement parent(String id) {
		this.parent = id;
		return this;
	}

	public JAdvancement display(JDisplay d) {
		this.display = d;
		return this;
	}

	public JAdvancement criterion(String key, JCriterion c) {
		this.criteria.put(key, c);
		return this;
	}

	public JAdvancement requirements(List<List<String>> req) {
		this.requirements = req;
		return this;
	}

	public JAdvancement rewards(JRewards r) {
		this.rewards = r;
		return this;
	}

	public JAdvancement telemetry(boolean v) {
		this.sendsTelemetryEvent = v;
		return this;
	}
}
