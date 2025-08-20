package com.pluralsight.courseinfo.cli.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;

@Component
public class CourseStorageService {
    public static final String PS_BASE_URL = "https://app.pluralsight.com";

    @Autowired
    private CourseRepository courseRepository;

    public void storePluralsightCourses(List<PluralsightCourse> psCourses) {
        for (PluralsightCourse psCourse: psCourses) {
            Course course = new Course(psCourse.id(), 
                psCourse.title(),
                psCourse.durationInMinutes(), 
                PS_BASE_URL + psCourse.contentUrl(), Optional.empty());
            courseRepository.saveCourse(course);
        }
    }

}
