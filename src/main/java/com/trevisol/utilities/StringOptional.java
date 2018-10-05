package com.trevisol.utilities;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A utility class mimicking the API of {@link Optional <String>} with the added benefit of checking a String's contents.
 * <p>
 * Uses a similar algorithm to Apache's StringUtils.isBlank to determine whether a String is "present" or not.
 * <p>
 * Examples:
 * <pre>
 * StringOptional.of(null) >> NOT present
 * StringOptional.of("") >> NOT present
 * StringOptional.of("   ") >> NOT present
 * StringOptional.of("content") >> present
 * </pre>
 */
public class StringOptional {
    private final String innerValue;
    private boolean present;
    /**
     * A guaranteed not-present StringOptional
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
     * @param input the string
     * @return a StringOptional instance containing the input
     */
    public static StringOptional of(String input) {
        return new StringOptional(input);
    }

    /**
     * <strong>Use with care! This method offers no checking so it may result in a Null Pointer Exception</strong>
     *
     * @return the value regardless of whether it is present.
     */
    public String get() {
        return innerValue;
    }


    /**
     * Get the object if present or otherwise the default value supplied in the argument
     *
     * @return the value if present, or the defaultValue
     */
    public String orElse(String defaultValue) {
        return present ? innerValue : defaultValue;
    }

    /**
     * Get the object if present, or otherwise throw an exception
     * @param exceptionSupplier a {@link Supplier} of a child class of RuntimeException
     * @return
     */
    public String orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!present) {
            throw exceptionSupplier.get();
        }
        return innerValue;
    }

    /**
     * @return whether the string was evaluated as present
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * Check if the inner value is present, and if so, do something with it in the supplied consumer.
     * @param doThis
     */
    public void ifPresent(Consumer<String> doThis) {
        if (present) {
            doThis.accept(innerValue);
        }
    }

    private boolean checkPresence() {
        return innerValue != null &&
                innerValue.length() > 0 &&
                !innerValue.chars().allMatch(Character::isWhitespace);
    }

}
