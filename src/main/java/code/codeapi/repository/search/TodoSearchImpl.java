package code.codeapi.repository.search;

import code.codeapi.domain.QTodo;
import code.codeapi.domain.Todo;
import code.codeapi.dto.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Slf4j
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {
        log.info("동작중");
        QTodo todo = QTodo.todo;
        //todo에서 쿼리를 뽑는다.
        JPQLQuery<Todo> query = from(todo);
        //제목에 1이 포함된 걸 검색
        //검색은 pageable에 추가할 수 있으므로 삭제한다.
//        query.where(todo.title.contains("1"));
        //페이징처리 추가
//        PageRequest pageable = PageRequest.of(1, 10, Sort.by("tno").descending());
        //1부터 시작하므로 -1을 한다.
        PageRequest pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("tno").descending());

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, query);
        //쿼리 실행
        List<Todo> list = query.fetch();  //목록데이터 가져올 때
        long total = query.fetchCount(); //Long타입으로 결과를 가져다 준다.
        //return값을 공식문서에서 요구한대로 채워주면 페이징처리가 된다.
        return new PageImpl<>(list, pageable, total);
    }
}
