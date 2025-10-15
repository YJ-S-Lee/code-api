package code.codeapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data   //@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor이 모두 합쳐진 애노테이션
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    private Long tno;

    private String title;

    private String content;

    private boolean complete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;  //2025-09-16
}
