package code.codeapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {
    
    //카트번호
    private Long cino;
    //수량
    private int qty;
    //상품번호
    private Long pno;
    //상품이름
    private String pname;
    //가격
    private int price;
    //이미지
    private String imageFile;

    //생성자, 매개변수가 있는 생성자를 추가한다. 순서 기억하기
    public CartItemListDTO(Long cino, int qty, Long pno, String pname, int price, String imageFile) {
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}
