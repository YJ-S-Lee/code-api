package code.codeapi.repository;

import code.codeapi.domain.Product;
import code.codeapi.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

    //@EntityGraph : 해당 속성을 조인처리하도록 설정한다.
    @EntityGraph(attributePaths = "imageList")  //같이 처리하고 싶은 것
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno")Long pno);

    //상품삭제, update로 JPQL처리
    @Modifying
    @Query("update Product p set p.delFlag = :flag where p.pno = :pno")
    void updateToDelete(@Param("pno")Long pno, @Param("flag")boolean flag);

    //상품목록이 나을 때 대표 이미지(0번)도 같이 나와야함.
    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> selectList(Pageable pageable);
}
