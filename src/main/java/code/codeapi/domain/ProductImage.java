package code.codeapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    //각각의 이미지마다 번호지정, 원하는 번호만 볼 수 있도록(대표이미지)
    @Setter
    private int ord;

    //이미지 순서


}
