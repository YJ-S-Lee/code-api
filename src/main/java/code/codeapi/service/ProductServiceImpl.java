package code.codeapi.service;

import code.codeapi.domain.Product;
import code.codeapi.domain.ProductImage;
import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.ProductDTO;
import code.codeapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    //상품목록
    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        //페이지 목록 만들기
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() -1,    //페이지 시작 번호
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        //리포지토리에서 목록을 가져온다(상품의 대표 이미지 1개를 가져온다)
        Page<Object[]> result = productRepository.selectList(pageable);

        //0번째는 product이고 1번째는 productImage이다.
        List<ProductDTO> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];
            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();
            String imageFileName = productImage.getFileName();  //대표 이미지 정보
            productDTO.setUploadFileNames(List.of(imageFileName));  //대표 이미지를 DTO에 포함시킴

            return productDTO;
        }).toList();

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    //상품추가
    @Override
    public Long register(ProductDTO productDTO) {

        //추가하는 기능을 만든다.
        Product product = dtoToEntity(productDTO);

        //저장하기 전에 확인해본다.
        log.info("새로운 상품 : {}", product);
        log.info("새로운 상품 이미지 이름 : {}", product.getImageList());

        Long pno = productRepository.save(product).getPno();
        return pno;
    }

    //상품조회
    @Override
    public ProductDTO get(Long pno) {
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDTO productDTO = entityToDto(product);
        return productDTO;
    }

    //상품수정
    @Override
    public void modify(ProductDTO productDTO) {
        //1개의 상품 찾아오기
        Optional<Product> result = productRepository.selectOne(productDTO.getPno());

        Product product = result.orElseThrow();
        //새로 들어온 내용으로 수정한다.
        product.setPname(productDTO.getPname());
        product.setPdesc(productDTO.getPdesc());
        product.setPrice(productDTO.getPrice());
        product.setDelFlag(productDTO.isDelFlag());

        //무조건 다 지운다
        product.claerList();

        //입력된 이미지 파일 이름을 가져온다
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }
        productRepository.save(product);
    }

    //상품삭제
    @Override
    public void remove(Long pno) {
//        productRepository.updateToDelete(pno, true);
        productRepository.deleteById(pno);
    }

    //dto를 받아서 entity로 변환하는 코드
    private Product dtoToEntity(ProductDTO productDTO) {
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        //업로드 처리가 끝난 파일들의 이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }
        uploadFileNames.forEach(uploadName -> {
            product.addImageString(uploadName);
        });
        return product;
    }

    //entity를 받아서 dto로 변환하는 코드
    private ProductDTO entityToDto(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFileName()).toList();
        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
    }
}
