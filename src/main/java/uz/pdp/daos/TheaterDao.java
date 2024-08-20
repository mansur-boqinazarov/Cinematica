package uz.pdp.daos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.model.Theater;

import java.util.List;

@Component
public class TheaterDao implements BaseDao<Theater> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Theater> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String location = rs.getString("location");
        return new Theater(id, name, location);
    };

    public TheaterDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Theater entity) {
        String sql = "INSERT INTO rooms (name, location) VALUES (?, ?)";
        jdbcTemplate.update(sql, entity.getName(), entity.getLocation());
    }

    @Override
    public void update(Theater entity) {
        String sql = "UPDATE rooms SET name = ?, location = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getLocation(), entity.getId());

    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Theater getById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Theater> getAll() {
        String sql = "select * from rooms";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
