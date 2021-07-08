package cn.mockserver.plus.domain.mapper;


import cn.mockserver.plus.web.view.ApiHttpResponseVo;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.mockserver.model.MediaType;
import org.springframework.stereotype.Component;

/**
 * @author wangdengwu
 */
@Component
public class ApiHttpResponseVoMapper {
    public HttpResponse toHttpResponse(ApiHttpResponseVo apiHttpResponseVo) {
        HttpResponse httpResponse = HttpResponse.response();
        Integer statusCode = apiHttpResponseVo.getStatusCode();
        httpResponse.withStatusCode(statusCode);
        String body = apiHttpResponseVo.getBody();
        JsonBody jsonBody = JsonBody.json(body, MediaType.APPLICATION_JSON_UTF_8);
        httpResponse.withBody(jsonBody);
        return httpResponse;
    }
}
