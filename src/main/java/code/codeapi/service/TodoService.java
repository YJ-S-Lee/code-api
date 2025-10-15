package code.codeapi.service;

import code.codeapi.domain.Todo;
import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.TodoDTO;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface TodoService {
    //조회기능, TodoDTO 자료형으로 리턴 tno를 받아 조회
    //public 생략가능
    TodoDTO get(Long tno);

    //등록하기
    Long register(TodoDTO dto);

    //수정하기
    void modify(TodoDTO dto);

    //삭제하기
    void remove(Long tno);

    //목록(페이징)처리
    PageResponseDTO<TodoDTO> getlist(PageRequestDTO pageRequestDTO);

    //java8버전부터는 default기능이 추가되어 기본 기능을 설정해줄 수 있다.
    //엔티티를 TodoDTO로 변환
    default TodoDTO entityToDTO(Todo todo) {
        TodoDTO todoDTO = TodoDTO.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .complete(todo.isComplete())
                .dueDate(todo.getDueDate())
                .build();
        return todoDTO;
    }

    //TodoDTO를 엔티티로 변환
    default Todo dtoToEntity(TodoDTO todoDTO) {
        Todo todo = Todo.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .complete(todoDTO.isComplete())
                .dueDate(todoDTO.getDueDate())
                .build();
        return todo;
    }
}
