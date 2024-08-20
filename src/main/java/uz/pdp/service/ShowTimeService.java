package uz.pdp.service;

import org.springframework.stereotype.Component;
import uz.pdp.daos.ShowTimeDao;
import uz.pdp.model.ShowTime;

import java.util.List;

@Component
public class ShowTimeService implements BaseService<ShowTime>{

    private final ShowTimeDao showTimeDao;

    public ShowTimeService(ShowTimeDao showTimeDao) {
        this.showTimeDao = showTimeDao;
    }

    @Override
    public void save(ShowTime entity) {
        showTimeDao.save(entity);
    }

    @Override
    public void update(ShowTime entity) {
        showTimeDao.update(entity);
    }

    @Override
    public void delete(int entity) {
        showTimeDao.delete(entity);
    }

    @Override
    public ShowTime getById(int id) {
        return showTimeDao.getById(id);
    }

    @Override
    public List<ShowTime> getAll() {
        return showTimeDao.getAll();
    }

    public List<ShowTime> getShowTimeByMovieId(int movieId) {
        return showTimeDao.getShowTimesByMovieId(movieId);
    }
    public boolean existsByMovieId(int movieId) {
        return showTimeDao.existsByMovieId(movieId);
    }
}
