package code.codeapi.config;

import code.codeapi.controller.formatter.LocalDateFomatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        log.info("addFormatters 작동");
        registry.addFormatter(new LocalDateFomatter());
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  //모든 경로에 설정
//                .maxAge(500)    //연결이 안될 때, 서버에 문제가 있다고 생각하여 끊어준다.
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")  //어떤 방식의 호출을 허용할 건지, options는 되는지 한 번 해보는 것
//                .allowedOrigins("*");   //모든 경로에서 들어오는 것 허용
//    }
}
