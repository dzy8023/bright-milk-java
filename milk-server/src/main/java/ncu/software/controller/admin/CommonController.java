package ncu.software.controller.admin;

import ncu.software.constant.MessageConstant;
import ncu.software.result.Result;
import ncu.software.utils.MinOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private MinOssUtil minOssUtil;
    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件：{}", file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取文件后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径
//            String filePath=aliOssUtil.upload(file.getBytes(),objectName);
            String filePath = minOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    /**
     * 文件删除
     *
     * @param imgUrl
     * @return
     */
    @DeleteMapping("/del")
    public Result<String> del(String imgUrl) {
        log.info("删除文件：{}", imgUrl);
        minOssUtil.delete(imgUrl);
        return Result.success();
    }
}
