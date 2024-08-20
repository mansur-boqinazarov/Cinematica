package uz.pdp.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.model.AuthUser;
import uz.pdp.model.ShowTime;
import uz.pdp.model.Tickets;
import uz.pdp.service.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
public class TicketController {
    private final UserService userService;
    private final MovieService movieService;
    private final ShowTimeService showTimeService;
    private final ScreenService screenService;
    private final TicketService ticketService;

    public TicketController(UserService userService, MovieService movieService, ShowTimeService showTimeService, ScreenService screenService, TicketService ticketService) {
        this.userService = userService;
        this.movieService = movieService;
        this.showTimeService = showTimeService;
        this.screenService = screenService;
        this.ticketService = ticketService;
    }
    @GetMapping("/admin/ticket/{ticketId}")
    public String viewTicketDetails(@PathVariable("ticketId") int ticketId, Model model) {
        Tickets ticket = ticketService.getById(ticketId);

        if (ticket != null) {
            ShowTime showTime = showTimeService.getById(ticket.getShowtimeId());

            if (showTime != null) {
                String movieName = movieService.getMovieNameById(showTime.getMovieId()).orElse("Unknown Movie");
                String cinemaName = screenService.getCinemaNameByScreenId(showTime.getScreenId()).orElse("Unknown Cinema");
                AuthUser user = userService.getById(ticket.getUserId());

                model.addAttribute("ticket", ticket);
                model.addAttribute("fullname", user.getFullName());
                model.addAttribute("movieName", movieName);
                model.addAttribute("cinemaName", cinemaName);
                return "ticketDetails";
            }
        }

        return "redirect:/admin/profile";
    }
    @PostMapping("/buy-ticket")
    public String buyTicket(@RequestParam("seats") String seats,
                            @RequestParam("price") double totalPrice,
                            @RequestParam("showtimeId") int showtimeId,
                            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AuthUser user = userService.getByUsername(userDetails.getUsername()).orElse(null);

        ShowTime showTime = showTimeService.getById(showtimeId);

        if (showTime == null || user == null) {
            model.addAttribute("errorMessage", "ShowTime or User not found.");
            return "errorPage";
        }

        List<Integer> bookedSeats = ticketService.getBookedSeats(showTime.getId());
        model.addAttribute("bookedSeats", bookedSeats);

        String[] seatNumbers = seats.split(",");
        if (seatNumbers.length == 0) {
            model.addAttribute("errorMessage", "No seats selected.");
            return "errorPage";
        }

        double pricePerSeat = totalPrice / seatNumbers.length;
        for (String seatNumber : seatNumbers) {
            try {
                int seatNo = Integer.parseInt(seatNumber.trim());

                Tickets ticket = Tickets.builder()
                        .userId(user.getId())
                        .showtimeId(showTime.getId())
                        .time(Timestamp.from(Instant.now()))
                        .price(pricePerSeat)
                        .seatNumber(seatNo)
                        .build();
                ticketService.save(ticket);
            } catch (NumberFormatException e) {
                model.addAttribute("errorMessage", "Invalid seat number format: " + seatNumber);
                return "errorPage";
            } catch (DataIntegrityViolationException e) {
                model.addAttribute("errorMessage", "Failed to save ticket for seat number: " + seatNumber);
                return "errorPage";
            }
        }

        String movieName = movieService.getMovieNameById(showTime.getMovieId()).orElse("Unknown Movie");
        String cinemaName = screenService.getCinemaNameByScreenId(showTime.getScreenId()).orElse("Unknown Cinema");

        model.addAttribute("fullname", user.getFullName());
        model.addAttribute("movieName", movieName);
        model.addAttribute("cinemaName", cinemaName);
        model.addAttribute("seats", seats);
        model.addAttribute("currentTime", showTime.getShowTime());
        model.addAttribute("price", totalPrice);

        return "buyTicket";
    }

}
