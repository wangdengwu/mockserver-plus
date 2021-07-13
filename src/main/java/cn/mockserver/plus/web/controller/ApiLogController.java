package cn.mockserver.plus.web.controller;

import cn.mockserver.plus.interceptor.MockServerInterceptor;
import cn.mockserver.plus.web.view.ResultAjax;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.model.HttpRequest;
import org.mockserver.serialization.RequestDefinitionSerializer;
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
    public ResultAjax query(@RequestParam(required = false) String path) {
        HttpRequest httpRequest = HttpRequest.request(path);
        httpRequest.withQueryStringParameter("type", "REQUEST_RESPONSES");
        if (StringUtils.isNotBlank(path)) {
            httpRequest.withBody(new RequestDefinitionSerializer(null).serialize(HttpRequest.request(path)));
        }
        return success("ok", mockServerInterceptor.retrieve(httpRequest));
    }
}
