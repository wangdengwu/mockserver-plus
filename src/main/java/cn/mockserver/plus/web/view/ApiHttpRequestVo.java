package cn.mockserver.plus.web.view;

import lombok.Data;

import java.util.Map;

/**
 * @author wangdengwu
 */
@Data
public class ApiHttpRequestVo {
    private String method;
    private String path;
    private String body;
    private String bodyType;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
}
