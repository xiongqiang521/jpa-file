package com.xq.obs.domain;

import javax.persistence.*;
import java.io.InputStream;
import java.util.Date;

/**
 * 文件交互时的基本类型
 *
 * @author XQ
 * @version v1.0
 * 2020/10/31 0:18
 */
@Entity
@Table(name = "t_file", indexes = @Index(name = "file_url_index", columnList = "url", unique = true))
public class ObsBeanDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户上传时的文件名
     */
    private String name;
    /**
     * 文件类型，根据文件名获取
     */
    private String type;
    /**
     * 上传后obs的位置，包含上传后的文件名
     */
    // @Column(name = "url", unique = true)
    private String url;
    /**
     * 华为云obs桶名
     */
    private String bucket;

    /**
     * 华为云obs桶名
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    /**
     * 文件的输入流
     */
    @Transient
    private InputStream inputStream;

    @Override
    public String toString() {
        return "ObsBeanDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", bucket='" + bucket + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
