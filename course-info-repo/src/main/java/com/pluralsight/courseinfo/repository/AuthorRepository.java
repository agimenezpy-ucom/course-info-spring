package com.pluralsight.courseinfo.repository;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pluralsight.courseinfo.domain.Author;

@Repository
public class AuthorRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_AUTHOR = """
        MERGE INTO Authors (firstName, lastName, handle) KEY (handle)
        VALUES (?, ?, ?)
    """;

    @Autowired
    public AuthorRepository(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveAuthor(Author author) {
         jdbcTemplate.update(INSERT_AUTHOR, author.firstName(), author.lastName(), author.handle());
    }

    public List<Author> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM Authors", (resultSet, rowNum) -> 
            new Author(resultSet.getLong(1), 
            resultSet.getString(2), 
            resultSet.getString(3), 
            resultSet.getString(4))
        );
    }

    public Optional<Author> findByHandle(String handle) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM Authors WHERE handle = ? LIMIT 1", (resultSet, rowNum) -> 
            new Author(resultSet.getLong(1), 
            resultSet.getString(2), 
            resultSet.getString(3), 
            resultSet.getString(4)), handle));
        } catch (DataAccessException e) {
            Optional<Author> result = Optional.empty();
            return result;
        }
    }
}
