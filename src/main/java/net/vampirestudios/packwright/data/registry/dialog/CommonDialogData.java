package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Fields shared by every dialog type (vanilla's {@code CommonDialogData}). Embedded flat into
 * each {@link Dialog} variant's own codec via {@link #MAP_CODEC}, so title/body/inputs/etc. sit
 * alongside the type-specific fields in the encoded JSON rather than under a nested key.
 */
public record CommonDialogData(
		String title,
		Optional<String> externalTitle,
		boolean canCloseWithEscape,
		boolean pause,
		Dialog.AfterAction afterAction,
		List<Body> body,
		List<Input> inputs
) {
	public static final MapCodec<CommonDialogData> MAP_CODEC = RecordCodecBuilder.<CommonDialogData>mapCodec(i -> i.group(
			Codec.STRING.fieldOf("title").forGetter(CommonDialogData::title),
			Codec.STRING.optionalFieldOf("external_title").forGetter(CommonDialogData::externalTitle),
			Codec.BOOL.fieldOf("can_close_with_escape").orElse(true).forGetter(CommonDialogData::canCloseWithEscape),
			// vanilla defaults `pause` to true, not false
			Codec.BOOL.fieldOf("pause").orElse(true).forGetter(CommonDialogData::pause),
			Dialog.AfterAction.CODEC.fieldOf("after_action").orElse(Dialog.AfterAction.CLOSE).forGetter(CommonDialogData::afterAction),
			Body.CODEC.listOf().fieldOf("body").orElse(List.of()).forGetter(CommonDialogData::body),
			Input.CODEC.listOf().fieldOf("inputs").orElse(List.of()).forGetter(CommonDialogData::inputs)
	).apply(i, CommonDialogData::new))
			.validate(data -> data.pause() && !data.afterAction().willUnpause()
					? com.mojang.serialization.DataResult.error(() -> "Dialogs that pause the game must use after_action values that unpause it after user action!")
					: com.mojang.serialization.DataResult.success(data));

	public static CommonDialogData of(String title) {
		return new CommonDialogData(title, Optional.empty(), true, true, Dialog.AfterAction.CLOSE, List.of(), List.of());
	}

	public CommonDialogData withTitle(String title) { return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs); }
	public CommonDialogData withExternalTitle(String externalTitle) { return new CommonDialogData(title, Optional.of(externalTitle), canCloseWithEscape, pause, afterAction, body, inputs); }
	public CommonDialogData withCanCloseWithEscape(boolean canCloseWithEscape) { return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs); }
	public CommonDialogData withPause(boolean pause) { return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs); }
	public CommonDialogData withAfterAction(Dialog.AfterAction afterAction) { return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs); }

	public CommonDialogData withBody(List<Body> body) { return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, List.copyOf(body), inputs); }
	public CommonDialogData addBody(Body entry) {
		List<Body> updated = new ArrayList<>(body);
		updated.add(entry);
		return withBody(updated);
	}

	public CommonDialogData withInputs(List<Input> inputs) { return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, body, List.copyOf(inputs)); }
	public CommonDialogData addInput(Input input) {
		List<Input> updated = new ArrayList<>(inputs);
		updated.add(input);
		return withInputs(updated);
	}
}
