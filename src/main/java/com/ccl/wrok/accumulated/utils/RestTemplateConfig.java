package com.ccl.wrok.accumulated.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

/**
 * @author 23077
 */
@Configuration
public class RestTemplateConfig {

    @Value("${fpi.ignore-token:true}")
    private String ignoreToken;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        Duration duration = Duration.ofSeconds(5);
        RestTemplate restTemplate = builder.setConnectTimeout(duration).setReadTimeout(duration).build();
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            //调试阶段跳过网关
            headers.add("ignore-token", ignoreToken);
            //加入用户信息，便于权限过滤
            //CurrentUserUtil.putPermissionHeaders(headers);
            return execution.execute(request, body);
        }));
        return restTemplate;
    }
}
