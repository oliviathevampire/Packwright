package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * a grid of action buttons, vanilla's {@code minecraft:multi_action} dialog type.
 * {@code actions} is required and must be non-empty.
 */
public record MultiActionDialog(CommonDialogData common, List<Dialog.Button> actions, Optional<Dialog.Button> exitAction, int columns) implements Dialog {
	public MultiActionDialog {
		if (actions == null || actions.isEmpty()) throw new IllegalArgumentException("multi_action dialog actions cannot be empty");
		actions = List.copyOf(actions);
	}

	public static final MapCodec<MultiActionDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:multi_action"),
			CommonDialogData.MAP_CODEC.forGetter(MultiActionDialog::common),
			Dialog.Button.CODEC.listOf().fieldOf("actions").forGetter(MultiActionDialog::actions),
			Dialog.Button.CODEC.optionalFieldOf("exit_action").forGetter(MultiActionDialog::exitAction),
			Codec.INT.fieldOf("columns").orElse(2).forGetter(MultiActionDialog::columns)
	).apply(i, (type, common, actions, exitAction, columns) -> new MultiActionDialog(common, actions, exitAction, columns)));

	public MultiActionDialog title(String title) { return new MultiActionDialog(common.withTitle(title), actions, exitAction, columns); }
	public MultiActionDialog externalTitle(String externalTitle) { return new MultiActionDialog(common.withExternalTitle(externalTitle), actions, exitAction, columns); }
	public MultiActionDialog canCloseWithEscape(boolean canCloseWithEscape) { return new MultiActionDialog(common.withCanCloseWithEscape(canCloseWithEscape), actions, exitAction, columns); }
	public MultiActionDialog pause(boolean pause) { return new MultiActionDialog(common.withPause(pause), actions, exitAction, columns); }
	public MultiActionDialog afterAction(Dialog.AfterAction afterAction) { return new MultiActionDialog(common.withAfterAction(afterAction), actions, exitAction, columns); }
	public MultiActionDialog body(Body body) { return new MultiActionDialog(common.addBody(body), actions, exitAction, columns); }
	public MultiActionDialog plainMessage(String message) { return body(Body.plainMessage(message)); }
	public MultiActionDialog input(Input input) { return new MultiActionDialog(common.addInput(input), actions, exitAction, columns); }
	public MultiActionDialog actions(List<Dialog.Button> actions) { return new MultiActionDialog(common, actions, exitAction, columns); }
	public MultiActionDialog addAction(Dialog.Button action) {
		List<Dialog.Button> updated = new ArrayList<>(actions);
		updated.add(action);
		return actions(updated);
	}
	public MultiActionDialog exitAction(Dialog.Button exitAction) { return new MultiActionDialog(common, actions, Optional.of(exitAction), columns); }
	public MultiActionDialog columns(int columns) { return new MultiActionDialog(common, actions, exitAction, columns); }
}
