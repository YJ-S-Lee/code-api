package code.codeapi.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

public class MemberDTO extends User {

    private String email, pw, nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    //생성자 추가. 시큐리티 문법에 맞추어 생성자를 생성해 준다.
    //우리는 그냥 문자로 권한을 받으면 되
    // 시큐리티는 객체로 받아야한다. 그래서 new SimpleGrantedAuthority("ROLE_" + str)문자를 객체로  생성해준다.
    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(email, pw, roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    //JWT문자열을 만들어서 데이터를 주고받는다.
    //JWT문자열의 내용물을 크레임스(Claims)라고도 한다.
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }
}
