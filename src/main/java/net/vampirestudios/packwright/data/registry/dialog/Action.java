package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.util.DynamicMap;

import java.util.Optional;

/**
 * A dialog click action (vanilla's dialog {@code action} registry, backed by {@code ClickEvent}
 * for the seven static types plus two dialog-only "dynamic" types that substitute input values
 * into a template). Bootstrapped by vanilla's {@code ActionTypes}.
 */
public sealed interface Action permits
		Action.OpenUrl, Action.RunCommand, Action.SuggestCommand, Action.ShowDialog, Action.ChangePage,
		Action.CopyToClipboard, Action.Custom, Action.DynamicRunCommand, Action.DynamicCustom {

	Codec<Action> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<Action, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "open_url" -> OpenUrl.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "run_command" -> RunCommand.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "suggest_command" -> SuggestCommand.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "show_dialog" -> ShowDialog.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "change_page" -> ChangePage.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "copy_to_clipboard" -> CopyToClipboard.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "custom" -> Custom.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "dynamic/run_command" -> DynamicRunCommand.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "dynamic/custom" -> DynamicCustom.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported dialog action type");
			});
		}

		@Override
		public <T> DataResult<T> encode(Action input, DynamicOps<T> ops, T prefix) {
			if (input instanceof OpenUrl a) return OpenUrl.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof RunCommand a) return RunCommand.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof SuggestCommand a) return SuggestCommand.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof ShowDialog a) return ShowDialog.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof ChangePage a) return ChangePage.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof CopyToClipboard a) return CopyToClipboard.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof Custom a) return Custom.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof DynamicRunCommand a) return DynamicRunCommand.CODEC.codec().encode(a, ops, prefix);
			if (input instanceof DynamicCustom a) return DynamicCustom.CODEC.codec().encode(a, ops, prefix);
			return DataResult.error(() -> "Unsupported dialog action: " + input.getClass().getSimpleName());
		}
	};

	// ---------- factories ----------

	static OpenUrl openUrl(String url) { return new OpenUrl(url); }
	static RunCommand runCommand(String command) { return new RunCommand(command); }
	static SuggestCommand suggestCommand(String command) { return new SuggestCommand(command); }
	static ShowDialog showDialog(Dialog.Reference dialog) { return new ShowDialog(dialog); }
	static ShowDialog showDialog(Identifier dialogId) { return new ShowDialog(Dialog.Reference.id(dialogId)); }
	static ShowDialog showDialog(String dialogId) { return new ShowDialog(Dialog.Reference.id(dialogId)); }
	static ShowDialog showDialog(Dialog inline) { return new ShowDialog(Dialog.Reference.inline(inline)); }
	static ChangePage changePage(int page) { return new ChangePage(page); }
	static CopyToClipboard copyToClipboard(String value) { return new CopyToClipboard(value); }
	static Custom custom(Identifier id) { return new Custom(id, Optional.empty()); }
	static Custom custom(Identifier id, DynamicMap payload) { return new Custom(id, Optional.of(payload)); }
	static Custom custom(String id) { return custom(Identifier.tryParse(id)); }
	static Custom custom(String id, DynamicMap payload) { return custom(Identifier.tryParse(id), payload); }
	static DynamicRunCommand dynamicRunCommand(String template) { return new DynamicRunCommand(template); }
	static DynamicCustom dynamicCustom(Identifier id) { return new DynamicCustom(id, Optional.empty()); }
	static DynamicCustom dynamicCustom(Identifier id, DynamicMap additions) { return new DynamicCustom(id, Optional.of(additions)); }
	static DynamicCustom dynamicCustom(String id) { return dynamicCustom(Identifier.tryParse(id)); }
	static DynamicCustom dynamicCustom(String id, DynamicMap additions) { return dynamicCustom(Identifier.tryParse(id), additions); }

	// ---------- variants ----------

	/** opens {@code url} in the player's browser */
	record OpenUrl(String url) implements Action {
		public static final MapCodec<OpenUrl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:open_url"),
				Codec.STRING.fieldOf("url").forGetter(OpenUrl::url)
		).apply(i, (type, url) -> new OpenUrl(url)));
	}

	/** runs {@code command} as the player */
	record RunCommand(String command) implements Action {
		public static final MapCodec<RunCommand> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:run_command"),
				Codec.STRING.fieldOf("command").forGetter(RunCommand::command)
		).apply(i, (type, command) -> new RunCommand(command)));
	}

	/** fills {@code command} into the player's chat input without running it */
	record SuggestCommand(String command) implements Action {
		public static final MapCodec<SuggestCommand> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:suggest_command"),
				Codec.STRING.fieldOf("command").forGetter(SuggestCommand::command)
		).apply(i, (type, command) -> new SuggestCommand(command)));
	}

	/** opens another dialog, by id or inline */
	record ShowDialog(Dialog.Reference dialog) implements Action {
		public static final MapCodec<ShowDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:show_dialog"),
				Dialog.Reference.CODEC.fieldOf("dialog").forGetter(ShowDialog::dialog)
		).apply(i, (type, dialog) -> new ShowDialog(dialog)));
	}

	/** switches a multi-page dialog list to {@code page} (1-indexed) */
	record ChangePage(int page) implements Action {
		public static final MapCodec<ChangePage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:change_page"),
				Codec.INT.fieldOf("page").forGetter(ChangePage::page)
		).apply(i, (type, page) -> new ChangePage(page)));
	}

	/** copies {@code value} to the player's clipboard */
	record CopyToClipboard(String value) implements Action {
		public static final MapCodec<CopyToClipboard> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:copy_to_clipboard"),
				Codec.STRING.fieldOf("value").forGetter(CopyToClipboard::value)
		).apply(i, (type, value) -> new CopyToClipboard(value)));
	}

	/** fires a custom click event {@code id}, with an optional NBT {@code payload} */
	record Custom(Identifier id, Optional<DynamicMap> payload) implements Action {
		public static final MapCodec<Custom> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:custom"),
				Identifier.CODEC.fieldOf("id").forGetter(Custom::id),
				DynamicMap.CODEC.optionalFieldOf("payload").forGetter(Custom::payload)
		).apply(i, (type, id, payload) -> new Custom(id, payload)));
	}

	/** runs a command template with input values substituted in, vanilla's {@code minecraft:dynamic/run_command} */
	record DynamicRunCommand(String template) implements Action {
		public static final MapCodec<DynamicRunCommand> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:dynamic/run_command"),
				Codec.STRING.fieldOf("template").forGetter(DynamicRunCommand::template)
		).apply(i, (type, template) -> new DynamicRunCommand(template)));
	}

	/** fires a custom click event {@code id} with input values merged into an optional NBT {@code additions}, vanilla's {@code minecraft:dynamic/custom} */
	record DynamicCustom(Identifier id, Optional<DynamicMap> additions) implements Action {
		public static final MapCodec<DynamicCustom> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:dynamic/custom"),
				Identifier.CODEC.fieldOf("id").forGetter(DynamicCustom::id),
				DynamicMap.CODEC.optionalFieldOf("additions").forGetter(DynamicCustom::additions)
		).apply(i, (type, id, additions) -> new DynamicCustom(id, additions)));
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
