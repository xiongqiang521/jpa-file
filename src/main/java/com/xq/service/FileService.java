package com.xq.service;

import com.xq.converter.ObsDTOConverter;
import com.xq.obs.ObsUtils;
import com.xq.obs.domain.ObsBeanDTO;
import com.xq.obs.domain.ObsUploadDTO;
import com.xq.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/31 23:48
 */
@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    @Autowired
    private ObsDTOConverter obsDTOConverter;

    @Autowired
    private FileRepository fileRepository;


    public ObsBeanDTO uploadFile(ObsUploadDTO obsUploadDTO) {
        // 1、创建对应的数据库实体类
        ObsBeanDTO obsBeanDTO = obsDTOConverter.convertObsBean(obsUploadDTO);

        // 2、上传至华为云obs
        ObsUtils.uploadFile(obsBeanDTO.getUrl(), obsBeanDTO.getInputStream());

        logger.info("obsBeanDTO -> {}", obsBeanDTO);

        // 3、保存进数据库
        ObsBeanDTO save = fileRepository.save(obsBeanDTO);
        return save;
    }

    public ObsBeanDTO downFile(String url) throws IOException {
        // 1、数据库查询对应的实体类
        ObsBeanDTO obsBeanDTO = fileRepository.findObsBeanDTOByUrl(url);
        // 2、从华为云获取文件流
        InputStream inputStream = ObsUtils.downFile(url);
        // 3、封装bean，并返回
        obsBeanDTO.setInputStream(inputStream);
        return obsBeanDTO;
    }
}
