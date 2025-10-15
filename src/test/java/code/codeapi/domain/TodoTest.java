package code.codeapi.domain;

import code.codeapi.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class TodoTest {
    @Autowired
    TodoRepository todoRepository;

    @Test
    public void 할일추가백개() {
        for(int i=1; i<=100; i++) {
            Todo todo = Todo.builder()
                    .title("title.." + i)
                    .content("content.." + i)
                    .dueDate(LocalDate.now())
                    .build();
            todoRepository.save(todo);
        }
    }

    @Test
    public void 데이터조회() {
        //존재하는 번호로 확인
        long tno = 33L;
        // Repository를 이용해서 PK(기본키) 33번 데이터를 조회
        Optional<Todo> findTodo = todoRepository.findById(tno);
        // 값이 있으면 (존재한다면)
        if(findTodo.isPresent()) {
            // 조회된 엔티티의 tno 값이 33인지 확인 (단위 테스트 검증)
            assertEquals(33L, findTodo.get().getTno());
        }
    }

    @Test
    public void 데이터수정() {
        //33번의 title을 Modify로 수정
        long tno = 33L;
        Optional<Todo> findTodo = todoRepository.findById(tno);
        Todo todo = findTodo.orElseThrow();
        todo.setTitle("Modify 33");
        todo.setComplete(true);
        todo.setDueDate(LocalDate.of(2025, 9, 15));
        todoRepository.save(todo);
    }

    @Test
    public void 데이터삭제() {
        //100번의 데이터 삭제
        long tno = 100L;
        todoRepository.deleteById(tno);
    }

    @Test
    public void 페이징테스트() {
        //findAll()매서드를 통해 한번에 페이지 처리가 가능하다.
        //Pageable은 PageRequest.of(페이지번호, 사이즈)의 형태로 생성
        //페이지 번호는 0번부터 시작한다.
        //findAll()의 결과는 Page<엔티티> 타입으로 생성된다.
        //데이터의 수가 충분하면 내부적으로 데이터베이스에 count쿼리를 같이 실행한다.

        //내림차순(마지막부터 첫번째까지)
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        Page<Todo> result = todoRepository.findAll(pageable);
        log.info("결과 {}", result.getTotalElements());
        result.getContent().stream().forEach(todo -> log.info("데이터 {}", todo));
    }

//    @Test
//    public void 검색1() {
//        todoRepository.search1();
//    }
}