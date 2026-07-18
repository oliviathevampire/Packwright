package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/** shows a message with a single dismiss button, vanilla's {@code minecraft:notice} dialog type */
public record NoticeDialog(CommonDialogData common, Optional<Dialog.Button> action) implements Dialog {
	public static final MapCodec<NoticeDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:notice"),
			CommonDialogData.MAP_CODEC.forGetter(NoticeDialog::common),
			// left genuinely optional (rather than defaulted) since vanilla's default action label
			// is a translated "gui.ok" component that a plain String field can't faithfully represent;
			// omitting it entirely lets the client apply its own built-in default
			Dialog.Button.CODEC.optionalFieldOf("action").forGetter(NoticeDialog::action)
	).apply(i, (type, common, action) -> new NoticeDialog(common, action)));

	public NoticeDialog title(String title) { return new NoticeDialog(common.withTitle(title), action); }
	public NoticeDialog externalTitle(String externalTitle) { return new NoticeDialog(common.withExternalTitle(externalTitle), action); }
	public NoticeDialog canCloseWithEscape(boolean canCloseWithEscape) { return new NoticeDialog(common.withCanCloseWithEscape(canCloseWithEscape), action); }
	public NoticeDialog pause(boolean pause) { return new NoticeDialog(common.withPause(pause), action); }
	public NoticeDialog afterAction(Dialog.AfterAction afterAction) { return new NoticeDialog(common.withAfterAction(afterAction), action); }
	public NoticeDialog body(Body body) { return new NoticeDialog(common.addBody(body), action); }
	public NoticeDialog plainMessage(String message) { return body(Body.plainMessage(message)); }
	public NoticeDialog input(Input input) { return new NoticeDialog(common.addInput(input), action); }
	public NoticeDialog action(Dialog.Button action) { return new NoticeDialog(common, Optional.of(action)); }
}
