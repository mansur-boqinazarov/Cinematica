package uz.pdp.service;

import org.springframework.stereotype.Component;
import uz.pdp.daos.ScreenDao;
import uz.pdp.model.Screens;

import java.util.List;
import java.util.Optional;

@Component
public class ScreenService implements BaseService<Screens> {
    private final ScreenDao screensDto;

    public ScreenService(ScreenDao screensDto) {
        this.screensDto = screensDto;
    }

    @Override
    public void save(Screens entity) {
        screensDto.save(entity);
    }

    @Override
    public void update(Screens entity) {
        screensDto.update(entity);
    }

    @Override
    public void delete(int entity) {
        screensDto.delete(entity);
    }

    @Override
    public Screens getById(int id) {
        return screensDto.getById(id);
    }

    @Override
    public List<Screens> getAll() {
        return screensDto.getAll();
    }

    public Optional<String> getCinemaNameByScreenId (int screenId) {
        return screensDto.findCinemaNameByScreenId(screenId);
    }
}
