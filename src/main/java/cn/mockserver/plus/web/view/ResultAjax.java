package cn.mockserver.plus.web.view;

import lombok.Data;

/**
 * @author wangdengwu
 */
@Data
public class ResultAjax {
    private String code;
    private String message;
    private Boolean success;
    private Object data;
}
