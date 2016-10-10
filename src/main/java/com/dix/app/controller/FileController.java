package com.dix.app.controller;

import com.dix.base.common.DataResponse;
import com.dix.base.exception.BaseException;
import com.dix.base.exception.WrongParamException;
import com.dix.base.service.UploadService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

@RestController("FileController")
@RequestMapping("/file")
@Component
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private UploadService uploadService;

    @RequestMapping("/img/upload")
    public DataResponse handleFileUpload(@RequestParam("file") MultipartFile file)
    {
        uploadService.validateFile(file);

        DataResponse response = DataResponse.create();
        try
        {
            uploadService.ensureImageRoot();
            String fileName = uploadService.copy(file);

            HashMap<String, Object> fileDataMap = new HashMap<>();
            fileDataMap.put("name", fileName);
            response.put("file", fileDataMap);

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            throw new BaseException(-1, "文件上传失败 ");
        }

        return response;
    }

}
