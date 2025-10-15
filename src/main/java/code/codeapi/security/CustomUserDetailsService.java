package code.codeapi.security;

import code.codeapi.domain.CodeMember;
import code.codeapi.dto.MemberDTO;
import code.codeapi.repository.CodeMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CodeMemberRepository codeMemberRepository;

    //loadUserByUsername()에서 사용자 정보를 조회하고 해당 사용자의 인증과 권한을 처리하게 된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("시큐리티가 사용자 정보조회를 처리하는가? {}", username);

        CodeMember member = codeMemberRepository.getWithRoles(username);
        //맴버는 entity이고 우리가 반환해야 하는 것은 memberDTO자료형이다.
        if(member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList()
                        .stream()
                        .map(memberRole -> memberRole.name()).collect(Collectors.toList()));
        log.info("로그인한 맴버 : {}", memberDTO);
        return memberDTO;
    }
}
