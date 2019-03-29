package com.trevisol.utilities;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

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
    public void testIsPresent() {
        assertTrue(StringOptional.of(" a real string ").isPresent());
        assertTrue(StringOptional.of("a").isPresent());
        assertTrue(StringOptional.of("                  a                  ").isPresent());
        assertFalse(StringOptional.of("      ").isPresent());
        assertFalse(StringOptional.of("").isPresent());
        assertFalse(StringOptional.of(null).isPresent());
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
}
