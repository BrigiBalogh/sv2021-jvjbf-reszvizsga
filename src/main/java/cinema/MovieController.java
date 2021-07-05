package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;




@RestController
@RequestMapping("/api/cinema")
public class MovieController {

    private MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovieDTO> getMovies(@RequestParam Optional<String> title) {
        return service.getMovies(title);
    }


    @GetMapping("/{id}")
    public ResponseEntity findMovieById(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(service.findMovieById(id));

        } catch (IllegalArgumentException iae) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDTO createMovie( @Valid @RequestBody CreateReservationCommand command) {
        return service.createMovie(command);
    }




    @PostMapping("/{id}/reserve")
    public void reserve(@PathVariable int id,@RequestBody int seats) {
        service.reserve(id, seats);
    }


    @PutMapping("/{id}")
    public MovieDTO updateMovie(@PathVariable("id") long id, @RequestBody UpdateDateCommand command) {
        return service.updateMovie(id, command);
    }

    @DeleteMapping
    public void deleteAllMovies() {
        service.deleteAllMovies();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovies(@PathVariable("id") long id) {
        service.deleteMovies(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException iae) {
        Problem problem =
                Problem.builder()
                        .withType(URI.create("cinema/not-found"))
                        .withTitle("Not found")
                        .withStatus(Status.NOT_FOUND)
                        .withDetail(iae.getMessage())
                        .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Problem> handleBadReservation(IllegalStateException exception) {
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/bad-reservation"))
                .withTitle("Bad request")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);

    }

}