package uz.pdp.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.daos.ScreenDao;
import uz.pdp.model.Movie;
import uz.pdp.model.Screens;
import uz.pdp.model.ShowTime;
import uz.pdp.service.MovieService;
import uz.pdp.service.ScreenService;
import uz.pdp.service.ShowTimeService;
import uz.pdp.service.TicketService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ShowTimeController {
    private final ShowTimeService showTimeService;
    private final ScreenDao screenDao;
    private final MovieService movieService;
    private final ScreenService screenService;
    private final TicketService ticketService;

    public ShowTimeController(ShowTimeService showTimeService, ScreenDao screenDao, MovieService movieService, ScreenService screenService, TicketService ticketService) {
        this.showTimeService = showTimeService;
        this.screenDao = screenDao;
        this.movieService = movieService;
        this.screenService = screenService;
        this.ticketService = ticketService;
    }

    @PostMapping("/admin/addShowTime")
    public String addShowTime(@RequestParam("movieId") int movieId,
                              @RequestParam("screenId") int screenId,
                              @RequestParam("showTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime showTime,
                              @RequestParam("price") int price) {
        ShowTime newShowTime = ShowTime.builder()
                .movieId(movieId)
                .screenId(screenId)
                .showTime(Timestamp.valueOf(showTime))
                .price(price)
                .build();
        showTimeService.save(newShowTime);
        return "redirect:/admin/moviesDetails.html?id=" + movieId;
    }
    @GetMapping("/admin/addShowTime.html")
    public String showAddShowTimePage(@RequestParam("movieId") int movieId, Model model) {
        model.addAttribute("movieId", movieId);

        List<Screens> screens = screenDao.getAll();
        model.addAttribute("screens", screens);

        return "admin/addShowTime";
    }
    @GetMapping("/showtime/{showTimeId}")
    public String viewShowTimeDetails(@PathVariable("showTimeId") int showTimeId, Model model) {
        ShowTime showTime = showTimeService.getById(showTimeId);
        if (showTime != null) {
            model.addAttribute("showTime", showTime);
            Movie movie = movieService.getById(showTime.getMovieId());
            model.addAttribute("movie", movie);
            Screens screen = screenService.getById(showTime.getScreenId());
            model.addAttribute("screen", screen);
            List<Integer> bookedSeats = ticketService.getBookedSeats(showTime.getId());
            model.addAttribute("bookedSeats", bookedSeats);

            return "showTimeDetails";
        }
        model.addAttribute("errorMessage", "Showtime not found.");
        return "error";
    }

}
