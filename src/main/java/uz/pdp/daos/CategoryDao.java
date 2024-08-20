package uz.pdp.daos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.model.Category;

import java.util.List;

@Component
public class CategoryDao implements BaseDao<Category> {

    private final JdbcTemplate jdbcTemplate;

    public CategoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Category> rowMapper = (rs, rowNum) -> {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        return Category.builder().categoryId(id).categoryName(name).build();
    };

    @Override
    public void save(Category entity) {
        String sql = "insert into categories (category_name) values (?)";
        jdbcTemplate.update(sql, entity.getCategoryName());

    }

    @Override
    public void update(Category entity) {
        String sql = "update categories set category_name=? where category_id=?";
        jdbcTemplate.update(sql , entity.getCategoryName(), entity.getCategoryId());
    }

    @Override
    public void delete(int entity) {
        String sql = "delete from categories where category_id=?";
        jdbcTemplate.update(sql, entity);
    }

    @Override
    public Category getById(int id) {
        String sql = "select * from categories where category_id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Category> getAll() {
        String sql = "select * from categories";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
