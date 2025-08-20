package com.pluralsight.courseinfo.cli.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pluralsight.courseinfo.domain.Author;
import com.pluralsight.courseinfo.repository.AuthorRepository;

@Component
public class AuthorStorageService {
    @Autowired
    private AuthorRepository authorRepository;

    public AuthorRepository getAuthorRepository() {
        return authorRepository;
    }

    public void storePluralsightAuthors(List<PluralsightAuthor> psAuthors) {
        for (PluralsightAuthor author : psAuthors) {
            Author authorToSave = new Author(null, author.firstName(), author.lastName(), author.handle());
            authorRepository.saveAuthor(authorToSave);
        }
    }

}
