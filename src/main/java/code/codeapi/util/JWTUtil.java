package code.codeapi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.InvalidClassException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.MalformedInputException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class JWTUtil {
    //JWT의 서명을 생성할 때 사용하는 비밀 키, 최소 256비트(32자 이상)가 필요하다.
    //HMAC-SHA 알고리즘을 사용
    private static String key = "1234567890123456789012345678901234567890";

    //JWT문자열 생성을 위한 generateToken() 매서드
    //입력으로 전달된 valueMap과 유효 시간(min)을 바탕으로 JWT를 생성
    //valueMap은 JWT의  클레임(Claims)이다. 예를 들어,  사용자 정보같은 데이터를 담을 수 있다.
    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey key = null;
        try {
            //hmacShakeyFor, 비밀키를 HMAC-SHA 알고리즘용 키로 변환
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String jwtStr = Jwts.builder()
                //JWT의 헤더를 성정
                .setHeader(Map.of("typ", "JWT"))
                //클레임 설정
                .setClaims(valueMap)
                //발급시간 - 토큰이 언제 발급되었는지를 기록
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                //토큰의 유효기간을 설정
                //plusMinutes(min) -> 현재 시간에 min(10분)을 더해 만료 시간을 계산
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) //만료시간
                //HMAC-SHA 알고리즘을 서명을 만듬
                .signWith(key)
                //JWT를 직렬화하여 최종 문자열로 반환
                .compact();
        return jwtStr;
    }

    //검증을 위한 validate Token()
    //입렵값 token은 검증대상의 JWT문자열
    public static Map<String, Object> validateToken(String token) {
        //토큰의 클레임 데이터를 저장할 변수, 클레임은 토큰에 담긴 정보(ket-value쌍)으로 이루어짐
        Map<String, Object> claim = null;
        //비밀키
        SecretKey key = null;
        try {
            //JWT생성 시 사용한 키와 동일해야 함
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

            claim = Jwts.parserBuilder()    //JWT 문자열을 파싱하는 객체를 빌드
                    .setSigningKey(key) //JET의 서명을 겁증하기 위해 사용할 비밀 키를 설정
                    .build()
                    .parseClaimsJws(token)  //입력받은 JWT문자열을 파싱하여 유효성을 확인, 서명이 유효한지, 토큰이 만료되지 않았는지 확인
                    .getBody(); //검증이 성공하면 트큰의 페이로드(Payload) 부분에 포함된 클레임 데이터를 반환
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException("MalFormed");  //토큰이 잘못된 형식으로 작성된 경우
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("Expired");  //토큰이 만료되었거나, 만료 시간이 잘못된 경우
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid");  //JWT 처리 중, 클레임 값이 특정 검증 조건을 충족하지 않을 때
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JwtError");  //JWT 생성, 검증, 또는 파실 과정에서 발생할 수 있는 다양한 문제
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }
        return claim;   //클레임 데이터는 Map<String, Object>로 반환
    }
}
