package uz.pdp.service;

import org.springframework.stereotype.Service;
import uz.pdp.daos.TheaterDao;
import uz.pdp.model.Theater;

import java.util.List;

@Service
public class RoomService implements BaseService<Theater> {

    private final TheaterDao roomDao;

    public RoomService(TheaterDao roomDao) {
        this.roomDao = roomDao;
    }


    @Override
    public void save(Theater entity)   {
        roomDao.save(entity);
    }

    @Override
    public void update(Theater entity) {
        roomDao.update(entity);
    }

    @Override
    public void delete(int id) {
        roomDao.delete(id);
    }

    @Override
    public Theater getById(int id) {
        return roomDao.getById(id);
    }

    @Override
    public List<Theater> getAll() {
        return roomDao.getAll();
    }

    public void save(String name, int capacity) {
    }
}
