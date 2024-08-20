package uz.pdp.daos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.model.AuthUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserDao implements BaseDao<AuthUser> {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AuthUser> rowMapper = (rs, rowNum) -> mapUser(rs);

    private AuthUser mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");

        LocalDateTime createdAt = null;
        try {
            createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        } catch (SQLException e) {
            // Handle exception or leave empty if not crucial
        }

        return AuthUser.builder()
                .id(id)
                .fullName(fullName)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .createDate(createdAt)
                .build();
    }

    @Override
    public void save(AuthUser entity) {
        String sql = "INSERT INTO users (full_name, username, email, password) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql, entity.getFullName(), entity.getUsername(), entity.getEmail(), entity.getPassword());
    }

    @Override
    public void update(AuthUser entity) {
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public AuthUser getById(int id) {
        String query = "SELECT * FROM users WHERE id=?;";
        return jdbcTemplate.query(query, rowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<AuthUser> getAll() {
        String query = "SELECT * FROM users;";
        return jdbcTemplate.query(query, rowMapper);
    }

    public Optional<AuthUser> getByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ? LIMIT 1;";
        try {
            AuthUser user = jdbcTemplate.queryForObject(query, new Object[]{username}, rowMapper);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public List<AuthUser> findByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ?";
        return jdbcTemplate.query(sql, rowMapper, role);
    }
}
