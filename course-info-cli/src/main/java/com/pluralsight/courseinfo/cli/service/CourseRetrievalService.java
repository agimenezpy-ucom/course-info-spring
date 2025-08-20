package com.pluralsight.courseinfo.cli.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CourseRetrievalService {
    public static final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";

    public static final HttpClient CLIENT = HttpClient.newBuilder()
                            .followRedirects(HttpClient.Redirect.ALWAYS)
                            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public List<PluralsightCourse> getCoursesFor(String authorId) {
        HttpRequest request = HttpRequest
                        .newBuilder(URI.create(PS_URI.formatted(authorId)))
                        .GET().build();
        
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            return switch(response.statusCode()) {
                case 200 -> toPluralsightCourses(response);
                case 404 -> List.of();
                default -> throw new RuntimeException("Pluralsight API call failed with status code " + response.statusCode());
            };
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not call Pluralsight API", e);
        }
    }

    private List<PluralsightCourse> toPluralsightCourses(HttpResponse<String> response) throws JsonProcessingException,JsonMappingException {
        JavaType returnType = OBJECT_MAPPER.getTypeFactory()
                .constructCollectionType(List.class, PluralsightCourse.class);
                
        return OBJECT_MAPPER.readValue(response.body(), returnType);
    }

}
