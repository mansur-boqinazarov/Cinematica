package uz.pdp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.pdp.daos.UserDao;
import uz.pdp.model.AuthUser;
import uz.pdp.model.ShowTime;
import uz.pdp.model.Tickets;
import uz.pdp.service.MovieService;
import uz.pdp.service.ScreenService;
import uz.pdp.service.ShowTimeService;
import uz.pdp.service.TicketService;
import uz.pdp.service.UserService;

import java.util.List;

@Controller
public class UserProfileController {

    private final UserDao userDao;
    private final TicketService ticketService;
    private final MovieService movieService;
    private final ScreenService screenService;
    private final UserService userService;
    private final ShowTimeService showTimeService;


    public UserProfileController(UserDao userDao, TicketService ticketService, MovieService movieService, ScreenService screenService, UserService userService, ShowTimeService showTimeService) {
        this.userDao = userDao;
        this.ticketService = ticketService;
        this.movieService = movieService;
        this.screenService = screenService;
        this.userService = userService;
        this.showTimeService = showTimeService;
    }

    @GetMapping("/user/profile")
    public String userProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        AuthUser user = userDao.getByUsername(username).orElse(null);

        if (user != null) {
            AuthUser authUser = new AuthUser();
            authUser.setId(user.getId());
            authUser.setFullName(user.getFullName());
            authUser.setUsername(user.getUsername());
            authUser.setEmail(user.getEmail());
            authUser.setRole(user.getRole());
            authUser.setCreateDate(user.getCreateDate());

            model.addAttribute("userProfile", authUser);

            List<Tickets> userTickets = ticketService.getByUserId(user.getId());
            model.addAttribute("userTickets", userTickets);
        }

        return "userProfile";
    }
    @GetMapping("/user/ticket/{ticketId}")
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

        return "redirect:/user/profile";
    }


}
