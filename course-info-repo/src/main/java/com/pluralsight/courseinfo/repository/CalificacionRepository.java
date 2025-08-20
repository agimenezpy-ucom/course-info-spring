package com.pluralsight.courseinfo.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pluralsight.courseinfo.domain.Calificacion;

public class CalificacionRepository {
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_CALIFICACION = """
        INSERT INTO Calificacion (author_id, course_id, nota) VALUES (?, ?, ?)
    """;

    private static final String UPDATE_CALIFICACION = """
        MERGE INTO Calificacion (id, author_id, course_id, nota) KEY (id)
        VALUES (?, ?, ?, ?)
    """;

    @Autowired
    public CalificacionRepository(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveCalificacion(Calificacion calificacion) {
        if (calificacion.id() == null) {
            jdbcTemplate.update(INSERT_CALIFICACION, calificacion.authorId(), calificacion.courseId(), calificacion.nota());
        }
        else {
            jdbcTemplate.update(UPDATE_CALIFICACION, calificacion.id(), calificacion.authorId(), calificacion.courseId(), calificacion.nota());
        }
    }

    public List<Calificacion> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM Calificacion", (resultSet, rowNum) -> 
            new Calificacion(resultSet.getLong(1), 
            resultSet.getLong(2), 
            resultSet.getString(3), 
            resultSet.getInt(4))
        );
    }
}
