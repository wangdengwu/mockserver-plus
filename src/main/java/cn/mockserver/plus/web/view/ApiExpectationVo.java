package cn.mockserver.plus.web.view;

import lombok.Data;

/**
 * @author wangdengwu
 */
@Data
public class ApiExpectationVo {
    private Long id;
    private String name;
    private Integer groupId;
    private long created;
    private int priority;
    private String apiStatus;
    private ApiHttpRequestVo httpRequest;
    private ApiHttpResponseVo httpResponse;
}
