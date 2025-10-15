package code.codeapi.controller;

import code.codeapi.dto.CartItemDTO;
import code.codeapi.dto.CartItemListDTO;
import code.codeapi.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    //장바구니 아이템의 추가/수정
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO cartItemDTO, Principal principal) {
        //현재 사용자를 찍어보자
        log.info("현재 인증된 사용자: {}", principal.getName());
        log.info("카트 아이템? {}", cartItemDTO.getEmail());
        if(!Objects.equals(principal.getName(), cartItemDTO.getEmail())) {
            throw new IllegalArgumentException("인증된 사용자와 요청 정보가 일치하지 않습니다.");
        }
        if(cartItemDTO.getQty() <= 0) {
            //해당 아이템을 삭제한다
            return cartService.remove(cartItemDTO.getCino());
        }
        return cartService.addOrModify(cartItemDTO);
    }

    //사용자 장바구니 목록
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal) {
        String email = principal.getName();
        log.info("인증된 이메일은? {}", email);

        return cartService.getCartItems(email);
    }

    //장바구니 아이템 삭제
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {
        log.info("삭제할 장바구니 아이템 번호는? {}", cino);
        return cartService.remove(cino);
    }
}
