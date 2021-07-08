package cn.mockserver.plus.domain.mapper;

import cn.mockserver.plus.domain.entity.ApiExpectation;
import cn.mockserver.plus.domain.entity.ApiHeader;
import cn.mockserver.plus.domain.entity.ApiQueryStringParameter;
import cn.mockserver.plus.web.view.ApiHttpRequestVo;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.model.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author wangdengwu
 */
@Component
@Slf4j
public class ApiHttpRequestVoMapper {

    public HttpRequest toHttpRequest(ApiHttpRequestVo apiHttpRequestVo) {
        String method = apiHttpRequestVo.getMethod();
        String path = apiHttpRequestVo.getPath();
        HttpRequest httpRequest = HttpRequest.request(path).withMethod(method);
        Map<String, String> headers = apiHttpRequestVo.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpRequest::withHeader);
        }
        Map<String, String> queryStringParameters = apiHttpRequestVo.getQueryStringParameters();
        if (queryStringParameters != null && !queryStringParameters.isEmpty()) {
            queryStringParameters.forEach(httpRequest::withQueryStringParameter);
        }
        String bodyType = apiHttpRequestVo.getBodyType();
        String body = apiHttpRequestVo.getBody();
        MediaType mediaType = MediaType.parse(bodyType);
        if (mediaType.isCompatible(MediaType.APPLICATION_JSON_UTF_8)) {
            JsonBody jsonBody = JsonBody.json(body, mediaType);
            httpRequest.withBody(jsonBody);
        }
        if (mediaType.isCompatible(MediaType.APPLICATION_FORM_URLENCODED)) {
            Parameters parameters = retrieveFormParameters(body);
            ParameterBody parameterBody = ParameterBody.params(parameters);
            httpRequest.withBody(parameterBody);
        }
        return httpRequest;
    }

    public ApiHttpRequestVo toApiHttpRequestVo(ApiExpectation apiExpectation, List<ApiHeader> apiHeaderList, List<ApiQueryStringParameter> apiQueryStringParameterList) {
        ApiHttpRequestVo apiHttpRequestVo = new ApiHttpRequestVo();
        apiHttpRequestVo.setBodyType(apiExpectation.getRequestBodyType());
        apiHttpRequestVo.setBody(apiExpectation.getRequestBody());
        apiHttpRequestVo.setMethod(apiExpectation.getMethod());
        apiHttpRequestVo.setPath(apiExpectation.getPath());
        if (!apiHeaderList.isEmpty()) {
            Map<String, String> headers = new HashMap<>();
            apiHeaderList.stream().forEach(apiHeader -> {
                String name = apiHeader.getName();
                String value = apiHeader.getValue();
                headers.put(name, value);
            });
            apiHttpRequestVo.setHeaders(headers);
        }
        if (!apiQueryStringParameterList.isEmpty()) {
            Map<String, String> parameters = new HashMap<>();
            apiQueryStringParameterList.stream().forEach(apiQueryStringParameter -> {
                String name = apiQueryStringParameter.getName();
                String value = apiQueryStringParameter.getValue();
                parameters.put(name, value);
            });
            apiHttpRequestVo.setQueryStringParameters(parameters);
        }
        return apiHttpRequestVo;
    }

    private Parameters retrieveFormParameters(String parameterString) {
        Parameters parameters = new Parameters();
        Map<String, List<String>> parameterMap = new HashMap<>(8);
        if (isNotBlank(parameterString)) {
            try {
                parameterMap.putAll(new QueryStringDecoder(parameterString, false).parameters());
            } catch (IllegalArgumentException iae) {
                log.error("retrieveFormParameters error:{}", iae.getMessage());
            }
        }
        return parameters.withEntries(parameterMap);
    }
}
