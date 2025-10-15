package code.codeapi.service;

import code.codeapi.domain.Cart;
import code.codeapi.domain.CartItem;
import code.codeapi.domain.CodeMember;
import code.codeapi.domain.Product;
import code.codeapi.dto.CartItemDTO;
import code.codeapi.dto.CartItemListDTO;
import code.codeapi.repository.CartItemRepository;
import code.codeapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    //장바구니 아이템 추가 혹은 변경
    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
        //사용자 이메일
        String email = cartItemDTO.getEmail();
        //상품번호
        Long pno = cartItemDTO.getPno();
        //수량
        int qty = cartItemDTO.getQty();
        //장바구니 아이템 번호
        Long cino = cartItemDTO.getCino();

        //장바구니 아이템 번호가 있어서 수량만 변경하는 경우
        if(cino != null) {
            //장바구니 아이템을 가져온다
            Optional<CartItem> cartResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartResult.orElseThrow();
            cartItem.setQty(qty);
            cartItemRepository.save(cartItem);
            return getCartItems(email);
        }
        //장바구니 아이템 번호가 없는 경우
        //사용자의 카트(장바구니) 만들기
        Cart cart = getCart(email);
        CartItem cartItem = null;

        //이미 동일한  상품이 담긴 적이 있을 수 있으므로 일단 가져온다.
        cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem == null) {
            //장바구니 카트에 해당 상품이 없다는 뜻. 상품을 찾아서 카트 아이템에 잠음
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        } else  {
            //동일한  상품이 카트 아이템 리스트에 있다는 것. 수량만 변경한다
            cartItem.setQty(qty);
        }
        //상품 아이템 저장
        cartItemRepository.save(cartItem);
        return getCartItems(email);
    }

    private Cart getCart(String email) {
        Cart cart = null;
        //사용자 가져오기
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        if(result.isEmpty()) {
            CodeMember member = CodeMember.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }
        return cart;
    }

    //모든 장바구니 아이템 목록
    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    //아이템 삭제
    @Override
    public List<CartItemListDTO> remove(Long cino) {
        //카트 아이템 번호로 카트(장바구니)번호를 가져온다.
        Long cno = cartItemRepository.getCartFromItem(cino);
        //카트 아이템 삭제
        cartItemRepository.deleteById(cino);
        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }
}
