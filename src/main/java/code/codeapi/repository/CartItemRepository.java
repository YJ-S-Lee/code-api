package code.codeapi.repository;

import code.codeapi.domain.CartItem;
import code.codeapi.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //특정한 사용자의 이메일을 통해서 해당 사용자의 모든 장바구니 아이템들을 조회하는 기능
    @Query("select " +
            " new code.codeapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName)" +
            " from " +
            " CartItem ci inner join Cart c on ci.cart = c " +
            " left join Product p on ci.product = p" +
            " left join p.imageList pi" +
            " where " +
            " c.owner.email = :email and pi.ord = 0 " +
            " order by ci.cino desc ")  //카트 아이템에 추가된 순서가 최근 추가된 상품부터 나오게 한다.
    public List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    //사용자의 이메일과 상품 번호로 해당 장바구니 아이템을 알아내는 기능
    //이 매서드는 해당 사용자(email)의 장바구니에 특정 상품(pno)이 담겨있는지를 찾아 그 CartItem 한 건을 돌려준다.
    @Query("select ci " +
            " from CartItem ci inner join Cart c on ci.cart = c " +
            " where " +
            " c.owner.email = :email and ci.product.pno = :pno")
    public CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

    //장바구니 아이템이 속한 장바구니의 번호를 알아내는 기능
    //조건에 맞는 장바구니 번호(cno)를 Long 타입으로 반환
    @Query("select c.cno " +
            " from Cart c inner join CartItem ci on ci.cart = c " +
            " where ci.cino = :cino")
    public Long getCartFromItem(@Param("cino") Long cino);

    //특정한 장바구니의 번호만으로 해당 장바구니의 모든 장바구니 아이템들을 조회하는 기능
    @Query("select new code.codeapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName) " +
            " from " +
            //CartItem과 Cart를 내부 조인
            " CartItem ci inner join Cart c on ci.cart = c " +
            //상품 정보가 없어도 아이템은 나오도록 왼쪽 조인
            " left join Product p on ci.product = p " +
            //상품 이미지와 왼쪽 조인
            " left join p.imageList pi " +
            " where " +
            //특정 장바구니 번호만 그리고 대표 이미지(0번)만 조회
            " c.cno = :cno and pi.ord = 0 " +
            " order by ci.cino desc")
    public List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);
}
