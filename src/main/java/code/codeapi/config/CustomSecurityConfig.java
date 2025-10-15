package code.codeapi.config;

import code.codeapi.security.filter.JWTCheckFilter;
import code.codeapi.security.handler.APILoginFailHandler;
import code.codeapi.security.handler.APILoginSuccessHandler;
import code.codeapi.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("......security config");

        //CORS 정책 사용하기
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        //CSRF 사용하지 않음
        http.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.disable();
        });

        //뷰가 없으므로 시큐리티가 제공하는 LoginForm을 사용한다.
        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());
            config.failureHandler(new APILoginFailHandler());
        });
        
        //사용자 아이디와 패스워드를 검증하는 필터 전에 우리가 만든 필터를 동작시킨다
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); //JWT 체크

        //유효시간이 지나지 않았지만 권한이 없는 사용자가 가진 토큰을 사용하는 경우
        http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        //CorsConfiguration 객체를 생성한다. 이 객체는 CORS 정책을 정의한다.
        CorsConfiguration configuration = new CorsConfiguration();

        //모든 Origin을 허용한다.
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        //허용할 HTTP 매서드를 설정
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));

        //허용할 HTTP 요청 헤더를 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        //클라이언트가 자격 증명(쿠키, 인증 정보 등)을 포함한 요청을 보낼 수 있도록 허용한다.
        //true로 설정되면 서버는 자격 증명 있는 요청을 신뢰한다.
        configuration.setAllowCredentials(true);

        //URL 패턴에 따라 CorsConfiguration을 매핑할 수 있는 객체를 생성한다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //"/**"로 지정하여 모든 URL경로에 대해 이 CORS 정책을 적용한다.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
