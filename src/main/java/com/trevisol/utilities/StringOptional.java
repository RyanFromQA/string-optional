package com.trevisol.utilities;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringOptional {
    private final String innerValue;
    private boolean present;
    public static final StringOptional EMPTY = new StringOptional();

    private StringOptional() {
        innerValue = null;
        present = false;
    }

    private StringOptional(String input) {
        innerValue = input;
        present = checkPresence();
    }

    public static StringOptional of(String input) {
        return new StringOptional(input);
    }

    public String get(){
        return innerValue;
    }

    public String orElse(String defaultValue) {
        return present ? innerValue : defaultValue;
    }

    public String orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!present) {
            throw exceptionSupplier.get();
        }
        return innerValue;
    }



    public boolean isPresent() {
        return present;
    }

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
