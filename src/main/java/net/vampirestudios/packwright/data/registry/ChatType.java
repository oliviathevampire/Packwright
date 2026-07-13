package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A chat type in the {@code chat_type} registry: how messages of this type render
 * in chat ({@code chat}) and how they are read aloud ({@code narration}).
 *
 * <pre>{@code
 * ChatType.chatType(
 *     Decoration.of("chat.type.mymod.whisper", "sender", "content").style(Style.style().color("gray").italic(true)),
 *     Decoration.of("chat.type.text.narrate", "sender", "content"))
 * }</pre>
 */
public class ChatType {
	public static final Codec<ChatType> CODEC = RecordCodecBuilder.create(i -> i.group(
			Decoration.CODEC.fieldOf("chat").forGetter(x -> x.chat),
			Decoration.CODEC.fieldOf("narration").forGetter(x -> x.narration)
	).apply(i, (chat, narration) -> {
		ChatType out = new ChatType();
		out.chat = chat;
		out.narration = narration;
		return out;
	}));

	private Decoration chat;
	private Decoration narration;

	public static ChatType chatType() {
		return new ChatType();
	}

	public static ChatType chatType(Decoration chat, Decoration narration) {
		return chatType().chat(chat).narration(narration);
	}

	public ChatType chat(Decoration chat) {
		this.chat = chat;
		return this;
	}

	public ChatType narration(Decoration narration) {
		this.narration = narration;
		return this;
	}

	public Decoration getChat() {
		return chat;
	}

	public Decoration getNarration() {
		return narration;
	}

	/**
	 * One decoration: a translation key whose placeholders are filled from
	 * {@code parameters} ({@code sender}, {@code content} and/or {@code target}),
	 * plus an optional text style.
	 */
	public static class Decoration {
		public static final Codec<Decoration> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("translation_key").forGetter(x -> x.translationKey),
				Codec.STRING.listOf().fieldOf("parameters").forGetter(x -> List.copyOf(x.parameters)),
				Style.CODEC.optionalFieldOf("style").forGetter(x -> Optional.ofNullable(x.style))
		).apply(i, (translationKey, parameters, style) -> {
			Decoration out = new Decoration();
			out.translationKey = translationKey;
			out.parameters = new ArrayList<>(parameters);
			out.style = style.orElse(null);
			return out;
		}));

		private String translationKey;
		private List<String> parameters = new ArrayList<>();
		private Style style;

		public static Decoration of(String translationKey, String... parameters) {
			Decoration out = new Decoration();
			out.translationKey = translationKey;
			out.parameters.addAll(List.of(parameters));
			return out;
		}

		public Decoration translationKey(String translationKey) {
			this.translationKey = translationKey;
			return this;
		}

		public Decoration parameter(String parameter) {
			this.parameters.add(parameter);
			return this;
		}

		public Decoration style(Style style) {
			this.style = style;
			return this;
		}

		public String getTranslationKey() {
			return translationKey;
		}

		public List<String> getParameters() {
			return List.copyOf(parameters);
		}

		public Style getStyle() {
			return style;
		}
	}

	/** an optional text style applied to the decorated message */
	public static class Style {
		public static final Codec<Style> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.optionalFieldOf("color").forGetter(x -> Optional.ofNullable(x.color)),
				Codec.BOOL.optionalFieldOf("bold").forGetter(x -> Optional.ofNullable(x.bold)),
				Codec.BOOL.optionalFieldOf("italic").forGetter(x -> Optional.ofNullable(x.italic)),
				Codec.BOOL.optionalFieldOf("underlined").forGetter(x -> Optional.ofNullable(x.underlined)),
				Codec.BOOL.optionalFieldOf("strikethrough").forGetter(x -> Optional.ofNullable(x.strikethrough)),
				Codec.BOOL.optionalFieldOf("obfuscated").forGetter(x -> Optional.ofNullable(x.obfuscated)),
				Codec.STRING.optionalFieldOf("font").forGetter(x -> Optional.ofNullable(x.font))
		).apply(i, (color, bold, italic, underlined, strikethrough, obfuscated, font) -> {
			Style out = new Style();
			out.color = color.orElse(null);
			out.bold = bold.orElse(null);
			out.italic = italic.orElse(null);
			out.underlined = underlined.orElse(null);
			out.strikethrough = strikethrough.orElse(null);
			out.obfuscated = obfuscated.orElse(null);
			out.font = font.orElse(null);
			return out;
		}));

		private String color;
		private Boolean bold;
		private Boolean italic;
		private Boolean underlined;
		private Boolean strikethrough;
		private Boolean obfuscated;
		private String font;

		public static Style style() {
			return new Style();
		}

		public Style color(String color) { this.color = color; return this; }
		public Style bold(boolean bold) { this.bold = bold; return this; }
		public Style italic(boolean italic) { this.italic = italic; return this; }
		public Style underlined(boolean underlined) { this.underlined = underlined; return this; }
		public Style strikethrough(boolean strikethrough) { this.strikethrough = strikethrough; return this; }
		public Style obfuscated(boolean obfuscated) { this.obfuscated = obfuscated; return this; }
		public Style font(String font) { this.font = font; return this; }
	}
}
