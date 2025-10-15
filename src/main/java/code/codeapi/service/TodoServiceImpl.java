package code.codeapi.service;

import code.codeapi.domain.Todo;
import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.TodoDTO;
import code.codeapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    //조회
    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        return entityToDTO(todo);
    }

    //등록
    @Override
    public Long register(TodoDTO dto) {
        Todo todo = dtoToEntity(dto);
        Todo result = todoRepository.save(todo);
        return result.getTno();
    }

    //수정
    @Override
    public void modify(TodoDTO dto) {
        //데이터베이스에 가서 가져온다.
        Optional<Todo> result = todoRepository.findById(dto.getTno());
        Todo todo = result.orElseThrow();

        todo.setTitle(dto.getTitle());
        todo.setContent(dto.getContent());
        todo.setComplete(dto.isComplete());
        todo.setDueDate(dto.getDueDate());

        todoRepository.save(todo);
    }

    //삭제
    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    //목록(페이징)처리
    @Override
    public PageResponseDTO<TodoDTO> getlist(PageRequestDTO pageRequestDTO) {

        //Todo의 리스트들을 가져온다
        Page<Todo> result = todoRepository.search1(pageRequestDTO);

        //TodoDTO 리스트가 화면에 나가야한다.
        List<TodoDTO> dtoList = result.get().map(todo -> entityToDTO(todo)).collect(Collectors.toList());

        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();

        return responseDTO;
    }
}
