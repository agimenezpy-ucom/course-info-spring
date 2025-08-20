package com.pluralsight.courseinfo.cli.service;

import java.util.List;
import java.time.Duration;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PluralsightCourse(String id, String title, String duration, String contentUrl, boolean isRetired, List<PluralsightAuthor> authors) {
    public long durationInMinutes() {
        return Duration.between(
            LocalTime.MIN, 
            LocalTime.parse(duration)
        ).toMinutes();
    }
}
