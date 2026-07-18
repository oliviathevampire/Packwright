package net.vampirestudios.packwright.data.loot.functions;

public record TooltipToggle(String component, boolean show) {
	public static TooltipToggle show(String component) {
		return new TooltipToggle(component, true);
	}

	public static TooltipToggle hide(String component) {
		return new TooltipToggle(component, false);
	}
}
