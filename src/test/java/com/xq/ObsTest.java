package com.xq;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.xq.obs.ObsUtils;

import java.io.*;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/31 16:34
 */
public class ObsTest {
    private static String endPoint="obs.cn-north-4.myhuaweicloud.com";
    private static String ak="CZPS4UAZI4SFSVMD3W3N";
    private static String sk="jsUONU4OP3jUj7EuuJIHQ0vDLcTYdLPlo9Prq2dW";
    private static String bucket="test-xq521";

    public static void main(String[] args) throws Exception {

        ObsClient obsClient=new ObsClient(ak,sk, endPoint);

        File file = new File("C:\\Users\\XQ\\OneDrive\\桌面\\test.txt");

        PutObjectResult putObjectResult = obsClient.putObject(bucket, "test.txt" ,new FileInputStream(file));

        System.out.println(putObjectResult);

        System.out.println("===========================");

        ObsObject obsObject = obsClient.getObject(bucket, "test.txt");

        System.out.println(obsClient);

        // 读取对象内容
        System.out.println("Object content:");
        InputStream input = obsObject.getObjectContent();
        byte[] b = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        while ((len=input.read(b)) != -1){
            bos.write(b, 0, len);
        }

        System.out.println(new String(bos.toByteArray()));
        bos.close();
        input.close();

    }
}
