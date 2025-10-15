package code.codeapi.repository;

import code.codeapi.domain.CodeMember;
import code.codeapi.domain.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CodeMemberRepositoryTest {

    @Autowired
    CodeMemberRepository codeMemberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //회원가입 시 password를 인코딩 해야한다.
    //10명의 회원을 만들어보자

    @Test
    public void 회원가입10명() {
        for(int i=0; i<10; i++) {
            CodeMember member = CodeMember.builder()
                    .email("user" + i + "@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("user" + i)
                    .build();
            member.addRole(MemberRole.USER);
            if(i>=5) member.addRole(MemberRole.MANAGER);
            if(i>=8) member.addRole(MemberRole.ADMIN);

            codeMemberRepository.save(member);
        }
    }

    @Test
    public void 회원조회() {
        String email = "user9@aaa.com";
        CodeMember member = codeMemberRepository.getWithRoles(email);
        log.info("9번 회원: {}", member);
        log.info("9번회원의 권한 : {}", member.getMemberRoleList());
    }

    //회원삭제
    @Test
    void 회원삭제() {
        String email = "4490804600@aaa.com";

        CodeMember member = codeMemberRepository.getWithRoles(email);
        if(member == null) {
            log.info("회원이 존재하지 않습니다. 이메일 : {}", email);
            return;
        }
        codeMemberRepository.delete(member);
        log.info("회원 삭제 완료 {}", email);
    }
}