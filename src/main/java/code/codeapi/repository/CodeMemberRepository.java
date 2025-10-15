package code.codeapi.repository;

import code.codeapi.domain.CodeMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeMemberRepository extends JpaRepository<CodeMember, String> {

    //조회 시 권한 목록까지 로딩되도록 해야한다.
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from CodeMember m where m.email = :email")
    CodeMember getWithRoles(@Param("email") String email);

    //nickname으로 찾기
    Optional<CodeMember> getCodeMemberByNickname(String nickname);
}
