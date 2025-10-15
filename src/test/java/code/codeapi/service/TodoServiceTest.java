package code.codeapi.service;

import code.codeapi.domain.Todo;
import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.TodoDTO;
import code.codeapi.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Slf4j
class TodoServiceTest {
    @Autowired
    TodoService todoService;
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void 조회() {
        Long tno = 50L;
        log.info("50번 {}", todoService.get(tno));
    }

    @Test
    public void 등록() {
        //title.. 추가, 내용.. 추가
        Todo todo = Todo.builder()
                .title("title.. 추가")
                .content("content.. 추가")
                .dueDate(LocalDate.now())
                .build();
        todoRepository.save(todo);
    }

    @Test
    public void 수정() {
        //1번 수정
        long tno = 1L;
        //기존데이터 확인
        TodoDTO before = todoService.get(tno);
        //수정
        before.setTitle("title.. 수정");
        before.setComplete(true);
        before.setContent("content.. 수정");
        before.setDueDate(LocalDate.of(2025,10,1));
        //수정 실행
        todoService.modify(before);
    }

    @Test
    public void 삭제() {
        long tno = 96L;
        todoRepository.deleteById(tno);
    }

    @Test
    public void 페이징리스트(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        log.info("페이징리스트 기본값 {}", todoService.getlist(pageRequestDTO));
    }

    @Test
    public void 페이징리스트10번(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(10).build();
        log.info("10번페이지 {}", todoService.getlist(pageRequestDTO));
    }

    @Test
    public void 페이징리스트9번(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(9).build();
        log.info("9번페이지 {}", todoService.getlist(pageRequestDTO));
    }
}