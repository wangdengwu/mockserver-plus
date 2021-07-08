package cn.mockserver.plus.web.view;

import lombok.Data;

import java.util.Map;

/**
 * @author wangdengwu
 */
@Data
public class ApiHttpResponseVo {
    private Integer statusCode;
    private String body;
}
