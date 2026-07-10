package net.vampirestudios.packwright.util;

/**
 * A {@link java.util.function.Function} that is allowed to throw, for async
 * resource generators whose work may fail (IO, image decoding, ...).
 *
 * @param <I> the input, usually the resource's path
 * @param <O> the produced value, usually the resource's bytes
 */
@FunctionalInterface
public interface CallableFunction<I, O> {
	O get(I input) throws Exception;
}
