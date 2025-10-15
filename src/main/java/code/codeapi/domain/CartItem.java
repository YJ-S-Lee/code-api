package code.codeapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = {"cart", "product"})    //연관관계는 다 빼는 게 좋다.
@Table(name = "tbl_cart_item", indexes = {
        @Index(columnList = "cart_cno", name = "idx_cartitem_cart"),
        @Index(columnList = "product_pno, cart_cno", name = "idx_cartitem_pno_cart")
})
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cino;

    @ManyToOne
    @JoinColumn(name = "product_pno")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_cno")
    private Cart cart;

    private int qty;    //수량

    //수량은 수정할 수 있도록한다.
    public void setQty(int qty) {
        this.qty = qty;
    }
}
