package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/** shows the server's registered links as a button grid, vanilla's {@code minecraft:server_links} dialog type */
public record ServerLinksDialog(CommonDialogData common, Optional<Dialog.Button> exitAction, int columns, int buttonWidth) implements Dialog {
	public static final MapCodec<ServerLinksDialog> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:server_links"),
			CommonDialogData.MAP_CODEC.forGetter(ServerLinksDialog::common),
			Dialog.Button.CODEC.optionalFieldOf("exit_action").forGetter(ServerLinksDialog::exitAction),
			Codec.INT.fieldOf("columns").orElse(2).forGetter(ServerLinksDialog::columns),
			Codec.INT.fieldOf("button_width").orElse(150).forGetter(ServerLinksDialog::buttonWidth)
	).apply(i, (type, common, exitAction, columns, buttonWidth) -> new ServerLinksDialog(common, exitAction, columns, buttonWidth)));

	public ServerLinksDialog title(String title) { return new ServerLinksDialog(common.withTitle(title), exitAction, columns, buttonWidth); }
	public ServerLinksDialog externalTitle(String externalTitle) { return new ServerLinksDialog(common.withExternalTitle(externalTitle), exitAction, columns, buttonWidth); }
	public ServerLinksDialog canCloseWithEscape(boolean canCloseWithEscape) { return new ServerLinksDialog(common.withCanCloseWithEscape(canCloseWithEscape), exitAction, columns, buttonWidth); }
	public ServerLinksDialog pause(boolean pause) { return new ServerLinksDialog(common.withPause(pause), exitAction, columns, buttonWidth); }
	public ServerLinksDialog afterAction(Dialog.AfterAction afterAction) { return new ServerLinksDialog(common.withAfterAction(afterAction), exitAction, columns, buttonWidth); }
	public ServerLinksDialog body(Body body) { return new ServerLinksDialog(common.addBody(body), exitAction, columns, buttonWidth); }
	public ServerLinksDialog plainMessage(String message) { return body(Body.plainMessage(message)); }
	public ServerLinksDialog input(Input input) { return new ServerLinksDialog(common.addInput(input), exitAction, columns, buttonWidth); }
	public ServerLinksDialog exitAction(Dialog.Button exitAction) { return new ServerLinksDialog(common, Optional.of(exitAction), columns, buttonWidth); }
	public ServerLinksDialog columns(int columns) { return new ServerLinksDialog(common, exitAction, columns, buttonWidth); }
	public ServerLinksDialog buttonWidth(int buttonWidth) { return new ServerLinksDialog(common, exitAction, columns, buttonWidth); }
}
