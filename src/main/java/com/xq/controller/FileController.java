package com.xq.controller;

import com.xq.obs.domain.ObsBeanDTO;
import com.xq.obs.domain.ObsUploadDTO;
import com.xq.service.FileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/31 23:28
 */
@Controller
public class FileController {
    private static final Logger logger = getLogger(FileController.class);
    @Autowired
    FileService fileService;

    @PostMapping("/file")
    @ResponseBody
    public ObsBeanDTO uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("upload file,file name -> {}, file.getSize -> {}", file.getOriginalFilename(), file.getSize());
        ObsUploadDTO obsUploadDTO = new ObsUploadDTO();
        obsUploadDTO.setName(file.getOriginalFilename());
        obsUploadDTO.setInputStream(file.getInputStream());
        ObsBeanDTO obsBeanDTO = fileService.uploadFile(obsUploadDTO);
        // 清理流，并将实体类的流设为空
        InputStream inputStream = obsBeanDTO.getInputStream();
        inputStream.close();
        obsBeanDTO.setInputStream(null);

        logger.info("uploadFile is success, obsBeanDTO -> {}", obsBeanDTO);
        return obsBeanDTO;
    }

    @GetMapping("/file")
    public void downFile(String url, HttpServletResponse response) throws IOException {
        ObsBeanDTO obsBeanDTO = fileService.downFile(url);
        InputStream inputStream = obsBeanDTO.getInputStream();
        byte[] b = new byte[1024];

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + obsBeanDTO.getName());

        ServletOutputStream outputStream = response.getOutputStream();
        int len;
        while ((len=inputStream.read(b)) != -1){
            outputStream.write(b, 0, len);
            outputStream.flush();
        }
        inputStream.close();
        obsBeanDTO.setInputStream(null);
        logger.info("downFile success, obsBeanDTO -> {}", obsBeanDTO);
    }


}
