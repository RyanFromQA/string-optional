package com.trevisol.utilities;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StringOptionalTests {

    @Test
    public void testConstant() {
        assertFalse(StringOptional.EMPTY.isPresent());
    }

    @Test
    public void testIfPresent() {
        StringOptional.of("             ").ifPresent(s -> fail("This should not execute because we passed in blank"));
        StringOptional.of("").ifPresent(s -> fail("This should not execute because we passed in empty"));
        StringOptional.of(null).ifPresent(s -> fail("This should not execute because we passed in null"));
        final String aRealString = " A real string ";
        StringOptional.of(aRealString).ifPresent(s -> assertEquals(s, aRealString, "Should be able to receive string in lambda"));
    }

    @Test
    public void testIfPresentOrElsePresent() {
        List<String> ifpresentcheck = new ArrayList<>();
        List<String> elseCheck = new ArrayList<>();
        StringOptional.EMPTY.ifPresentOrElse(v -> {
            ifpresentcheck.add("this shouldn't show up");
        }, () -> {
            elseCheck.add("this should be added");
        });
        assertEquals(ifpresentcheck.size(), 0);
        assertEquals(elseCheck.size(), 1);
    }

    @Test
    public void testIfPresentOrElseNotPresent() {
        List<String> ifpresentcheck = new ArrayList<>();
        List<String> elseCheck = new ArrayList<>();
        StringOptional.of("totes not empty").ifPresentOrElse(v -> {
            ifpresentcheck.add("this shouldn't show up");
        }, () -> {
            elseCheck.add("this should be added");
        });
        assertEquals(ifpresentcheck.size(), 1);
        assertEquals(elseCheck.size(), 0);
    }

    @Test
    public void testIsPresent() {
        assertTrue(StringOptional.of(" a real string ").isPresent());
        assertTrue(StringOptional.of("a").isPresent());
        assertTrue(StringOptional.of("                  a                  ").isPresent());
        assertFalse(StringOptional.of("      ").isPresent());
        assertFalse(StringOptional.of("").isPresent());
        assertFalse(StringOptional.of(null).isPresent());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(StringOptional.of(" a real string ").isEmpty());
        assertFalse(StringOptional.of("a").isEmpty());
        assertFalse(StringOptional.of("                  a                  ").isEmpty());

        assertTrue(StringOptional.EMPTY.isEmpty());
        assertTrue(StringOptional.of("      ").isEmpty());
        assertTrue(StringOptional.of("").isEmpty());
        assertTrue(StringOptional.of(null).isEmpty());
    }

    @Test(expectedExceptions = ExpectedException.class)
    public void testOrElseThrowBlank() {
        StringOptional.of("    ").orElseThrow(() -> new ExpectedException("Should throw because we passed null"));
    }

    @Test(expectedExceptions = ExpectedException.class)
    public void testOrElseThrowEmpty() {
        StringOptional.of("").orElseThrow(() -> new ExpectedException("Should throw because we passed null"));
    }

    @Test(expectedExceptions = ExpectedException.class)
    public void testOrElseThrowNull() {
        StringOptional.of(null).orElseThrow(() -> new ExpectedException("Should throw because we passed null"));
    }

    @Test()
    public void testOrElseThrowRealString() {
        assertEquals(StringOptional.of("a real string").orElseThrow(() -> new ExpectedException("Should throw because we passed null")), "a real string");
    }

    @Test
    public void testGet() {
        assertNull(StringOptional.of(null).get());
        assertEquals(StringOptional.of("a string").get(), "a string");
    }

    @Test
    public void testOrElseGet() {
        final String orelseGetValue = "This should be returned";
        String shouldBeGet = StringOptional.EMPTY.orElseGet(() -> {
            return orelseGetValue;
        });
        String shouldNotBeGet = StringOptional.of("Delicious Value").orElseGet(() -> "this will never show");

        assertEquals(shouldBeGet, orelseGetValue);
        assertNotEquals(shouldNotBeGet, "this will never show");
        assertEquals(shouldNotBeGet, "Delicious Value");
    }

    @Test
    public void testOrElse() {
        assertEquals(StringOptional.of(null).orElse("that was null"), "that was null");
        assertEquals(StringOptional.of("").orElse("that was empty"), "that was empty");
        assertEquals(StringOptional.of("      ").orElse("that was blank"), "that was blank");
        assertEquals(StringOptional.of("that was a real string").orElse("never gonna give you up"), "that was a real string");
    }

    @Test
    public void testMapping() {
        assertEquals(StringOptional.of(null).map(Integer::parseInt).orElse(1), (Integer) 1);
        assertEquals(StringOptional.of(null).map(String::toUpperCase).orElse("nope"), "nope");
        assertEquals(StringOptional.of("lowercase").map(String::toUpperCase).get(), "LOWERCASE");
    }

    @Test
    public void testOptionalConversion() {
        final String stringval = "Tacos are quite possibly the best food ever";
        StringOptional stringOpt = StringOptional.of(stringval);
        Optional<String> optString = Optional.of(stringval);

        assertTrue(stringOpt.asOptional().isPresent());
        assertFalse(stringOpt.asOptional().isEmpty());
        assertEquals(stringOpt.asOptional().get(), stringval);
        assertTrue(StringOptional.ofOptional(optString).isPresent());
        assertFalse(StringOptional.ofOptional(optString).isEmpty());
        assertEquals(StringOptional.ofOptional(optString).get(), stringval);
        assertEquals(StringOptional.ofOptional(optString).asOptional(), Optional.of(stringval));

    }

    @Test
    public void testStream() {
        List<String> streamoutput = StringOptional.of("totes string")
                .stream()
                .collect(Collectors.toList());

        assertEquals(streamoutput.size(), 1);
        assertEquals(streamoutput.get(0), "totes string");
    }
}
