package uz.pdp.daos;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.model.Screens;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ScreenDao implements BaseDao<Screens> {

    private final JdbcTemplate jdbcTemplate;

    public ScreenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<Screens> rowMapper = (rs,rowMapper) -> mapScreen(rs);
        private Screens mapScreen(ResultSet rs) throws SQLException {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int roomId = rs.getInt("room_id");
            int capacity = rs.getInt("capacity");
            return Screens.builder()
                    .id(id)
                    .name(name)
                    .roomId(roomId)
                    .capacity(capacity)
                    .build();
        }



    @Override
    public void save(Screens entity) {
        String sql = "INSERT INTO screens (name, room_id, capacity) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, entity.getName(), entity.getRoomId(), entity.getCapacity());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error saving screen: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Screens entity) {
        String sql = "UPDATE screens SET name = ?, room_id = ? , capacity =? where id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getRoomId(), entity.getId(), entity.getCapacity());
    }

    @Override
    public void delete(int entity) {
        String sql = "DELETE FROM screens WHERE id = ?";
        jdbcTemplate.update(sql, entity);
    }

    @Override
    public Screens getById(int id) {
        String sql = "SELECT * FROM screens WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);

    }

    @Override
    public List<Screens> getAll() {
        String sql = "SELECT s.id, s.name, s.room_id, r.name AS room_name, s.capacity " +
                     "FROM screens s " +
                     "JOIN rooms r ON s.room_id = r.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Screens screen = new Screens();
            screen.setId(rs.getInt("id"));
            screen.setName(rs.getString("name"));
            screen.setRoomId(rs.getInt("room_id"));
            screen.setCapacity(rs.getInt("capacity"));
            return screen;
        });
    }
    public List<Screens> findByRoomId(int roomId) {
        String sql = "SELECT id, name FROM screens WHERE room_id = ?";
        return jdbcTemplate.query(sql, rowMapper, roomId);
    }

    public Optional<String> findCinemaNameByScreenId(int screenId) {
        String sql = """
            SELECT r.name 
            FROM screens s
            INNER JOIN rooms r ON s.room_id = r.id
            WHERE s.id = ?
        """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class, screenId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
