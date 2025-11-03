package code.codeapi.controller;

import code.codeapi.dto.PageRequestDTO;
import code.codeapi.dto.PageResponseDTO;
import code.codeapi.dto.ProductDTO;
import code.codeapi.service.ProductService;
import code.codeapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;
    
    //파일 업로드
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {
        log.info("register {}", productDTO);

        List<MultipartFile> files = productDTO.getFiles();
        
        //업로드 로직 가져오기
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        
        //저장
        productDTO.setUploadFileNames(uploadFileNames);
        
        log.info("productDTO.setUploadFileNames? {}", uploadFileNames);

        //서비스 호출
        Long pno = productService.register(productDTO);
        
        return Map.of("result", pno);
    }

    //업로드 파일 조회
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    //상품목록 조회
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")  //임시로 권한 설정
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    //하나의 상품 조회
    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno")Long pno) {
        return productService.get(pno);
    }

    //상품수정
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno")Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);

        ProductDTO oldProductDTO = productService.get(pno);

        //기존파일(데이터베이스에 있던 파일)
        List<String > oldFileNames = oldProductDTO.getUploadFileNames();

        //새로 업로드 해야 하는 파일
        List<MultipartFile> files = productDTO.getFiles();

        //새로 업로드 해야 하는 파일의 파일이름
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //화면에 변화없이 계속 유지된 파일, 수정하지 않고 그냥 있을 파일
        //파일이 3개 있었는데, 하나는 삭제, 두개는 남아 있는 상태, 여기서는 두개를 말함
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        //만약 업로드된 파일이 있으면
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
            //유지되는 파일들(uploadedFileNames) + 새로 업로드된 파일(currentUploadFileNames) => 파일목록을 만든다.
        }

        //수정한다
        productService.modify(productDTO);

        //c를 삭제한다
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            //지워야 하는 파일목록 찾기, 올드목록에서 업로드파일 이름에 없는 것을 모은다
            List<String> removeFiles = oldFileNames.stream().filter(
                    fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            //실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");
    }

    //상품삭제
    @DeleteMapping("{pno}")
    public Map<String, String> remove(@PathVariable("pno")Long pno) {
        //삭제할 파일 알아내기
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "DELETE SUCCESS");
    }
}
