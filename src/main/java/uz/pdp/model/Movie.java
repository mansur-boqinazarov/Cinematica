package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    private int id;
    private String title;
    private String description;
    private String Trailer_url;
    private LocalDate releaseDate;
    private String categoryName;
    private int duration;
    private String posterImage;
}
