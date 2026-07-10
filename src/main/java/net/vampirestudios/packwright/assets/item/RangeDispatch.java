package net.vampirestudios.packwright.assets.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a range dispatch model type.
 */
public class RangeDispatch extends ItemModel {
	public static final String TYPE = "minecraft:range_dispatch";

	private List<RangeEntry> entries;
	private double period;
	private String property;
	private double scale;

	public RangeDispatch() {
		super(TYPE);
		this.entries = new ArrayList<>();
	}

	public RangeDispatch(List<RangeEntry> entries, double period, String property, double scale) {
		this();
		this.entries = entries;
		this.period = period;
		this.property = property;
		this.scale = scale;
	}

	// Getters and Setters
	public List<RangeEntry> getEntries() {
		return entries;
	}

	public RangeDispatch entries(List<RangeEntry> entries) {
		this.entries = entries;
		return this;
	}

	public RangeDispatch entry(RangeEntry entry) {
		this.entries.add(entry);
		return this;
	}

	public double getPeriod() {
		return period;
	}

	public RangeDispatch period(double period) {
		this.period = period;
		return this;
	}

	public String getProperty() {
		return property;
	}

	public RangeDispatch property(String property) {
		this.property = property;
		return this;
	}

	public double getScale() {
		return scale;
	}

	public RangeDispatch scale(double scale) {
		this.scale = scale;
		return this;
	}
}
