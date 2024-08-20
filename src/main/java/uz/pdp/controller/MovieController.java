package uz.pdp.controller;

import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.model.AuthUser;
import uz.pdp.model.Movie;
import uz.pdp.model.ShowTime;
import uz.pdp.model.Tickets;
import uz.pdp.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class MovieController {
    private final MovieService movieService;
    private final Path rootPath = Path.of("C:\\Users\\Acer\\OneDrive\\Рабочий стол\\cineeeemaaa\\src\\main\\resources\\static\\img");

    public MovieController(MovieService movieService) {
        this.movieService = movieService;

    }

    @PostMapping("/admin/addMovie")
    public String addMovie(@RequestParam("movieTitle") String title,
                           @RequestParam("description") String description,
                           @RequestParam("trailerUrl") String trailerUrl,
                           @RequestParam("categoryName") String categoryName,
                           @RequestParam("releaseDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate releaseDate,
                           @RequestParam("duration") int duration,
                           @RequestParam("posterImage") MultipartFile posterImage
    ){

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setTrailer_url(trailerUrl);
        movie.setCategoryName(categoryName);
        movie.setReleaseDate(releaseDate);
        movie.setDuration(duration);
        movie.setPosterImage(saveFile(posterImage));

        movieService.save(movie);

        return "redirect:/admin";
    }
    @GetMapping("/admin/showMovies")
    public String showMoviesPage(Model model) {
        List<Movie> moviesList = movieService.getAll();
        model.addAttribute("movies", moviesList);
        return "admin/showMovies";
    }
    @GetMapping("/admin/moviesDetails.html")
    public String getMovieDescription(@RequestParam("id") int id, Model model) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            model.addAttribute("movie", movie);
            return "admin/moviesDetails";
        } else {
            return "error";
        }
    }

    @GetMapping("/admin/addMovie")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/addMovie";
    }
    @SneakyThrows
    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String mimeType = StringUtils.getFilenameExtension(originalFilename);
        String generatedName = UUID.randomUUID() + "." + mimeType;
        Files.copy(file.getInputStream(), rootPath.resolve(generatedName), StandardCopyOption.REPLACE_EXISTING);
        return generatedName;
    }


}
