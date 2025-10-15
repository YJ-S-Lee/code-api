package code.codeapi.controller;

import code.codeapi.domain.Todo;
import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.TodoDTO;
import code.codeapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController //REST API(Json/XML)를 반환할 때 사용
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    //조회
    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    //리스트
    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list... {}", pageRequestDTO);
        return todoService.getlist(pageRequestDTO);
    }

    //post방식 - json형식으로 받고 return타입을 json형식으로 해줘야 한다
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
        log.info("TodoDTO {}", todoDTO);
        Long tno = todoService.register(todoDTO);
        return Map.of("TNO", tno);
    }

    //수정, put방식으로 처리, 결과는 json으로
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDTO todoDTO) {
        //수정하기 전, 안전하게 tno값을 넣어준다.
        todoDTO.setTno(tno);
        log.info("수정 {}", todoDTO);
        todoService.modify(todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    //삭제
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno) {
        log.info("삭제 {}", tno);
        todoService.remove(tno);
        return Map.of("RESULT", "SUCCESS");
    }
}
