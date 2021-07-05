package cinema;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateDateCommand {

    private LocalDateTime date;

}
