package com.pluralsight.courseinfo.web;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;

@RestController
@RequestMapping("courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    public void setRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping(produces = "application/json")
    public List<Course> getCourses() {
        return courseRepository
                .getAllCourses()
                .stream()
                .sorted(Comparator.comparing(Course::id))
                .toList();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Course> getCourse(@PathVariable("id") String id) {
        var result = courseRepository.findById(id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        else { 
            throw new NotFoundException();
        }
    }

    @PostMapping(path = "/{id}/notes", consumes = "text/plain")
    public void addNotes(@PathVariable("id") String id, String notes) {
        courseRepository.addNotes(id, notes);
    }

    @PostMapping(path = "/", consumes = "application/json")
    public void postCourse(Course course) {
        courseRepository.saveCourse(course);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public void putCourse(@PathVariable("id") String id, Course course) {
        courseRepository.saveCourse(new Course(id, course.name(), course.length(), course.url(), course.notes()));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCourse(@PathVariable("id") String id) {
        var result = courseRepository.findById(id);
        if (result.isPresent()) {
            courseRepository.deleteCourse(result.get());
        }
        else { 
            throw new NotFoundException();
        }
    }
}
