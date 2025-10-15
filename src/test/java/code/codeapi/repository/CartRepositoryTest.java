package code.codeapi.repository;

import code.codeapi.domain.Cart;
import code.codeapi.domain.CartItem;
import code.codeapi.domain.CodeMember;
import code.codeapi.domain.Product;
import code.codeapi.dto.CartItemListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CartRepositoryTest {

    @Autowired CartRepository cartRepository;
    @Autowired CartItemRepository cartItemRepository;

    @Test
    public void 맴버Cart가져오기() {
        String email = "user1@aaa.com";

        cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    //test가 서버에 반영되었으면 좋겠다라면 @Transactional, @Commit 사용
    @Test
    @Transactional
    @Commit
    public void 장바구니만들기() {
        //사용자가 전송하는 정보
        String email = "user1@aaa.com";
        Long pno = 43L;
        int qty = 1;

        //만일 기존에 사용자의 장바구니 아이템이 있다면
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if(cartItem != null) {
            cartItem.setQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        //장바구니 아이템이 없다면 장바구니부터 확인 필요
        //사용자가 장바구니를 만든 적이 있는지 확인
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        //cart변수를 밖으로 뺀 것
        //카트를 새로 만드는 경우와 카트가 있는 경우를 가져와야 하기 때문
        Cart cart = null;

        if(result.isEmpty()) {
            //카트를 만든다 => 맴버를 만든다 => 새로운 카트를 만든다
            log.info("카트 만든 적 없음");
            CodeMember member = CodeMember.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }
        log.info("카트는 : {}", cart);

        //상품을 만들어 넣어주자
        Product product = Product.builder().pno(pno).build();
        cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();

        //상품 아이템 저장
        cartItemRepository.save(cartItem);
    }

    //장바구니 아이템 수량 수정
    @Test
    @Commit
    public void 장바구니아이템수량수정() {
        Long cino = 1L;
        int qty = 4;

        Optional<CartItem> result = cartItemRepository.findById(cino);
        CartItem cartItem = result.orElseThrow();
        cartItem.setQty(qty);
        cartItemRepository.save(cartItem);
    }

    //햔재 사용자의 장바구니 아이템 목록테스트
    @Test
    public void 장바구니아이템목록보기() {
        String email = "user1@aaa.com";
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for(CartItemListDTO dto : cartItemList) {
            log.info("장바구니 아이템 목록 {}", dto);
        }
    }

    //장바구니 아이템 삭제와 목록 조회
    @Test
    public void 장바구니아이템삭제후장바구니조회() {
        //장바구니에 1번, 2번, 3번 상품이 존재 => 1번 삭제 => 장바구니 조회 2번, 3번이 있어야함
        //장바구니에 있는 아이템 번호
        Long cino = 1L;

        //장바구니 아이템 찾기
        Long cno = cartItemRepository.getCartFromItem(cino);

        //삭제, 테스트코드 실행 시 주석 처리한다.
        cartItemRepository.deleteById(cno);

        //목록
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);

        for(CartItemListDTO dto : cartItemList) {
            log.info("장바구니 아이템 목록 {}", dto);
        }
    }
}