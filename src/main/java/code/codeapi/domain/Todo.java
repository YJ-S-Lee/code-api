package code.codeapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private boolean complete;

    @Setter
    private LocalDate dueDate;
}
