package com.trevisol.utilities;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class mimicking the API of {@link Optional} with the added benefit of checking a String's contents.
 * <p>
 * Uses a similar algorithm to Apache's StringUtils.isBlank to determine whether a String is "present" or not.
 * <p>
 * Examples:
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
     * <strong>Use with care! This method offers no checking so it may result in a {@link NullPointerException}</strong>
     *
     * @return the value regardless of whether it is null or empty.
     */
    public String get() {
        return innerValue;
    }


    /**
     * Get the object if present or otherwise the default value supplied in the argument
     *
     * @param defaultValue the value to return if the inner value is null or empty
     * @return the value if present, or the defaultValue
     */
    public String orElse(String defaultValue) {
        return present ? innerValue : defaultValue;
    }

    /**
     * Get the object if present, or otherwise throw an exception
     *
     * @param exceptionSupplier a {@link Supplier} of a child class of RuntimeException
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
     * Check if the inner value is present, and if so, do something with it in the supplied consumer.
     *
     * @param doThis a {@link Consumer} to perform if the inner value is present
     */
    public void ifPresent(Consumer<String> doThis) {
        if (present) {
            doThis.accept(innerValue);
        }
    }

    /**
     * Checks in a manner similar to Apache's StringUtils#isEmpty
     *
     * @return
     */
    private boolean checkPresence() {
        return innerValue != null &&
            innerValue.length() > 0 &&
            !innerValue.chars().allMatch(Character::isWhitespace);
    }

    /**
     * Provides similar functionality to the Optional.map method; transforms the inner value if present.
     *
     * @param mappingFunction a function which accepts a string, and produces T
     * @param <T> the type returned by the mapppingFunction
     * @return an optional of type T, or Optional.empty() if the inner value is not present
     */

    public <T> Optional<T> map(Function<String, ? extends T> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return isPresent() ? Optional.ofNullable(mappingFunction.apply(innerValue)) : Optional.empty();
    }

}
