package com.xq.obs;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * 以uuid作为存放在华为云的文件名
 *
 * @author XQ
 * @version v1.0
 * 2020/10/31 0:02
 */
@Component
public class ObsUtils {
    private static ObsConfigProperties properties;
    private static ObsClient obsClient;

    private ObsUtils(){}

    /**
     * 通过输入流的方式上传文件
     * @param url
     * @param inputStream
     * @return
     */
    public static String uploadFile(String url, InputStream inputStream) {
        PutObjectResult putObjectResult = obsClient.putObject(properties.getBucket(), url, inputStream);
        int statusCode = putObjectResult.getStatusCode();
        if (statusCode != 200) {
            throw new RuntimeException("文件上传异常，http响应码为" + statusCode);
        }
        return url;
    }

    /**
     * 根据文件的路径获取文件的输入流
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream downFile(String url) throws IOException {
        ObsObject obsObject = obsClient.getObject(properties.getBucket(), url);

        Long contentLength = obsObject.getMetadata().getContentLength();
        return obsObject.getObjectContent();
    }

    @PostConstruct
    public void init() {
        obsClient = new ObsClient(properties.getAk(), properties.getSk(), properties.getEndPoint());
    }

    @Autowired
    public void setProperties(ObsConfigProperties obsConfigProperties) {
        properties = obsConfigProperties;
    }


}
