package code.codeapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")
@Table(name = "tbl_cart", indexes = {@Index(name = "idx_cart_email", columnList = "codemember_email")})
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne
    @JoinColumn(name = "codemember_email")
    private CodeMember owner;
}
