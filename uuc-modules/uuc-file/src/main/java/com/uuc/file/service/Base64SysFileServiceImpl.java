package com.uuc.file.service;

import com.uuc.common.core.exception.file.FileNameLengthLimitExceededException;
import com.uuc.common.core.utils.file.MimeTypeUtils;
import com.uuc.file.utils.FileUploadUtils;
import com.uuc.system.api.RemoteSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author: fxm
 * @date: 2022-05-30
 * @description: 文件上传保存base64
 **/
@Service("base64")
public class Base64SysFileServiceImpl implements ISysFileService {
    @Autowired
    private RemoteSystemService remoteSystemService;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        // 文件校验
        int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        FileUploadUtils.assertAllowed(file, MimeTypeUtils.INDEX_IMAGE_ALLOWED_EXTENSION);
        String base64 = FileUploadUtils.caseFile2Base64(file);

//        // 更新配置项及缓存
//        SysConfig config = new SysConfig();
//        config.setConfigName("主框架页-系统LOGO");
//        config.setConfigKey(VerifyConstants.VERIFY_INDEX_IMAGE);
//        config.setConfigValue(base64);
//        AjaxResult result = remoteSystemService.updateConfigByKey(config, SecurityConstants.INNER);
//        if (HttpStatus.SUCCESS != (int) result.get("code")) {
//            throw new ServiceException("更新配置项失败！  cause : " + result.get("msg"));
//        }

        return base64;
    }
}
