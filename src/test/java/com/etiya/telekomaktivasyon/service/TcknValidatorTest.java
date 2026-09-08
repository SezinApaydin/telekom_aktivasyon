package com.etiya.telekomaktivasyon.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TcknValidatorTest {

    @Test
    void shouldAcceptValidTckn() {
        assertTrue(TcknValidator.isValid("11111111110"));
    }

    @Test
    void shouldRejectShortTckn() {
        assertFalse(TcknValidator.isValid("123456789"));
    }

    @Test
    void shouldRejectTcknWithLetters() {
        assertFalse(TcknValidator.isValid("1234567890a"));
    }

    @Test
    void shouldRejectInvalidChecksum() {
        assertFalse(TcknValidator.isValid("12345678912"));
    }
}