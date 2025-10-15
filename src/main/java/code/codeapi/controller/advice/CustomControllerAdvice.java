package code.codeapi.controller.advice;

import code.codeapi.util.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomControllerAdvice {

    //목록에 없는 번호 조회 시 서버쪽에선 500번 에러가 나고, NoSuchElementException이 발생한다.
    //<?>는 어떤 자료형을 써야할 지 모를 때 쓴다.
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notExist(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", e.getMessage()));
    }

    //페이징처리시 list로 호출할 때 페이지 번호가 잘못되었을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notExist(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg", e.getMessage()));
    }

    //APIRefreshController에서 에러가 발생하면 CustomJWTException을 반환한다
    @ExceptionHandler(CustomJWTException.class)
    public ResponseEntity<?> handleJWTException(CustomJWTException e){
        String msg = e.getMessage();
        return ResponseEntity.ok().body(Map.of("error", msg));
    }
}
