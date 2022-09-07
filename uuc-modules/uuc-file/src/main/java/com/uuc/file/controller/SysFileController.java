package com.uuc.file.controller;

import com.uuc.common.core.domain.R;
import com.uuc.common.core.utils.file.FileUtils;
import com.uuc.file.service.Base64SysFileServiceImpl;
import com.uuc.file.service.ISysFileService;
import com.uuc.system.api.domain.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件请求处理
 * 
 * @author uuc
 */
@RestController
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Resource(name = "${upload.type}")
    private ISysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            if (sysFileService instanceof Base64SysFileServiceImpl) {
                sysFile.setName(file.getOriginalFilename());
            } else {
                sysFile.setName(FileUtils.getName(url));
            }
            sysFile.setUrl(url);
            return R.ok(sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

}