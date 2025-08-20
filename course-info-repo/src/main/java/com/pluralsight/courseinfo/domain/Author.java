package com.pluralsight.courseinfo.domain;

public record Author(Long id, String firstName, String lastName, String handle) {
    public Author {
        filled(firstName);
        filled(lastName);
        filled(handle);
    }

    private static void filled(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("No value present!!");
        }
    }
}
