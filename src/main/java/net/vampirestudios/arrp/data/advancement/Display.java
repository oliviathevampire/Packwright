package net.vampirestudios.arrp.data.advancement;// imports (Yarn names)

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.StringRepresentable;

public final class Display {
	public static final Codec<Display> CODEC = RecordCodecBuilder.create(i -> i.group(
			Icon.CODEC.fieldOf("icon").forGetter(d -> d.icon),
			ComponentSerialization.CODEC.fieldOf("title").forGetter(d -> d.title),
			ComponentSerialization.CODEC.fieldOf("description").forGetter(d -> d.description),
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
	public Icon icon;
	public Component title;
	public Component description;
	public String background;
	public Frame frame = Frame.TASK;
	public Boolean showToast, announceChat, hidden;

	public Display icon(Icon icon) {
		this.icon = icon;
		return this;
	}

	public Display title(Component title) {
		this.title = title;
		return this;
	}

	public Display description(Component description) {
		this.description = description;
		return this;
	}

	public Display background(String background) {
		this.background = background;
		return this;
	}

	public Display frame(Frame frame) {
		this.frame = frame;
		return this;
	}

	public Display showToast(Boolean showToast) {
		this.showToast = showToast;
		return this;
	}

	public Display announceChat(Boolean announceChat) {
		this.announceChat = announceChat;
		return this;
	}

	public Display hidden(Boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public enum Frame implements StringRepresentable {
		TASK("task"),
		GOAL("goal"),
		CHALLENGE("challenge");

		public static final Codec<Frame> CODEC = StringRepresentable.fromEnum(Frame::values);

		private final String id;

		Frame(String id) {
			this.id = id;
		}

		@Override
		public String getSerializedName() {
			return id;
		}
	}
}
