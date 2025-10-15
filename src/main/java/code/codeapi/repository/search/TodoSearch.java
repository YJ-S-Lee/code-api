package code.codeapi.repository.search;

import code.codeapi.domain.Todo;
import code.codeapi.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
