package cn.mockserver.plus.web.controller;

import cn.mockserver.plus.web.view.ResultAjax;
import cn.mockserver.plus.web.view.ResultCode;

/**
 * @author wangdengwu
 */
public class BaseController {

    protected ResultAjax success() {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setSuccess(true);
        resultAjax.setCode(ResultCode.SUCCESS.getCode());
        return resultAjax;
    }

    protected ResultAjax success(String successMsg) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setSuccess(true);
        resultAjax.setMessage(successMsg);
        resultAjax.setCode(ResultCode.SUCCESS.getCode());
        return resultAjax;
    }

    protected ResultAjax success(String successMsg, Object data) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setSuccess(true);
        resultAjax.setMessage(successMsg);
        resultAjax.setCode(ResultCode.SUCCESS.getCode());
        resultAjax.setData(data);
        return resultAjax;
    }

    protected ResultAjax error(String errorMsg) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setSuccess(false);
        resultAjax.setMessage(errorMsg);
        resultAjax.setCode(ResultCode.FAILURE.getCode());
        return resultAjax;
    }
}
