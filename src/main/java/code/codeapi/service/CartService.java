package code.codeapi.service;

import code.codeapi.dto.CartItemDTO;
import code.codeapi.dto.CartItemListDTO;

import java.util.List;

public interface CartService {

    //장바구니 아이템 추가 혹은 변경
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    //모든 장바구니 아이템 목록
    public List<CartItemListDTO> getCartItems(String email);

    //아이템 삭제
    public List<CartItemListDTO> remove(Long cino);
}
