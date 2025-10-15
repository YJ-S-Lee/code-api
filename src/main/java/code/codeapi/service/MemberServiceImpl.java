package code.codeapi.service;

import code.codeapi.domain.CodeMember;
import code.codeapi.domain.MemberRole;
import code.codeapi.dto.KakaoUserInfoDTO;
import code.codeapi.dto.MemberDTO;
import code.codeapi.dto.MemberModifyDTO;
import code.codeapi.repository.CodeMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final CodeMemberRepository codeMemberRepository;

    private final PasswordEncoder passwordEncoder;

    //accessToken을 이용해서 사용자 정보 가져오기 => 기존회원정보가 있는경우, 없는경우
    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        KakaoUserInfoDTO kakaoUserInfo = getNicknameFromKakaoAccessToken(accessToken);

        log.info("가져온 nickname: {}", kakaoUserInfo);

        Optional<CodeMember> result = codeMemberRepository.getCodeMemberByNickname(kakaoUserInfo.getNickname());

        //기존 회원의 경우 DTO로 변환 후 반환
        if(result.isPresent()) {
            MemberDTO memberDTO = entityToDTO(result.get());
            return memberDTO;
        }
        //새로운 회원인 경우, 비밀번호 임의로 생성
        CodeMember socialMember = makeSocialMember(kakaoUserInfo.getId(), kakaoUserInfo.getNickname());
        codeMemberRepository.save(socialMember);
        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;
    }

    //accessToken을 이용해서 사용자 정보 가져오는 메서드
    private KakaoUserInfoDTO getNicknameFromKakaoAccessToken(String accessToken) {
        //공식문서에서 주소 가져온다
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if(accessToken == null){
            throw new RuntimeException("Access Token is null");
        }

        RestTemplate restTemplate = new RestTemplate();

        //Authorization과 Content-Type 공식문서에 있음, HttpHeaders()=> spring꺼로 import
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        //header정보를 추가하기 위하여
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //실제로 보내야 한다. UriComponentsBuilder를 사용한다.
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        //일단 데이터를 보내야 한다., 이때 나오는 정보가 LinkedHashMap 형태로 나온다
        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);
        log.info("response {}", response ); //사용자 정보가 터미널로 로그에 찍힌다
        //사용자 정보에서 아이디를 가져오자. nickname=이유진, id=4490804600
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("bodtMap: {}", bodyMap);
        //bodtMap: {id=4490804600, connected_at=2025-10-13T00:55:04Z, properties={nickname=이유진}, kakao_account={profile_nickname_needs_agreement=false, profile={nickname=이유진, is_default_nickname=false}}}
        log.info("id: {}", bodyMap.get("id"));
        String id = String.valueOf(bodyMap.get("id"));

        LinkedHashMap<String, String> properties = bodyMap.get("properties");
        String nickname = properties.get("nickname");
        log.info("nickname: {}", properties.get("nickname"));

        return new KakaoUserInfoDTO(id, nickname);
    }

    //해당 이메일을 가진 회원이 없다면 새로운 회원을 추가할 때 패스워드를 임의로 생성한다.
    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for(int i=0; i<10; i++) {
            buffer.append((char)((int)(Math.random()*55)+65));
        }
        return buffer.toString();
    }

    //소셜회원 만들기
    private CodeMember makeSocialMember(String id, String nickname) {
        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);

        //회원 만들기
        CodeMember member = CodeMember.builder()
                .email(id+"@aaa.com")
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();
        member.addRole(MemberRole.USER);

        return member;
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<CodeMember> result = codeMemberRepository.getCodeMemberByNickname(memberModifyDTO.getNickname());

        CodeMember member = result.orElseThrow();
        member.setPw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.setSocial(false);
        member.setNickname(memberModifyDTO.getNickname());

        codeMemberRepository.save(member);
    }
}
