package cn.mockserver.plus.domain.mapper;


import cn.mockserver.plus.domain.entity.ApiExpectation;
import cn.mockserver.plus.domain.entity.ApiHeader;
import cn.mockserver.plus.domain.entity.ApiQueryStringParameter;
import cn.mockserver.plus.web.view.ApiExpectationVo;
import cn.mockserver.plus.web.view.ApiHttpRequestVo;
import cn.mockserver.plus.web.view.ApiHttpResponseVo;
import org.mockserver.mock.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangdengwu
 */

@Component
public class ApiExpectationVoMapper {
    @Autowired
    private ApiHttpRequestVoMapper apiHttpRequestVoMapper;
    @Autowired
    private ApiHttpResponseVoMapper apiHttpResponseVoMapper;

    public Expectation toExpectation(ApiExpectationVo apiExpectationVo) {
        ApiHttpRequestVo httpRequest = apiExpectationVo.getHttpRequest();
        ApiHttpResponseVo httpResponse = apiExpectationVo.getHttpResponse();
        Expectation expectation = Expectation.when(apiHttpRequestVoMapper.toHttpRequest(httpRequest), apiExpectationVo.getPriority())
                .withId(apiExpectationVo.getId().toString())
                .withCreated(apiExpectationVo.getCreated()).thenRespond(apiHttpResponseVoMapper.toHttpResponse(httpResponse));
        return expectation;
    }

    public ApiExpectation toApiExpectation(ApiExpectationVo apiExpectationVo) {
        ApiExpectation apiExpectation = new ApiExpectation();
        apiExpectation.setId(apiExpectationVo.getId());
        apiExpectation.setCreated(apiExpectationVo.getCreated());
        apiExpectation.setApiStatus(apiExpectationVo.getApiStatus());
        apiExpectation.setMethod(apiExpectationVo.getHttpRequest().getMethod().toUpperCase());
        apiExpectation.setName(apiExpectationVo.getName());
        apiExpectation.setGroupId(apiExpectationVo.getGroupId());
        apiExpectation.setPath(apiExpectationVo.getHttpRequest().getPath());
        apiExpectation.setPriority(apiExpectationVo.getPriority());
        apiExpectation.setRequestBody(apiExpectationVo.getHttpRequest().getBody());
        apiExpectation.setRequestBodyType(apiExpectationVo.getHttpRequest().getBodyType());
        apiExpectation.setStatusCode(apiExpectationVo.getHttpResponse().getStatusCode());
        apiExpectation.setResponseBody(apiExpectationVo.getHttpResponse().getBody());
        return apiExpectation;
    }

    public ApiExpectationVo toApiExpectationVo(ApiExpectation apiExpectation, List<ApiHeader> apiHeaderList, List<ApiQueryStringParameter> apiQueryStringParameterList) {
        Long id = apiExpectation.getId();
        ApiExpectationVo apiExpectationVo = new ApiExpectationVo();
        apiExpectationVo.setId(id);
        apiExpectationVo.setApiStatus(apiExpectation.getApiStatus());
        apiExpectationVo.setCreated(apiExpectation.getCreated());
        apiExpectationVo.setGroupId(apiExpectation.getGroupId());
        apiExpectationVo.setPriority(apiExpectation.getPriority());
        apiExpectationVo.setName(apiExpectation.getName());
        ApiHttpResponseVo apiHttpResponseVo = new ApiHttpResponseVo();
        apiHttpResponseVo.setStatusCode(apiExpectation.getStatusCode());
        apiHttpResponseVo.setBody(apiExpectation.getResponseBody());
        apiExpectationVo.setHttpResponse(apiHttpResponseVo);
        apiExpectationVo.setHttpRequest(apiHttpRequestVoMapper.toApiHttpRequestVo(apiExpectation, apiHeaderList, apiQueryStringParameterList));
        return apiExpectationVo;
    }


}
