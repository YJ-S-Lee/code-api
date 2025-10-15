package code.codeapi.repository;

import code.codeapi.domain.Product;
import code.codeapi.dto.PageRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;
    
    //상품 + 이미지 2개 저장해보기
    @Test
    public void 상품추가() {
        Product product = Product.builder()
                .pname("test상품")
                .price(20000)
                .pdesc("25가을신상")
                .build();
        //이미지 2개 추가, 이름추가로 테스트
        product.addImageString(UUID.randomUUID().toString() + "_" + "image1.png");
        product.addImageString(UUID.randomUUID().toString() + "_" + "image2.png");
        product.addImageString(UUID.randomUUID().toString() + "_" + "image3.png");

        productRepository.save(product);
    }

    @Transactional
    @Test
    public void 상품조회() {
        Long pno = 1L;
        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info("결과 : {}", product.getPname());
        log.info("이미지 결과: {}", product.getImageList());
    }

    @Transactional
    @Test
    public void 상품조회2() {
        Long pno = 1L;
        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info("결과2 : {}", product.getPname());
        log.info("이미지 결과2: {}", product.getImageList());

    }

    @Transactional
    @Commit
    @Test
    public void 상품삭제() {
        Long pno = 1L;

        productRepository.updateToDelete(pno, true);
    }

    @Test
    public void 상품추가열개() {
        for(int i=1; i<=10; i++) {
            Product product = Product.builder()
                    .pname("test상품" + i)
                    .price(20000)
                    .pdesc("상품설명" + i)
                    .build();

            //이미지 2개 추가, 이름추가로 테스트
            product.addImageString(UUID.randomUUID().toString() + "_" + "image1.png");
            product.addImageString(UUID.randomUUID().toString() + "_" + "image2.png");

            productRepository.save(product);

            log.info("저장완료 : {}", product.getPname());
        }
    }

    @Test
    public void 수정8번상품() {
        Long pno = 8L;
        Product product = productRepository.selectOne(pno).get();

        product.setPname("수정8번");
        product.setPdesc("수정설명8번");
        product.setPrice(10000);

        //이미지는 지우고 다시 추가한다.
        product.claerList();
        product.addImageString(UUID.randomUUID().toString() + "_" + "image1.png");
        product.addImageString(UUID.randomUUID().toString() + "_" + "image2.png");
        product.addImageString(UUID.randomUUID().toString() + "_" + "image3.png");

        productRepository.save(product);
    }

    @Test
    public void 상품리스트조회() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Test
    public void 상품검색() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        productRepository.searchList(pageRequestDTO);
    }
}