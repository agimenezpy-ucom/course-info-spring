package com.pluralsight.courseinfo.repository;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pluralsight.courseinfo.domain.Course;

@Repository
public class CourseRepository {
    public static final Logger LOG = LoggerFactory.getLogger(CourseRepository.class);
    
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_COURSE = """
        MERGE INTO Courses (id, name, length, url)
        VALUES (?, ?, ?, ?)
    """;

    private static final String ADD_NOTES = """
        UPDATE Courses SET notes = ?
        WHERE id = ?
    """;

    private static final String DELETE_COURSE = """
        DELETE FROM Courses WHERE id = ?
    """;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveCourse(Course course) {
        jdbcTemplate.update(INSERT_COURSE, course.id(), course.name(), course.length(), course.url());
    }

    public List<Course> getAllCourses() {
        return jdbcTemplate.query("SELECT * FROM Courses", (resultSet, rowNum) -> 
            new Course(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getLong(3), 
                resultSet.getString(4),
                Optional.ofNullable(resultSet.getString(5))
            )
        );
    }

    public void addNotes(String id, String notes) {
        jdbcTemplate.update(ADD_NOTES, notes, id);
    }

    public void deleteCourse(Course course) {
        jdbcTemplate.update(DELETE_COURSE, course.id());
    }

    public Optional<Course> findById(String id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM Courses WHERE id = ? LIMIT 1", (resultSet, rowNum) -> 
            new Course(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getLong(3), 
                resultSet.getString(4),
                Optional.ofNullable(resultSet.getString(5))
            ), id));
        } catch (DataAccessException e) {
            LOG.error("Error retreving id " + id, e);
            Optional<Course> result = Optional.empty();
            return result;
        }
    }
}
