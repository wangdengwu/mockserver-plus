package cn.mockserver.plus.web.view;

/**
 * @author wangdengwu
 */

public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS("成功", 200),
    /**
     * 失败
     */
    FAILURE("失败", 500);
    private String name;
    private Integer code;

    ResultCode(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
