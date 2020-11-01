package com.xq.obs.domain;

import java.io.InputStream;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/31 15:47
 */
public class ObsUploadDTO {

    private String name;
    private InputStream inputStream;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
