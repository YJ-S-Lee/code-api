package code.codeapi.service;

import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.ProductDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface ProductService {
    //상품목록 가져오기
    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    //상품추가
    Long register(ProductDTO productDTO);

    //상품조회
    ProductDTO get(Long pno);

    //상품수정
    void modify(ProductDTO productDTO);

    //상품삭제
    void remove(Long pno);
}
