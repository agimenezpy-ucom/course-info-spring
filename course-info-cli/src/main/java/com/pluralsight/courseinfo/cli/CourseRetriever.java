package com.pluralsight.courseinfo.cli;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pluralsight.courseinfo.cli.service.AuthorStorageService;
import com.pluralsight.courseinfo.cli.service.CourseRetrievalService;
import com.pluralsight.courseinfo.cli.service.CourseStorageService;
import com.pluralsight.courseinfo.cli.service.PluralsightAuthor;
import com.pluralsight.courseinfo.cli.service.PluralsightCourse;
import com.pluralsight.courseinfo.domain.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pluralsight.courseinfo")
public class CourseRetriever {
    public static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);

    @Autowired
    private CourseRetrievalService courseRetrievalService;
    @Autowired
    private CourseStorageService courseStorageService;
    @Autowired
    private AuthorStorageService authorStorageService;

    public static void main(String[] args) {
        LOG.info("CourseRetriever starting");
        SpringApplication.run(CourseRetriever.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (args.length == 0) {
                LOG.warn("Please provide an author name as first argument.");
                return;
            }

            try {
                retrieveCourses(args[0]);
            } catch (Exception e) {
                LOG.error("Unexpected error", e);
            }
        };
    }

    private void retrieveCourses(String authorId) {
        LOG.info("Retrieving courses for author {}", authorId);

        var coursesToStore = courseRetrievalService.getCoursesFor(authorId)
                .stream()
                .filter(Predicate.not(PluralsightCourse::isRetired))
                .toList();
        LOG.info("Retrieved the following {} courses {}", coursesToStore.size(), coursesToStore);
        courseStorageService.storePluralsightCourses(coursesToStore);
        LOG.info("Courses successfully stored");

        if (coursesToStore.size() > 0) {
            PluralsightAuthor author = coursesToStore.get(0).authors().get(0);
            LOG.info("Using the following {} author from {} courses", author, coursesToStore.size());
            
            Optional<Author> checkAuthor = authorStorageService.getAuthorRepository().findByHandle(author.handle());
            if (!checkAuthor.isPresent()) {
                authorStorageService.storePluralsightAuthors(List.of(author));
                LOG.info("new author successfully stored");
            }
        }
    }
}
