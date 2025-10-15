package code.codeapi.service;

import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductServiceTest {

    @Autowired
    ProductService productService;

    //상품조회
    @Test
    public void 상품조회() {
        //기본 1페이지, 10개
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info("상품리스트 : {}", dto));
    }

    //상품저장
    @Test
    public void 상품저장() {
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운상품 추가")
                .pdesc("25년 겨울 신상")
                .price(150000)
                .build();

        //uuid 필요
        productDTO.setUploadFileNames(
                List.of(
                        UUID.randomUUID() + "_" + "new1.png",
                        UUID.randomUUID() + "_" + "new2.png"
                        )
        );
        productService.register(productDTO);
    }

    //상품 가져오기
    @Test
    public void 상품1번가져오기() {
        Long pno = 1L;

        ProductDTO productDTO = productService.get(pno);

        log.info("찾은 상품 {}", productDTO);
        log.info("찾은 상품 {}", productDTO.getUploadFileNames());
    }
}