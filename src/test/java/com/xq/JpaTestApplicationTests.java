package com.xq;

import com.xq.controller.IndexController;
import com.xq.domain.ManufacturerDTO;
import com.xq.domain.ProductDTO;
import com.xq.domain.ServiceDTO;
import com.xq.obs.ObsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
class JpaTestApplicationTests {
    @Autowired
    IndexController indexController;

    @Test
    void contextLoads() throws IOException {
        // File file = new File("C:\\Users\\XQ\\OneDrive\\桌面\\test.txt");
        //
        // String s = ObsUtils.uploadFile("test/test.txt",new FileInputStream(file));

        InputStream inputStream = ObsUtils.downFile("file/2a3cda36-9432-4f59-bbb4-9e074e794714.http");

        System.out.println(inputStream.available());
        // 读取对象内容
        System.out.println("Object content:");
        InputStream input = inputStream;
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
