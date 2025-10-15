package code.codeapi.dto;

import lombok.Data;

@Data
public class CartItemDTO {

    private String email;
    //상품번호
    private Long pno;
    //수량
    private int qty;
    //장바구니 아이템 번호
    private Long cino;
}
