package com.xq.obs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/30 22:38
 */
@Data
@Component
@ConfigurationProperties(prefix = "obs")
public class ObsConfigProperties {
    private String endPoint;
    private String ak;
    private String sk;
    private String bucket;
}
