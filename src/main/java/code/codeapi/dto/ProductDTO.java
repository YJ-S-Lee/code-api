package code.codeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long pno;

    private String pname;

    private int price;

    //설명
    private String pdesc;

    //삭제 시 표시해두기. 실제로 삭제하면 제품 통계에 문제가 발생한다.
    private boolean delFlag;

    //데이터 베이스에 실제로 보내지는 파일 데이터, 실제 업도르
    private List<MultipartFile> files = new ArrayList<>();

    //업로드가 완료된 파일의 이름만 문자열을 보관하는 리스트, 조회용
    private List<String> uploadFileNames = new ArrayList<>();
}
