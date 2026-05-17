package net.vampirestudios.arrp.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProcessorList {
	public static final Codec<ProcessorList> CODEC = RecordCodecBuilder.create(i -> i.group(
			Processor.CODEC.listOf().fieldOf("processors").forGetter(x -> x.processors)
	).apply(i, processors -> new ProcessorList().processors(processors)));

	private List<Processor> processors = new ArrayList<>();

	public static ProcessorList processorList() { return new ProcessorList(); }
	public ProcessorList processors(List<Processor> processors) { this.processors = new ArrayList<>(processors); return this; }
	public ProcessorList processor(Processor processor) { this.processors.add(processor); return this; }
	public ProcessorList blockIgnore(List<String> blocks) { return processor(Processor.blockIgnore(blocks)); }
	public ProcessorList rule(Rule rule) { return processor(Processor.ruleProcessor(rule)); }
	public List<Processor> getProcessors() { return processors; }

	public static class Processor {
		public static final Codec<Processor> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("processor_type").forGetter(x -> x.processorType),
				Codec.STRING.listOf().optionalFieldOf("blocks", List.of()).forGetter(x -> x.blocks),
				Rule.CODEC.listOf().optionalFieldOf("rules", List.of()).forGetter(x -> x.rules),
				Codec.INT.optionalFieldOf("limit").forGetter(x -> x.limit)
		).apply(i, (type, blocks, rules, limit) -> new Processor().processorType(type).blocks(blocks).rules(rules).limit(limit)));

		private Identifier processorType;
		private List<String> blocks = new ArrayList<>();
		private List<Rule> rules = new ArrayList<>();
		private Optional<Integer> limit = Optional.empty();

		public static Processor blockIgnore(List<String> blocks) {
			return new Processor().processorType("minecraft:block_ignore").blocks(blocks);
		}

		public static Processor ruleProcessor(Rule rule) {
			return new Processor().processorType("minecraft:rule").rule(rule);
		}

		public Processor processorType(String processorType) { return processorType(Identifier.tryParse(processorType)); }
		public Processor processorType(Identifier processorType) { this.processorType = processorType; return this; }
		public Processor blocks(List<String> blocks) { this.blocks = new ArrayList<>(blocks); return this; }
		public Processor block(String block) { this.blocks.add(block); return this; }
		public Processor rules(List<Rule> rules) { this.rules = new ArrayList<>(rules); return this; }
		public Processor rule(Rule rule) { this.rules.add(rule); return this; }
		public Processor limit(Optional<Integer> limit) { this.limit = limit; return this; }
		public Processor limit(int limit) { this.limit = Optional.of(limit); return this; }
	}

	public static class Rule {
		public static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(
				Test.CODEC.fieldOf("input_predicate").forGetter(x -> x.inputPredicate),
				Test.CODEC.optionalFieldOf("location_predicate", Test.alwaysTrue()).forGetter(x -> x.locationPredicate),
				State.CODEC.fieldOf("output_state").forGetter(x -> x.outputState)
		).apply(i, (input, location, output) -> new Rule().inputPredicate(input).locationPredicate(location).outputState(output)));

		private Test inputPredicate = Test.alwaysTrue();
		private Test locationPredicate = Test.alwaysTrue();
		private State outputState;

		public static Rule replace(String inputBlock, String outputBlock) {
			return new Rule().inputPredicate(Test.matchBlock(inputBlock)).outputState(State.state(outputBlock));
		}

		public Rule inputPredicate(Test inputPredicate) { this.inputPredicate = inputPredicate; return this; }
		public Rule locationPredicate(Test locationPredicate) { this.locationPredicate = locationPredicate; return this; }
		public Rule outputState(State outputState) { this.outputState = outputState; return this; }
	}

	public record Test(Identifier predicateType, Optional<String> block, Optional<Float> probability) {
		public static final Codec<Test> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("predicate_type").forGetter(Test::predicateType),
				Codec.STRING.optionalFieldOf("block").forGetter(Test::block),
				Codec.FLOAT.optionalFieldOf("probability").forGetter(Test::probability)
		).apply(i, Test::new));
		public static Test alwaysTrue() { return new Test(Identifier.withDefaultNamespace("always_true"), Optional.empty(), Optional.empty()); }
		public static Test matchBlock(String block) { return new Test(Identifier.withDefaultNamespace("block_match"), Optional.of(block), Optional.empty()); }
		public static Test random(float probability) { return new Test(Identifier.withDefaultNamespace("random_block_match"), Optional.empty(), Optional.of(probability)); }
	}

	public record State(String Name) {
		public static final Codec<State> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("Name").forGetter(State::Name)
		).apply(i, State::new));
		public static State state(String blockId) { return new State(blockId); }
	}
}
