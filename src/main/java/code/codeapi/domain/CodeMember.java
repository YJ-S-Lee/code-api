package code.codeapi.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "memberRoleList")
@Getter
public class CodeMember {

    @Id
    private String email;

    @Setter
    private String pw;

    @Setter
    private String nickname;

    //소셜 로그인. true-소셜 로그인 사용자. false-일반 사용자.
    @Setter
    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    //권한 부여
    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    //권한 삭제
    public void clearRole() {
        memberRoleList.clear();
    }
}
