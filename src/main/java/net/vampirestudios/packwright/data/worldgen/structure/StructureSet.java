package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@code worldgen/structure_set} file: a weighted list of structures plus a
 * {@link StructurePlacement} rule.
 */
public class StructureSet {

	public static final Codec<StructureSet> CODEC = Codec.PASSTHROUGH.comapFlatMap(StructureSet::fromDynamic, StructureSet::toDynamic);

	private final List<Entry> structures = new ArrayList<>();
	private StructurePlacement placement;

	public static StructureSet set() {
		return new StructureSet();
	}

	// Core setters

	public StructureSet structures(List<Entry> structures) {
		this.structures.clear();
		if (structures != null) this.structures.addAll(structures);
		return this;
	}

	public StructureSet placement(StructurePlacement placement) {
		this.placement = placement;
		return this;
	}

	public StructureSet randomSpreadPlacement(int salt, int spacing, int separation) {
		return placement(StructurePlacement.randomSpread(salt, spacing, separation));
	}

	public StructureSet dimensionOriginPlacement() {
		return placement(StructurePlacement.dimensionOrigin());
	}

	// Convenience helpers

	public StructureSet addStructure(Identifier id, int weight) {
		this.structures.add(new Entry(id, weight));
		return this;
	}

	public static StructureSet randomSpread(Identifier structureId, int weight, int salt, int spacing, int separation) {
		return StructureSet.set()
				.addStructure(structureId, weight)
				.randomSpreadPlacement(salt, spacing, separation);
	}

	public List<Entry> getStructures() {
		return List.copyOf(this.structures);
	}

	public StructurePlacement getPlacement() {
		return this.placement;
	}

	// Codec plumbing (plain Java values, no JSON)

	private Dynamic<?> toDynamic() {
		Map<String, Object> out = new LinkedHashMap<>();
		List<Object> structureList = new ArrayList<>();
		for (Entry entry : this.structures) {
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("structure", entry.structure().toString());
			item.put("weight", entry.weight());
			structureList.add(item);
		}
		out.put("structures", structureList);
		if (this.placement != null) {
			out.put("placement", StructurePlacement.CODEC.encodeStart(JavaOps.INSTANCE, this.placement).getOrThrow());
		}
		return new Dynamic<>(JavaOps.INSTANCE, out);
	}

	private static DataResult<StructureSet> fromDynamic(Dynamic<?> dynamic) {
		Object raw = dynamic.convert(JavaOps.INSTANCE).getValue();
		if (!(raw instanceof Map<?, ?> map)) {
			return DataResult.error(() -> "Structure set must be an object");
		}
		StructureSet set = new StructureSet();
		if (map.get("structures") instanceof List<?> list) {
			for (Object element : list) {
				if (element instanceof Map<?, ?> item) {
					Identifier structure = Identifier.tryParse(String.valueOf(item.get("structure")));
					int weight = item.get("weight") instanceof Number number ? number.intValue() : 1;
					set.structures.add(new Entry(structure, weight));
				}
			}
		}
		if (map.get("placement") != null) {
			set.placement = StructurePlacement.CODEC.parse(new Dynamic<>(JavaOps.INSTANCE, map.get("placement"))).result().orElse(null);
		}
		return DataResult.success(set);
	}

	/** one weighted structure entry: {@code { "structure": id, "weight": n }} */
	public record Entry(Identifier structure, int weight) {
	}
}
