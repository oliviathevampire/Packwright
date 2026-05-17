package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dialog {
	public static final Codec<Dialog> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
			Codec.STRING.fieldOf("title").forGetter(x -> x.title),
			Codec.STRING.optionalFieldOf("external_title").forGetter(x -> x.externalTitle),
			Codec.BOOL.optionalFieldOf("can_close_with_escape", true).forGetter(x -> x.canCloseWithEscape),
			Codec.BOOL.optionalFieldOf("pause", false).forGetter(x -> x.pause),
			AfterAction.CODEC.optionalFieldOf("after_action", AfterAction.CLOSE).forGetter(x -> x.afterAction),
			Body.CODEC.listOf().optionalFieldOf("body", List.of()).forGetter(x -> x.body),
			Input.CODEC.listOf().optionalFieldOf("inputs", List.of()).forGetter(x -> x.inputs),
			Button.CODEC.optionalFieldOf("action").forGetter(x -> x.action),
			Button.CODEC.optionalFieldOf("yes").forGetter(x -> x.yes),
			Button.CODEC.optionalFieldOf("no").forGetter(x -> x.no),
			Button.CODEC.listOf().optionalFieldOf("actions", List.of()).forGetter(x -> x.actions),
			Codec.INT.optionalFieldOf("columns", 1).forGetter(x -> x.columns),
			Codec.INT.optionalFieldOf("button_width", 150).forGetter(x -> x.buttonWidth)
	).apply(i, (type, title, externalTitle, canClose, pause, afterAction, body, inputs, action, yes, no, actions, columns, buttonWidth) -> new Dialog()
			.type(type).title(title).externalTitle(externalTitle).canCloseWithEscape(canClose).pause(pause).afterAction(afterAction)
			.body(body).inputs(inputs).action(action).yes(yes).no(no).actions(actions).columns(columns).buttonWidth(buttonWidth)));

	private Identifier type = Identifier.withDefaultNamespace("notice");
	private String title = "";
	private Optional<String> externalTitle = Optional.empty();
	private boolean canCloseWithEscape = true;
	private boolean pause;
	private AfterAction afterAction = AfterAction.CLOSE;
	private List<Body> body = new ArrayList<>();
	private List<Input> inputs = new ArrayList<>();
	private Optional<Button> action = Optional.empty();
	private Optional<Button> yes = Optional.empty();
	private Optional<Button> no = Optional.empty();
	private List<Button> actions = new ArrayList<>();
	private int columns = 1;
	private int buttonWidth = 150;

	public static Dialog dialog() { return new Dialog(); }
	public static Dialog notice(String title, String actionLabel) { return dialog().type("minecraft:notice").title(title).action(Button.button(actionLabel)); }
	public static Dialog confirmation(String title, String yes, String no) { return dialog().type("minecraft:confirmation").title(title).yes(Button.button(yes)).no(Button.button(no)); }
	public Dialog type(String type) { return type(Identifier.tryParse(type)); }
	public Dialog type(Identifier type) { this.type = type; return this; }
	public Dialog title(String title) { this.title = title; return this; }
	public Dialog externalTitle(Optional<String> externalTitle) { this.externalTitle = externalTitle; return this; }
	public Dialog externalTitle(String externalTitle) { this.externalTitle = Optional.of(externalTitle); return this; }
	public Dialog canCloseWithEscape(boolean canCloseWithEscape) { this.canCloseWithEscape = canCloseWithEscape; return this; }
	public Dialog pause(boolean pause) { this.pause = pause; return this; }
	public Dialog afterAction(AfterAction afterAction) { this.afterAction = afterAction; return this; }
	public Dialog body(List<Body> body) { this.body = new ArrayList<>(body); return this; }
	public Dialog plainMessage(String message) { this.body.add(Body.plainMessage(message)); return this; }
	public Dialog inputs(List<Input> inputs) { this.inputs = new ArrayList<>(inputs); return this; }
	public Dialog input(Input input) { this.inputs.add(input); return this; }
	public Dialog action(Optional<Button> action) { this.action = action; return this; }
	public Dialog action(Button action) { this.action = Optional.of(action); return this; }
	public Dialog yes(Optional<Button> yes) { this.yes = yes; return this; }
	public Dialog yes(Button yes) { this.yes = Optional.of(yes); return this; }
	public Dialog no(Optional<Button> no) { this.no = no; return this; }
	public Dialog no(Button no) { this.no = Optional.of(no); return this; }
	public Dialog actions(List<Button> actions) { this.actions = new ArrayList<>(actions); return this; }
	public Dialog addAction(Button action) { this.actions.add(action); return this; }
	public Dialog columns(int columns) { this.columns = columns; return this; }
	public Dialog buttonWidth(int buttonWidth) { this.buttonWidth = buttonWidth; return this; }

	public enum AfterAction implements StringRepresentable {
		CLOSE("close"),
		NONE("none"),
		WAIT_FOR_RESPONSE("wait_for_response");
		public static final Codec<AfterAction> CODEC = StringRepresentable.fromEnum(AfterAction::values);
		private final String name;
		AfterAction(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}

	public static class Body {
		public static final Codec<Body> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
				Codec.STRING.fieldOf("contents").forGetter(x -> x.contents),
				Codec.INT.optionalFieldOf("width", 200).forGetter(x -> x.width)
		).apply(i, (type, contents, width) -> new Body().type(type).contents(contents).width(width)));

		private Identifier type = Identifier.withDefaultNamespace("plain_message");
		private String contents = "";
		private int width = 200;

		public static Body plainMessage(String contents) { return new Body().type("minecraft:plain_message").contents(contents); }
		public Body type(String type) { return type(Identifier.tryParse(type)); }
		public Body type(Identifier type) { this.type = type; return this; }
		public Body contents(String contents) { this.contents = contents; return this; }
		public Body width(int width) { this.width = width; return this; }
	}

	public static class Button {
		public static final Codec<Button> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("label").forGetter(x -> x.label),
				Codec.STRING.optionalFieldOf("tooltip").forGetter(x -> x.tooltip),
				Codec.INT.optionalFieldOf("width", 150).forGetter(x -> x.width),
				Action.CODEC.optionalFieldOf("action").forGetter(x -> x.action)
		).apply(i, (label, tooltip, width, action) -> new Button().label(label).tooltip(tooltip).width(width).action(action)));

		private String label = "";
		private Optional<String> tooltip = Optional.empty();
		private int width = 150;
		private Optional<Action> action = Optional.empty();

		public static Button button(String label) { return new Button().label(label); }
		public Button label(String label) { this.label = label; return this; }
		public Button tooltip(Optional<String> tooltip) { this.tooltip = tooltip; return this; }
		public Button tooltip(String tooltip) { this.tooltip = Optional.of(tooltip); return this; }
		public Button width(int width) { this.width = width; return this; }
		public Button action(Optional<Action> action) { this.action = action; return this; }
		public Button action(Action action) { this.action = Optional.of(action); return this; }
	}

	public record Action(Identifier type, Optional<String> value) {
		public static final Codec<Action> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("type").forGetter(Action::type),
				Codec.STRING.optionalFieldOf("value").forGetter(Action::value)
		).apply(i, Action::new));
		public static Action custom(String value) { return new Action(Identifier.withDefaultNamespace("custom"), Optional.of(value)); }
	}

	public record Input(Identifier type, String key, String label) {
		public static final Codec<Input> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("type").forGetter(Input::type),
				Codec.STRING.fieldOf("key").forGetter(Input::key),
				Codec.STRING.fieldOf("label").forGetter(Input::label)
		).apply(i, Input::new));
		public static Input text(String key, String label) { return new Input(Identifier.withDefaultNamespace("text"), key, label); }
	}
}
