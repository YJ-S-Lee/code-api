package code.codeapi.repository.search;

import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);
}
