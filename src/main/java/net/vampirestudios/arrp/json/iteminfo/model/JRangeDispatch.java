package net.vampirestudios.arrp.json.iteminfo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a range dispatch model type.
 */
public class JRangeDispatch extends JItemModel {
	public static final String TYPE = "minecraft:range_dispatch";

	private List<JRangeEntry> entries;
	private double period;
	private String property;
	private double scale;

	public JRangeDispatch() {
		super(TYPE);
		this.entries = new ArrayList<>();
	}

	public JRangeDispatch(List<JRangeEntry> entries, double period, String property, double scale) {
		this();
		this.entries = entries;
		this.period = period;
		this.property = property;
		this.scale = scale;
	}

	// Getters and Setters
	public List<JRangeEntry> getEntries() {
		return entries;
	}

	public JRangeDispatch entries(List<JRangeEntry> entries) {
		this.entries = entries;
		return this;
	}

	public JRangeDispatch entry(JRangeEntry entry) {
		this.entries.add(entry);
		return this;
	}

	public double getPeriod() {
		return period;
	}

	public JRangeDispatch period(double period) {
		this.period = period;
		return this;
	}

	public String getProperty() {
		return property;
	}

	public JRangeDispatch property(String property) {
		this.property = property;
		return this;
	}

	public double getScale() {
		return scale;
	}

	public JRangeDispatch scale(double scale) {
		this.scale = scale;
		return this;
	}
}
