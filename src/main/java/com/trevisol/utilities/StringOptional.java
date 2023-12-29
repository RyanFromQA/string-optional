package com.trevisol.utilities;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A utility class mimicking the API of {@link Optional} with the added benefit
 * of checking a String's contents.
 * <p>
 * Uses a similar algorithm to Apache's StringUtils.isBlank to determine whether
 * a String is "present" or not.
 * <p>
 * Examples:
 * 
 * <pre>
 * StringOptional.of(null) &gt; NOT present
 * StringOptional.of("") &gt; NOT present
 * StringOptional.of("   ") &gt; NOT present
 * StringOptional.of("content") &gt; present
 * </pre>
 */
public class StringOptional {
    private final String innerValue;
    private boolean present;
    /**
     * A guaranteed not-present {@link StringOptional}
     */
    public static final StringOptional EMPTY = new StringOptional();

    private StringOptional() {
        innerValue = null;
        present = false;
    }

    private StringOptional(String input) {
        innerValue = input;
        present = checkPresence();
    }

    /**
     * Accepts a nullable input, and returns a StringOptional
     *
     * @param input the {@link String} to check
     * @return a StringOptional instance containing the input
     */
    public static StringOptional of(String input) {
        return new StringOptional(input);
    }

    /**
     * Accepts a Optional of type String, converts it to a StringOptional
     * 
     * @param input a {@link Optional} of type {@link String}
     * @return a StringOptional instance containing the value in the Optional
     */
    public static StringOptional of(Optional<String> input) {
        return input.isPresent() ? new StringOptional(input.get()) : StringOptional.EMPTY;
    }

    /**
     * <strong>Use with care! This method offers no checking so it may result in a
     * {@link NullPointerException}</strong>
     *
     * @return the value regardless of whether it is null or empty.
     */
    public String get() {
        return innerValue;
    }

    /**
     * Get the object if present or otherwise the default value supplied in the
     * argument
     *
     * @param defaultValue the value to return if the inner value is null or empty
     * @return the value if present, or the defaultValue
     */
    public String orElse(String defaultValue) {
        return present ? innerValue : defaultValue;
    }

    /**
     * Checks to see if the {@link #innerValue} is present. If it is, it returns
     * that. Otherwise, call the {@link Supplier}
     * 
     * @param supplier a {@link Supplier} function to call if the StringOptional is
     *                 empty
     * @return the innerValue of the StringOptional or whatever the Supplier returns
     */
    public String orElseGet(Supplier<? extends String> supplier) {
        if (present) {
            return innerValue;
        } else {
            return supplier.get();
        }
    }

    /**
     * Get the object if present, or otherwise throw an exception
     *
     * @param exceptionSupplier a {@link Supplier} of a child class of
     *                          RuntimeException
     * @return the inner value, if not null or empty
     */
    public String orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!present) {
            throw exceptionSupplier.get();
        }
        return innerValue;
    }

    /**
     * @return whether the string was evaluated as not null or empty
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * @return true if empty, false otherwise, like Optional#isEmpty
     */
    public boolean isEmpty() {
        return !present;
    }

    /**
     * Check if the inner value is present, and if so, do something with it in the
     * supplied consumer.
     *
     * @param doThis a {@link Consumer} to perform if the inner value is present
     */
    public void ifPresent(Consumer<String> doThis) {
        if (present) {
            doThis.accept(innerValue);
        }
    }

    /**
     * If the innerValue is present, call the first action to do something. If it is not present, call the empty action
     * 
     * @param action        Consumer to call if the value is present
     * @param emptyAction   Runnable to call if value is empty
     */
    public void ifPresentOrElse(Consumer<String> action, Runnable emptyAction) {
        if(isPresent()) {
            action.accept(innerValue);
        } else {
            emptyAction.run();
        }
    }

    /**
     * Converts the StringOptional into a stream of a single entry
     * 
     * @return a Stream of a single entry if a value is present, otherwise an empty stream
     */
    public Stream<String> stream() {
        if(isPresent()) {
            return Stream.of(innerValue);
        } else {
            return Stream.empty();
        }
    }

    /**
     * Checks in a manner similar to Apache's StringUtils#isEmpty
     *
     * @return true if exists, false otherwise
     */
    private boolean checkPresence() {
        return innerValue != null &&
                innerValue.length() > 0 &&
                !innerValue.chars().allMatch(Character::isWhitespace);
    }

    /**
     * Provides similar functionality to the Optional.map method; transforms the
     * inner value if present.
     *
     * @param mappingFunction a function which accepts a string, and produces T
     * @param <T>             the type returned by the mapppingFunction
     * @return an optional of type T, or Optional.empty() if the inner value is not
     *         present
     */

    public <T> Optional<T> map(Function<String, ? extends T> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return isPresent() ? Optional.ofNullable(mappingFunction.apply(innerValue)) : Optional.empty();
    }

    /**
     * Converts the StringOptional into an Optional
     * 
     * @return an Optional of type String or Optional.EMPTY if not present
     */
    public Optional<String> asOptional() {
        return present ? Optional.of(innerValue) : Optional.empty();

    }
}
