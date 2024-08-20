package uz.pdp.service;

import org.springframework.stereotype.Component;
import uz.pdp.daos.UserDao;
import uz.pdp.model.AuthUser;

import java.util.List;
import java.util.Optional;

@Component
public class UserService implements BaseService<AuthUser> {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    @Override
    public void save(AuthUser entity) {
        userDao.save(entity);
    }

    @Override
    public void update(AuthUser entity) {
        userDao.update(entity);
    }

    @Override
    public AuthUser getById(int id) {
        return userDao.getById(id);
    }

    @Override
    public List<AuthUser> getAll() {
        return userDao.getAll();
    }

    public Optional<AuthUser> getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public List<AuthUser> findByRole(String role) {
        return userDao.findByRole(role);
    }

}
