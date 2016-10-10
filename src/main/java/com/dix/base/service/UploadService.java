package com.dix.base.service;

import com.dix.app.controller.FileController;
import com.dix.base.common.Core;
import com.dix.base.common.SpringMybatisObjectFactory;
import com.dix.base.exception.InvalidConfigurationException;
import com.dix.base.exception.WrongParamException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by dd on 10/10/2016.
 */
@Component
public class UploadService {

    private static Logger logger = LoggerFactory.getLogger(UploadService.class);

    @Value("${file.upload.root:#{null}}")
    private String fileRoot;

    @Value("${file.upload.image.root:#{null}}")
    private String imageRoot;

    @Value("${file.upload.image.mime.type:image/png,image/jpeg,image/jpg,image/gif}")
    private String allowedImageMimeType;

    @Value("${file.upload.image.size.max:1048576}")
    private Long maxImageSize;

    public void validateFile(MultipartFile file)
    {
        if (file.isEmpty())
        {
            throw new WrongParamException("文件为空");
        }

        if (!allowedImageMimeType.contains(file.getContentType()))
        {
            throw new WrongParamException("文件类型不符");
        }

        if (file.getSize() > maxImageSize)
        {
            throw new WrongParamException("文件大小超过最大限制");
        }
    }

    public void ensureImageRoot() throws IOException {
        if (fileRoot == null || imageRoot == null)
        {
            throw new InvalidConfigurationException("file.upload.root or file.upload.image.root not set");
        }

        File directory = new File(String.format("%s/%s", fileRoot, imageRoot));
        Files.createDirectories(directory.toPath());
    }

    public Path getImageFilePath(String fileName)
    {
        if (fileRoot == null || imageRoot == null)
        {
            throw new InvalidConfigurationException("file.upload.root or file.upload.image.root not set");
        }

        return Paths.get(fileRoot, imageRoot, fileName);
    }

    public String copy(MultipartFile file) throws IOException {
        String fileName = DigestUtils.md5Hex(file.getInputStream()) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        copy(file, fileName);
        return fileName;
    }

    public void copy(MultipartFile file, String toFileName) throws IOException {
        Files.copy(file.getInputStream(), getImageFilePath(toFileName), StandardCopyOption.REPLACE_EXISTING);
    }

}
