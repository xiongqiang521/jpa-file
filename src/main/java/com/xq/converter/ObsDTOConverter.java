package com.xq.converter;

import com.xq.obs.ObsConfigProperties;
import com.xq.obs.domain.ObsBeanDTO;
import com.xq.obs.domain.ObsUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author XQ
 * @version v1.0
 * 2020/11/1 0:22
 */
@Service
public class ObsDTOConverter {
    @Autowired
    private ObsConfigProperties properties;

    public ObsBeanDTO convertObsBean(ObsUploadDTO obsUploadDTO) {
        ObsBeanDTO bean = new ObsBeanDTO();
        bean.setName(obsUploadDTO.getName());
        bean.setInputStream(obsUploadDTO.getInputStream());
        bean.setType(makeType(obsUploadDTO.getName()));
        bean.setUrl(createPath(bean.getType()));
        bean.setBucket(properties.getBucket());
        bean.setDateTime(new Date());
        return bean;
    }


    private String createPath(String type) {
        if (type.isEmpty()) {
            return "file/" + UUID.randomUUID();
        }
        return "file/" + UUID.randomUUID() + "." + type;
    }

    private String makeType(String fileName) {
        // 不能等于0，.txt不是正常的window文件格式
        int indexOf = fileName.lastIndexOf('.');
        if (indexOf > 0) {
            return fileName.substring(indexOf + 1).toLowerCase();
        }
        return "";
    }
}
