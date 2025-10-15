package code.codeapi.repository.search;

import code.codeapi.domain.Product;
import code.codeapi.domain.QProduct;
import code.codeapi.domain.QProductImage;
import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.ProductDTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {
        log.info("동작하고 있어? ProductSearchImpl");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        //jpql 쿼리를 뽑는다. product로 부터 query를 뽑니다.
        JPQLQuery<Product> query = from(product);

        //상품인데 이미지가 없을 수도 있으므로 leftjoin
        query.leftJoin(product.imageList, productImage);

        //where 조건 추가, 이미지는 0번째 것만 가져오기
        query.where(productImage.ord.eq(0));

        //페이징 처리, null 포인트 입셉션이 일어날 수 있다.
        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

//        List<Product> productList = query.fetch();
        //만약 상품과 상품 이미지를 뽑고 싶으면
        List<Tuple> productList = query.select(product, productImage).fetch();
        long count = query.fetchCount();
        log.info("상품리스트1 : {}", productList);

        return null;
    }
}
