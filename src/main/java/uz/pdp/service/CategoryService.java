package uz.pdp.service;

import org.springframework.stereotype.Service;
import uz.pdp.daos.CategoryDao;
import uz.pdp.model.Category;

import java.util.List;

@Service
public class CategoryService implements BaseService<Category> {
    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void save(Category entity) {
        categoryDao.save(entity);
    }

    @Override
    public void update(Category entity) {
        categoryDao.update(entity);
    }

    @Override
    public void delete(int entity) {
        categoryDao.delete(entity);
    }

    @Override
    public Category getById(int id) {
        return categoryDao.getById(id);
    }

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }
}
