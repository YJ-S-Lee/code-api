package code.codeapi.service;

import code.codeapi.domain.CodeMember;
import code.codeapi.dto.MemberDTO;
import code.codeapi.dto.MemberModifyDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {
    MemberDTO getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    //엔티티 => dto로 변환
    default MemberDTO entityToDTO(CodeMember member) {
        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
        return dto;
    }
}
