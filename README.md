使用华为云的obs作为文件服务
使用springdata jpa框架操作数据库
## 创建springboot项目，添加华为云obs的SDK的maven依赖

```XML
		<!-- 开启spring配置类 -->
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- 华为云obs的SDK -->
        <dependency>
            <groupId>com.huaweicloud</groupId>
            <artifactId>esdk-obs-java</artifactId>
            <version>[3.19.7,)</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```

*版本号[3.19.7,)没有问题，表示自动选择最高版本，最低使用3.19.7版本*

## 自定义配置类，并读取配置

```yaml
# 华为云 obs地址
obs.endPoint=华为云的obs的endpoint地址
obs.ak=华为云账号的ak
obs.sk=华为云账号的sk
obs.bucket=华为云的obs创建的桶名称，建议在华为云obs控制台创建，也可以使用SDK创建
```
在pom的依赖中配置开启spring的配置类，创建配置类ObsConfigProperties 

```java
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data // lombok注解
@Component // 将此类加载到spring容器中
@ConfigurationProperties(prefix = "obs") // 与配置文件中obs的前缀对应
public class ObsConfigProperties {
    private String endPoint;
    private String ak;
    private String sk;
    private String bucket;
}

```
在后面的代码中就可以使用
@Autowired
private static ObsConfigProperties properties;
加载配置信息，也可以使用的@Value("${obs.endPoint}") 方式加载配置
## 编写文件上传下载的工具类

```java
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
 */
@Component
public class ObsUtils {
    private static ObsConfigProperties properties;
    private static ObsClient obsClient;

    /**
     * 通过输入流的方式上传文件
     * @param url 上传到OBS桶中的路径
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

```
**注意：**

 1.工具类一般为静态方法，属于class的，而spring容器是基于对象的，
如果直接写成
@Autowired
private static ObsConfigProperties properties;
static的class字段无法被注入进来
需要使用set方法注入，@Component不能丢
 2. 静态字段obsClient的初始化，也需要通过spring在创建对象时完成
@PostConstruct表示spring创建对象前执行的方法

## 对应数据库的实体类
由于上传文件时会有不同文件相同文件名的情况，上传至obs前需对文件名重命名，这里使用uuid作为文件名，将文件信息放在数据库实体类中，下载时替换文件名

```java

import javax.persistence.*;
import java.io.InputStream;
import java.util.Date;

/**
 * 文件交互时的基本类型
 *
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

```
jpa实体类中常见注解说明：https://www.cnblogs.com/jpfss/p/10895336.html

## controller层

```java

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
```
注意：文件下载时response.setHeader应在流的操作之前设置号，放在流之后，响应头信息会丢失

## dao层接口

```java
public interface FileRepository extends JpaRepository<ObsBeanDTO, Long> {

    public ObsBeanDTO findObsBeanDTOByUrl(String url);
}
```

## service层

```java

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
```
其中：ObsDTOConverter为类之间转换器，demo里比较简单，完全可以不用，代码如下：

```java

import com.xq.obs.ObsConfigProperties;
import com.xq.obs.domain.ObsBeanDTO;
import com.xq.obs.domain.ObsUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

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

```

使用的实体类，下载时需要封装的实体类，demo较简单可以去掉，在实际场景需要的字段可能较多，建议封装一下

```java
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
```

github仓库地址：https://github.com/xiongqiang521/jpa-file.git
