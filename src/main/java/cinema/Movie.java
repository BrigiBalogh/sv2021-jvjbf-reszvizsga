package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private Long id;
    private String title;
    private LocalDateTime date;
    private int maxSeats;
    private int freeSpaces;

    public void reserve(int seats) {
        if (freeSpaces >= seats) {
            freeSpaces -= seats;
        } else {
            throw new IllegalStateException();
        }
    }
}
