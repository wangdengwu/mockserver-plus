package cn.mockserver.plus.web.view;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdengwu
 */
@Data
public class ApiExpectationVo {
    public static final String DONE = "done";
    public static final String DEV = "dev";
    private Long id;
    private String name;
    private Integer groupId;
    private long created;
    private int priority;
    private String apiStatus;
    private ApiHttpRequestVo httpRequest;
    private ApiHttpResponseVo httpResponse;

    public Boolean isDone() {
        if (StringUtils.equals(DONE, apiStatus)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
