package com.pluralsight.courseinfo.web;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pluralsight.courseinfo.domain.Author;
import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.AuthorRepository;
import com.pluralsight.courseinfo.repository.CourseRepository;

@RestController
@RequestMapping("authors")
public class AuthorController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping(produces = "application/json")
    public List<Author> getCourses() {
        return authorRepository
                .getAllAuthors()
                .stream()
                .sorted(Comparator.comparing(Author::id))
                .toList();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Author> getCourse(@PathVariable("id") String id) {
        var result = authorRepository.findByHandle(id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        else { 
            throw new NotFoundException();
        }
    }

}
