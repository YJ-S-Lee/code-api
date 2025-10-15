package code.codeapi.controller;

import code.codeapi.dto.MemberDTO;
import code.codeapi.dto.MemberModifyDTO;
import code.codeapi.service.MemberService;
import code.codeapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {

        log.info("React에서 가져온 access Token {}", accessToken);

        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);
        Map<String, Object> claims = memberDTO.getClaims();

        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;
    }

    @PutMapping("/api/member/modify")
    public Map<String,String> modify(@RequestBody MemberModifyDTO memberModifyDTO){
        log.info("member modify: " + memberModifyDTO);
        memberService.modifyMember(memberModifyDTO);
        return Map.of("result","modified");
    }
}
