package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** shows a list of other dialogs as a button grid, vanilla's {@code minecraft:dialog_list} dialog type */
public record DialogListDialog(CommonDialogData common, List<Dialog.Reference> dialogs, Optional<Dialog.Button> exitAction, int columns, int buttonWidth) implements Dialog {
	public DialogListDialog { dialogs = List.copyOf(dialogs); }

	public static final MapCodec<DialogListDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:dialog_list"),
			CommonDialogData.MAP_CODEC.forGetter(DialogListDialog::common),
			Dialog.Reference.CODEC.listOf().fieldOf("dialogs").forGetter(DialogListDialog::dialogs),
			Dialog.Button.CODEC.optionalFieldOf("exit_action").forGetter(DialogListDialog::exitAction),
			Codec.INT.fieldOf("columns").orElse(2).forGetter(DialogListDialog::columns),
			Codec.INT.fieldOf("button_width").orElse(150).forGetter(DialogListDialog::buttonWidth)
	).apply(i, (type, common, dialogs, exitAction, columns, buttonWidth) -> new DialogListDialog(common, dialogs, exitAction, columns, buttonWidth)));

	public DialogListDialog title(String title) { return new DialogListDialog(common.withTitle(title), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog externalTitle(String externalTitle) { return new DialogListDialog(common.withExternalTitle(externalTitle), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog canCloseWithEscape(boolean canCloseWithEscape) { return new DialogListDialog(common.withCanCloseWithEscape(canCloseWithEscape), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog pause(boolean pause) { return new DialogListDialog(common.withPause(pause), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog afterAction(Dialog.AfterAction afterAction) { return new DialogListDialog(common.withAfterAction(afterAction), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog body(Body body) { return new DialogListDialog(common.addBody(body), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog plainMessage(String message) { return body(Body.plainMessage(message)); }
	public DialogListDialog input(Input input) { return new DialogListDialog(common.addInput(input), dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog dialogs(List<Dialog.Reference> dialogs) { return new DialogListDialog(common, dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog addDialog(Dialog.Reference dialog) {
		List<Dialog.Reference> updated = new ArrayList<>(dialogs);
		updated.add(dialog);
		return dialogs(updated);
	}
	public DialogListDialog exitAction(Dialog.Button exitAction) { return new DialogListDialog(common, dialogs, Optional.of(exitAction), columns, buttonWidth); }
	public DialogListDialog columns(int columns) { return new DialogListDialog(common, dialogs, exitAction, columns, buttonWidth); }
	public DialogListDialog buttonWidth(int buttonWidth) { return new DialogListDialog(common, dialogs, exitAction, columns, buttonWidth); }
}
