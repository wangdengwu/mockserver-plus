package cn.mockserver.plus.config;

/*
 * @author wangdengwu 2021/6/24
 */

import cn.mockserver.plus.interceptor.MockServerInterceptor;
import org.mockserver.configuration.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author wangdengwu
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Value("${mockserver.api.server}")
    private String apiServer;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/_mock_server/**").allowedOriginPatterns("*").allowCredentials(true).allowedMethods(ORIGINS).maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ConfigurationProperties.enableCORSForAllResponses(true);
        registry.addInterceptor(mockServerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        Arrays.asList(
                                "/",
                                "/_mock_server/**",
                                "/error",
                                "/favicon.ico",
                                "/*.html",
                                "/**/*.js",
                                "/**/*.ttf",
                                "/**/*.woff",
                                "/**/*.svg",
                                "/**/*.map",
                                "/**/*.css",
                                "/**/*.png",
                                "/**/*.jpg",
                                "/**/*.jpeg"));
    }

    @Bean
    public MockServerInterceptor mockServerInterceptor() {
        return new MockServerInterceptor(apiServer);
    }
}
