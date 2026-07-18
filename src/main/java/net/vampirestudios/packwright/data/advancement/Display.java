package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.StringRepresentable;
import net.vampirestudios.packwright.util.DynamicMap;

/**
 * {@code title}/{@code description} are vanilla text components, but this project can't depend
 * on vanilla's actual {@code Component}/{@code ComponentSerialization.CODEC} to build or encode
 * one — that requires the game's registries to be bootstrapped ({@code Bootstrap.bootStrap()}),
 * which this standalone data-generation tool never runs (fails with e.g. "Not bootstrapped
 * (called from registry minecraft:game_event)"). {@link DynamicMap} builds the same JSON shape
 * by hand instead.
 */
public final class Display {
	public static final Codec<Display> CODEC = RecordCodecBuilder.create(i -> i.group(
			Icon.CODEC.fieldOf("icon").forGetter(d -> d.icon),
			DynamicMap.CODEC.fieldOf("title").forGetter(d -> d.title),
			DynamicMap.CODEC.fieldOf("description").forGetter(d -> d.description),
			Codec.STRING.optionalFieldOf("background").forGetter(d -> Optional.ofNullable(d.background)),
			Frame.CODEC.optionalFieldOf("frame", Frame.TASK).forGetter(d -> d.frame),
			Codec.BOOL.optionalFieldOf("show_toast").forGetter(d -> Optional.ofNullable(d.showToast)),
			Codec.BOOL.optionalFieldOf("announce_to_chat").forGetter(d -> Optional.ofNullable(d.announceChat)),
			Codec.BOOL.optionalFieldOf("hidden").forGetter(d -> Optional.ofNullable(d.hidden))
	).apply(i, (icon, title, desc, bg, frame, toast, announce, hidden) -> {
		Display d = new Display();
		d.icon = icon;
		d.title = title;
		d.description = desc;
		d.background = bg.orElse(null);
		d.frame = frame;
		d.showToast = toast.orElse(null);
		d.announceChat = announce.orElse(null);
		d.hidden = hidden.orElse(null);
		return d;
	}));

	private Icon icon;
	private DynamicMap title;
	private DynamicMap description;
	private String background;
	private Frame frame = Frame.TASK;
	private Boolean showToast, announceChat, hidden;

	public Display icon(Icon icon) { this.icon = icon; return this; }
	/** an advanced/translatable title; build with e.g. {@code DynamicMap.object().set("translate", "...")} */
	public Display title(DynamicMap title) { this.title = title; return this; }
	public Display title(String plainText) { this.title = DynamicMap.object().set("text", plainText); return this; }
	/** an advanced/translatable description; build with e.g. {@code DynamicMap.object().set("translate", "...")} */
	public Display description(DynamicMap description) { this.description = description; return this; }
	public Display description(String plainText) { this.description = DynamicMap.object().set("text", plainText); return this; }
	public Display background(String background) { this.background = background; return this; }
	public Display frame(Frame frame) { this.frame = frame; return this; }
	public Display showToast(Boolean showToast) { this.showToast = showToast; return this; }
	public Display announceChat(Boolean announceChat) { this.announceChat = announceChat; return this; }
	public Display hidden(Boolean hidden) { this.hidden = hidden; return this; }

	public Icon getIcon() { return icon; }
	public DynamicMap getTitle() { return title; }
	public DynamicMap getDescription() { return description; }
	public String getBackground() { return background; }
	public Frame getFrame() { return frame; }
	public Boolean getShowToast() { return showToast; }
	public Boolean getAnnounceChat() { return announceChat; }
	public Boolean getHidden() { return hidden; }

	public enum Frame implements StringRepresentable {
		TASK("task"),
		GOAL("goal"),
		CHALLENGE("challenge");

		public static final Codec<Frame> CODEC = StringRepresentable.fromEnum(Frame::values);

		private final String id;
		Frame(String id) { this.id = id; }

		@Override
		public String getSerializedName() { return id; }
	}
}
