package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** a yes/no confirmation, vanilla's {@code minecraft:confirmation} dialog type. Both buttons are required. */
public record ConfirmationDialog(CommonDialogData common, Dialog.Button yes, Dialog.Button no) implements Dialog {
	public static final MapCodec<ConfirmationDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:confirmation"),
			CommonDialogData.MAP_CODEC.forGetter(ConfirmationDialog::common),
			Dialog.Button.CODEC.fieldOf("yes").forGetter(ConfirmationDialog::yes),
			Dialog.Button.CODEC.fieldOf("no").forGetter(ConfirmationDialog::no)
	).apply(i, (type, common, yes, no) -> new ConfirmationDialog(common, yes, no)));

	public ConfirmationDialog title(String title) { return new ConfirmationDialog(common.withTitle(title), yes, no); }
	public ConfirmationDialog externalTitle(String externalTitle) { return new ConfirmationDialog(common.withExternalTitle(externalTitle), yes, no); }
	public ConfirmationDialog canCloseWithEscape(boolean canCloseWithEscape) { return new ConfirmationDialog(common.withCanCloseWithEscape(canCloseWithEscape), yes, no); }
	public ConfirmationDialog pause(boolean pause) { return new ConfirmationDialog(common.withPause(pause), yes, no); }
	public ConfirmationDialog afterAction(Dialog.AfterAction afterAction) { return new ConfirmationDialog(common.withAfterAction(afterAction), yes, no); }
	public ConfirmationDialog body(Body body) { return new ConfirmationDialog(common.addBody(body), yes, no); }
	public ConfirmationDialog plainMessage(String message) { return body(Body.plainMessage(message)); }
	public ConfirmationDialog input(Input input) { return new ConfirmationDialog(common.addInput(input), yes, no); }
	public ConfirmationDialog yes(Dialog.Button yes) { return new ConfirmationDialog(common, yes, no); }
	public ConfirmationDialog no(Dialog.Button no) { return new ConfirmationDialog(common, yes, no); }
}
