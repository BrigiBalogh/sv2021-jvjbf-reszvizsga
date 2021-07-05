package cinema;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private ModelMapper modelMapper;

    private AtomicLong idGenerator = new AtomicLong();

    private List<Movie> movies = Collections.synchronizedList(new ArrayList<>());

    public MovieService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MovieDTO> getMovies(Optional<String> prefix) {
        Type targetListType = new TypeToken<List<MovieDTO>>(){}.getType();
        List<Movie> filtered = movies.stream()
                .filter(m -> prefix.isEmpty() || m.getTitle().toLowerCase().contains(prefix.get().toLowerCase()))
                .collect(Collectors.toList());
        return modelMapper.map(filtered, targetListType);
    }


    public MovieDTO findMovieById(long id) {
        return modelMapper.map(movies.stream()
                        .filter(m -> m.getId() == id).findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id)),
                MovieDTO.class);

    }

    public MovieDTO createNewReservation(CreateReservationCommand command) {
        Movie movie = new Movie(idGenerator.incrementAndGet(), command.getTitle(), command.getDate(),
                command.getMaxSeats(), command.getMaxSeats());
        movies.add(movie);
        return modelMapper.map(movie, MovieDTO.class);

    }

    public void reserve(int id, int seats) {
        Movie movie = movies.stream()
                .filter(m -> m.getId() == id).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
        movie.reserve(seats);
    }

    public MovieDTO updateMovie (long id, UpdateDateCommand command) {
        Movie movie = movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
        movie.setDate(command.getDate());
        return modelMapper.map(movie, MovieDTO.class);
    }

    public void deleteMovies(long id) {
       Movie movie = movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        movies.remove(movie);
    }
}
