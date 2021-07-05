package cn.mockserver.plus.web.controller;

import cn.mockserver.plus.interceptor.MockServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author wangdengwu 2021/6/24
 */
@RestController
@RequestMapping("/_mock_server")
public class GreetingController {
    @Autowired
    private MockServerInterceptor mockServerInterceptor;

    @GetMapping("/test")
    public String ok() {
        return "ok";
    }

    @GetMapping("/ok/ok")
    public String ok_ok() {
        return "ok";
    }
}
