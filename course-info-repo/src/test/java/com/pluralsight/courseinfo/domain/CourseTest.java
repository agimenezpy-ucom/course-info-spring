package com.pluralsight.courseinfo.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class CourseTest {
    @Test
    void testIdNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Course(null, "Jhon", 0, "abc", Optional.empty());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Course("", "Jhon", 0, "abc", Optional.empty());
        });
    }

    @Test
    void testIdNameOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Course("123", null, 0, "abc", Optional.empty());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Course("123", "", 0, "abc", Optional.empty());
        });
    }

    @Test
    void testUrlNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Course("123", "Jhon", 0, null, Optional.empty());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Course("123", "Jhon", 0, "", Optional.empty());
        });
    }
}
