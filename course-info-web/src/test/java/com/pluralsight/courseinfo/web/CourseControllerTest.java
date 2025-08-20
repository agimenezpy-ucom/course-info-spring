package com.pluralsight.courseinfo.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;
import com.pluralsight.courseinfo.test.PerformanceTests;

/**
 * Implementación JUnit 5
 */
@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {
    static Long memBefore;
    static Long memAfter;

    @Mock
    private CourseRepository repository;

    @InjectMocks
    private CourseController controller;

    private Course courseOne;
    private Course courseTwo;

    // Preparar datos de prueba
    @BeforeAll
    public static void setup() {
        memBefore = PerformanceTests.getMemoryUsage();
    }

    // Generar métricas
    @BeforeEach
    public void before() {
        courseOne = new Course("123", "ABC", 40, "//", Optional.empty());
        courseTwo = new Course("456", "DEF", 11, "/A/", Optional.of("ABCe"));
    }

    /* Ejecución de Pruebas */
    @Test
    public void testCreate() {
        Course newCourse = courseOne;

        ArgumentCaptor<Course> value = ArgumentCaptor.forClass(Course.class);
        doNothing().when(repository).saveCourse(value.capture());

        controller.postCourse(newCourse);
        
        assertEquals(newCourse, value.getValue());
    }

    @Test
    public void testAddNote() {
        ArgumentCaptor<String> value = ArgumentCaptor.forClass(String.class);
        doNothing().when(repository).addNotes(anyString(), value.capture());

        controller.addNotes("123", "Hello");
        
        assertEquals("Hello", value.getValue());
    }

    @Test
    public void testUpdate() {
        Course updateCourse = courseTwo;

        ArgumentCaptor<Course> value = ArgumentCaptor.forClass(Course.class);
        doNothing().when(repository).saveCourse(value.capture());

        controller.putCourse("123", updateCourse);
        
        Course result = value.getValue();
        assertNotEquals(updateCourse, result);
        assertEquals("123", result.id());
        assertEquals(updateCourse.name(), result.name());
        assertEquals(updateCourse.length(), result.length());
        assertEquals(updateCourse.url(), result.url());
        assertEquals(updateCourse.notes(), result.notes());
    }

    @Test
    public void testDelete() {
        Course deletedCourse = courseOne;

        when(repository.findById(anyString())).thenReturn(Optional.of(deletedCourse));

        ArgumentCaptor<Course> value = ArgumentCaptor.forClass(Course.class);
        doNothing().when(repository).deleteCourse(value.capture());

        controller.deleteCourse(deletedCourse.id());
        
        assertEquals(deletedCourse, value.getValue());
    }

    @Test
    public void testFind() {
        Course expected = courseOne;

        when(repository.findById(anyString())).thenReturn(Optional.of(expected));

        ResponseEntity<Course> result = controller.getCourse("123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expected, result.getBody());
    }

    @Test
    public void testNotFound() {
        Course expected = null;

        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(expected));

        assertThrows(NotFoundException.class, () -> { 
            ResponseEntity<Course> result = controller.getCourse("123");
            assertNull(result);
        });

    }

    // Generar métricas
    @AfterAll
    public static void clear() {
        memAfter = PerformanceTests.getMemoryUsage();
        System.out.println(String.format("Memory used %s bytes", memAfter - memBefore));
    }
}
