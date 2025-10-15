package code.codeapi.repository;

import code.codeapi.domain.Todo;
import code.codeapi.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
    Long tno(Long tno);
}
