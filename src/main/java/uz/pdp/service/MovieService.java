package uz.pdp.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.daos.MovieDao;
import uz.pdp.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService implements BaseService<Movie> {


    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }


    private final Path rootPath = Path.of("C:\\Users\\gayra\\OneDrive\\Desktop\\file\\Cinematica\\src\\main\\resources\\static\\img");


public void addMovie(Movie movie, MultipartFile posterFile) throws IOException {
    String originalFilename = posterFile.getOriginalFilename();

    if (originalFilename == null || originalFilename.isEmpty()) {
        throw new IllegalArgumentException("File name is invalid");
    }

    String extension = StringUtils.getFilenameExtension(originalFilename);
    if (extension == null) {
        throw new IllegalArgumentException("File extension is invalid");
    }
    String generatedFileName = UUID.randomUUID() + "." + extension;
    Path destinationPath = rootPath.resolve(generatedFileName);
    Files.createDirectories(rootPath);
    try (InputStream inputStream = posterFile.getInputStream()) {
        Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
        throw new IOException("Failed to save the file", e);
    }

    movie.setPosterImage(generatedFileName);
    movieDao.save(movie);
}

    @Override
    public void save(Movie entity){
        movieDao.save(entity);
    }

    @Override
    public void update(Movie entity) {
        movieDao.update(entity);
    }

    @Override
    public void delete(int entity) {
        movieDao.delete(entity);
    }

    @Override
    public Movie getById(int id) {
        return movieDao.getById(id);
    }

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }

    public Optional<String> getMovieNameById(int id) {
        return movieDao.findNameById(id);
    }

}