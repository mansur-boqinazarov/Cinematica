package uz.pdp.service;

import org.springframework.stereotype.Component;
import uz.pdp.daos.TicketDao;
import uz.pdp.model.Tickets;

import java.util.List;

@Component
public class TicketService implements BaseService<Tickets> {

    private final TicketDao ticketDao;

    public TicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public void save(Tickets entity) {
        ticketDao.save(entity);
    }

    @Override
    public void update(Tickets entity) {
        ticketDao.update(entity);
    }

    @Override
    public void delete(int entity) {
        ticketDao.delete(entity);
    }

    @Override
    public Tickets getById(int id) {
        return ticketDao.getById(id);
    }

    @Override
    public List<Tickets> getAll() {
        return ticketDao.getAll();
    }

    public List<Integer> getBookedSeats(int id){
        return ticketDao.getBookedSeats(id);
    }

    public List<Tickets> getByUserId(int id){
        return ticketDao.findByUserId(id);
    }



}
