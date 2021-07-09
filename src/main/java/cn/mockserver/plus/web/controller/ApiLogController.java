package cn.mockserver.plus.web.controller;

import cn.mockserver.plus.interceptor.MockServerInterceptor;
import cn.mockserver.plus.web.view.ResultAjax;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangdengwu
 */
@RestController
@RequestMapping("/_mock_server/log")
public class ApiLogController extends BaseController {
    @Autowired
    private MockServerInterceptor mockServerInterceptor;

    @PostMapping("/query")
    public ResultAjax query(@RequestParam String path) {
        HttpRequest httpRequest = HttpRequest.request(path);
        return success("ok", mockServerInterceptor.retrieve(httpRequest));
    }
}
