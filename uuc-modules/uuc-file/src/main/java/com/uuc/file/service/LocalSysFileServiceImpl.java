package com.uuc.file.service;

import com.uuc.file.config.LocalConfig;
import com.uuc.file.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储
 * 
 * @author uuc
 */
@Service("file")
public class LocalSysFileServiceImpl implements ISysFileService
{
//    /**
//     * 资源映射路径 前缀
//     */
//    @Value("${file.prefix}")
//    public String localFilePrefix;
//
//    /**
//     * 域名或本机访问地址
//     */
//    @Value("${file.domain}")
//    public String domain;
//
//    /**
//     * 上传文件存储在本地的根路径
//     */
//    @Value("${file.path}")
//    private String localFilePath;

    @Autowired(required = false)
    private LocalConfig localConfig;

    /**
     * 本地文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception
    {
        String name = FileUploadUtils.upload(localConfig.getPath(), file);
        String url = localConfig.getDomain() + localConfig.getPrefix() + name;
        return url;
    }
}
