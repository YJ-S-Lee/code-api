package code.codeapi.security.filter;

import code.codeapi.dto.MemberDTO;
import code.codeapi.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
//모든 요청에 대해 체크하겠다는 뜻
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //return true 는 체크 안함, false는 체크한다는 뜻
        //Prefilight 요총은 체크하지 않음
        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }
        //지금 호출하는 경로는
        String path = request.getRequestURI();
        //member 경로는 호출하지 않음
        if(path.startsWith("/api/member/")){
            return true;    //체크하지 않음
        }
        //이미지 조회 경로는 체크하지 않는다면
        if(path.startsWith("/api/products/view")) {
            return true;
        }
        log.info("체크 url {}", path);
        return false;   //체크함
    }

    //요청과 응답을  실제로 구현
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //일단 끄집어낸다.
        String authHeaderStr = request.getHeader("Authorization");

        try {
            String accessToken = authHeaderStr.substring(7);    //앞의 7개는 잘라냄
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims" + claims);
            //성공하면 다음 목적지를 부른다.

            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO =  new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);
            log.info("맴버? {}", memberDTO);
            log.info("맴버 권한? {}", memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.info("에러 {}", e.getMessage());
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }
}
