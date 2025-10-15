package code.codeapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "imageList")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Setter
    private String pname;

    @Setter
    private int price;

    @Setter
    private String pdesc;

    @Setter
    private boolean delFlag;

    @ElementCollection
    @Builder.Default    //빈 배열을 유지하기 위함. 빌더는 필드의 초기값을 무시하지만 뒤에 디폴트를 주면 특정 필드에 초기값을 유지한다.
    private List<ProductImage> imageList = new ArrayList<>();

    //상품에 이미지 추가
    public void addImage(ProductImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    //파일 이름을 기반으로 상품에 이미지를 추가
    //이미지 파일의 이름을 문자열(fileName)로 전달 => 추가한 이미지를 imageList 배열에 저장
    public void addImageString(String fileName) {
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);
    }

    //상품삭제
    public void claerList() {
        this.imageList.clear();
    }
}
