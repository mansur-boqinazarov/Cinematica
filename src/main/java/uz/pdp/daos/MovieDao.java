package uz.pdp.daos;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import uz.pdp.model.Movie;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class MovieDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;


    public MovieDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Movie> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String trailerUrl = rs.getString("trailer_url");
        LocalDate releaseDate = convertToLocalDate(rs.getTimestamp("release_date"));
        int duration = rs.getInt("duration");
        String categoryName = rs.getString("category_name");
        String posterImage = rs.getString("poster_image");

        return new Movie(id, name, description, trailerUrl, releaseDate, categoryName, duration, posterImage);
    };

    public void save(Movie movie) {
        String sql = "INSERT INTO movies(name, description, release_date, category_name, poster_image, duration, trailer_url) " +
                "VALUES(:name, :description, :release_date, :category_name, :poster_image, :duration, :trailer_url)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", movie.getTitle())
                .addValue("description", movie.getDescription())
                .addValue("release_date", convertToTimestamp(movie.getReleaseDate()))
                .addValue("category_name", movie.getCategoryName())
                .addValue("poster_image", movie.getPosterImage())
                .addValue("duration", movie.getDuration())
                .addValue("trailer_url", movie.getTrailer_url());

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void update(Movie movie) {
        String sql = "UPDATE movies SET name=:name, description=:description, release_date=:release_date, category_name=:category_name, " +
                "poster_image=:poster_image,  duration=:duration, trailer_url=:trailer_url WHERE id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", movie.getTitle())
                .addValue("description", movie.getDescription())
                .addValue("release_date", convertToTimestamp(movie.getReleaseDate()))
                .addValue("category_name", movie.getCategoryName())
                .addValue("poster_image", movie.getPosterImage())
                .addValue("duration", movie.getDuration())
                .addValue("trailer_url", movie.getTrailer_url())
                .addValue("id", movie.getId());

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void delete(int id) {
        String sql = "DELETE FROM movies WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Movie getById(int id) {
        String sql = "SELECT * FROM movies WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    public List<Movie> getAll() {
        String sql = "SELECT * FROM movies";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    private Timestamp convertToTimestamp(LocalDate localDate) {
        return localDate == null ? null : Timestamp.valueOf(localDate.atStartOfDay());
    }

    private LocalDate convertToLocalDate(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().toLocalDate();
    }
    public Optional<String> findNameById(int movieId) {
        String sql = "SELECT name FROM movies WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class, movieId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
