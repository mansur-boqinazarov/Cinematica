package uz.pdp.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.pdp.model.*;
import uz.pdp.service.*;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final MovieService movieService;
    private final ShowTimeService showTimeService;


    public HomeController(UserService userService, MovieService movieService, ShowTimeService showTimeService) {
        this.userService = userService;
        this.movieService = movieService;
        this.showTimeService = showTimeService;

    }

    @GetMapping("/")
    public String home(Model model) {
        addUserInfoToModel(model);
        addMoviesToModel(model);
        return "homePage";
    }

    @GetMapping("/movies")
    public String moviePage(Model model) {
        addMoviesToModel(model);
        return "movie";
    }

    @GetMapping("/movies/details/{id}")
    public String userMovieDetails(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            List<ShowTime> showTimes = showTimeService.getShowTimeByMovieId(id);
            model.addAttribute("movie", movie);
            model.addAttribute("showTimes", showTimes);
            return "movieDetails";
        }
        model.addAttribute("errorMessage", "Movie not found.");
        return "error";
    }












    private void addUserInfoToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("role", userService.getByUsername(userDetails.getUsername()).map(AuthUser::getRole).orElse(null));
        } else {
            model.addAttribute("username", null);
            model.addAttribute("role", null);
        }
    }

    private void addMoviesToModel(Model model) {
        List<Movie> allMovies = movieService.getAll();

        List<Movie> availableMovies = allMovies.stream()
                .filter(movie -> showTimeService.existsByMovieId(movie.getId()))
                .toList();

        List<Movie> unavailableMovies = allMovies.stream()
                .filter(movie -> !showTimeService.existsByMovieId(movie.getId()))
                .toList();

        model.addAttribute("availableMovies", availableMovies);
        model.addAttribute("unavailableMovies", unavailableMovies);
    }

}
