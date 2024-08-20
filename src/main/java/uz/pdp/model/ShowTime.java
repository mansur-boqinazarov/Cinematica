package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowTime {
    private int id;
    private int screenId;
    private int movieId;
    private Timestamp showTime;
    private int price;
}
