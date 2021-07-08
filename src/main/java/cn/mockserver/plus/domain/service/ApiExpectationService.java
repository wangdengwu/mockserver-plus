package cn.mockserver.plus.domain.service;

import cn.mockserver.plus.domain.entity.ApiExpectation;
import cn.mockserver.plus.domain.entity.ApiHeader;
import cn.mockserver.plus.domain.entity.ApiQueryStringParameter;
import cn.mockserver.plus.domain.repository.ApiExpectationRepository;
import cn.mockserver.plus.domain.repository.ApiHeaderRepository;
import cn.mockserver.plus.domain.repository.ApiQueryStringParameterRepository;
import cn.mockserver.plus.interceptor.MockServerInterceptor;
import cn.mockserver.plus.web.view.ApiExpectationVo;
import cn.mockserver.plus.web.view.ApiHttpRequestVo;
import cn.mockserver.plus.web.view.ApiHttpResponseVo;
import com.github.yitter.idgen.YitIdHelper;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.mock.Expectation;
import org.mockserver.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author wangdengwu
 */
@Service
@Slf4j
public class ApiExpectationService {
    @Autowired
    private MockServerInterceptor mockServerInterceptor;
    @Autowired
    private ApiExpectationRepository apiExpectationRepository;
    @Autowired
    private ApiHeaderRepository apiHeaderRepository;
    @Autowired
    private ApiQueryStringParameterRepository apiQueryStringParameterRepository;

    @Transactional
    public ApiExpectationVo add(ApiExpectationVo apiExpectationVo) {
        apiExpectationVo.setCreated(System.currentTimeMillis());
        apiExpectationVo.setId(YitIdHelper.nextId());
        apiExpectationRepository.save(map2ApiExpectation(apiExpectationVo));
        Map<String, String> headers = apiExpectationVo.getHttpRequest().getHeaders();
        Map<String, String> queryStringParameters = apiExpectationVo.getHttpRequest().getQueryStringParameters();
        if (headers != null && !headers.isEmpty()) {
            List<ApiHeader> headerList = headers.entrySet().stream().map(stringStringEntry -> {
                ApiHeader apiHeader = new ApiHeader();
                apiHeader.setExpectationId(apiExpectationVo.getId());
                apiHeader.setName(stringStringEntry.getKey());
                apiHeader.setValue(stringStringEntry.getValue());
                return apiHeader;
            }).collect(Collectors.toList());
            apiHeaderRepository.saveAll(headerList);
        }
        if (queryStringParameters != null && !queryStringParameters.isEmpty()) {
            List<ApiQueryStringParameter> queryStringParameterList = queryStringParameters.entrySet().stream().map(stringStringEntry -> {
                ApiQueryStringParameter apiQueryStringParameter = new ApiQueryStringParameter();
                apiQueryStringParameter.setExpectationId(apiExpectationVo.getId());
                apiQueryStringParameter.setName(stringStringEntry.getKey());
                apiQueryStringParameter.setValue(stringStringEntry.getValue());
                return apiQueryStringParameter;
            }).collect(Collectors.toList());
            apiQueryStringParameterRepository.saveAll(queryStringParameterList);
        }
        String apiStatus = apiExpectationVo.getApiStatus();
        if (!StringUtils.equals("done", apiStatus)) {
            mockServerInterceptor.add(map2Expectation(apiExpectationVo));
        }
        return apiExpectationVo;
    }

    @Transactional
    public ApiExpectationVo save(ApiExpectationVo apiExpectationVo) {
        apiExpectationRepository.save(map2ApiExpectation(apiExpectationVo));
        Map<String, String> headers = apiExpectationVo.getHttpRequest().getHeaders();
        Map<String, String> queryStringParameters = apiExpectationVo.getHttpRequest().getQueryStringParameters();
        if (headers != null && !headers.isEmpty()) {
            List<ApiHeader> headerList = headers.entrySet().stream().map(stringStringEntry -> {
                ApiHeader apiHeader = new ApiHeader();
                apiHeader.setExpectationId(apiExpectationVo.getId());
                apiHeader.setName(stringStringEntry.getKey());
                apiHeader.setValue(stringStringEntry.getValue());
                return apiHeader;
            }).collect(Collectors.toList());
            apiHeaderRepository.deleteByExpectationId(apiExpectationVo.getId());
            apiHeaderRepository.saveAll(headerList);
        }
        if (queryStringParameters != null && !queryStringParameters.isEmpty()) {
            List<ApiQueryStringParameter> queryStringParameterList = queryStringParameters.entrySet().stream().map(stringStringEntry -> {
                ApiQueryStringParameter apiQueryStringParameter = new ApiQueryStringParameter();
                apiQueryStringParameter.setExpectationId(apiExpectationVo.getId());
                apiQueryStringParameter.setName(stringStringEntry.getKey());
                apiQueryStringParameter.setValue(stringStringEntry.getValue());
                return apiQueryStringParameter;
            }).collect(Collectors.toList());
            apiQueryStringParameterRepository.deleteByExpectationId(apiExpectationVo.getId());
            apiQueryStringParameterRepository.saveAll(queryStringParameterList);
        }
        String apiStatus = apiExpectationVo.getApiStatus();
        if (StringUtils.equals("done", apiStatus)) {
            mockServerInterceptor.delete(map2HttpRequest(apiExpectationVo.getHttpRequest()));
        }
        return apiExpectationVo;
    }

    @Transactional
    public void delete(Long id) {
        Optional<ApiExpectation> apiExpectation = apiExpectationRepository.findById(id);
        List<ApiHeader> apiHeaderList = apiHeaderRepository.findByExpectationId(id);
        List<ApiQueryStringParameter> apiQueryStringParameterList = apiQueryStringParameterRepository.findByExpectationId(id);
        ApiHttpRequestVo apiHttpRequestVo = map2ApiHttpRequestVo(apiExpectation.get(), apiHeaderList, apiQueryStringParameterList);
        mockServerInterceptor.delete(map2HttpRequest(apiHttpRequestVo));
        apiQueryStringParameterRepository.deleteByExpectationId(id);
        apiHeaderRepository.deleteByExpectationId(id);
        apiExpectationRepository.deleteById(id);
    }

    public List<ApiExpectationVo> list(Integer groupId) {
        List<ApiExpectation> apiExpectationList = apiExpectationRepository.findByGroupId(groupId);
        return apiExpectationList.stream().map(apiExpectation -> {
            Long id = apiExpectation.getId();
            List<ApiHeader> apiHeaderList = apiHeaderRepository.findByExpectationId(id);
            List<ApiQueryStringParameter> apiQueryStringParameterList = apiQueryStringParameterRepository.findByExpectationId(id);
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
            apiExpectationVo.setHttpRequest(map2ApiHttpRequestVo(apiExpectation, apiHeaderList, apiQueryStringParameterList));
            return apiExpectationVo;
        }).collect(Collectors.toList());
    }

    private ApiHttpRequestVo map2ApiHttpRequestVo(ApiExpectation apiExpectation, List<ApiHeader> apiHeaderList, List<ApiQueryStringParameter> apiQueryStringParameterList) {
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


    private ApiExpectation map2ApiExpectation(ApiExpectationVo apiExpectationVo) {
        ApiExpectation apiExpectation = new ApiExpectation();
        apiExpectation.setId(apiExpectationVo.getId());
        apiExpectation.setCreated(apiExpectationVo.getCreated());
        apiExpectation.setApiStatus(apiExpectationVo.getApiStatus());
        apiExpectation.setMethod(apiExpectationVo.getHttpRequest().getMethod());
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

    private Expectation map2Expectation(ApiExpectationVo apiExpectationVo) {
        ApiHttpRequestVo httpRequest = apiExpectationVo.getHttpRequest();
        ApiHttpResponseVo httpResponse = apiExpectationVo.getHttpResponse();
        Expectation expectation = Expectation.when(map2HttpRequest(httpRequest), apiExpectationVo.getPriority())
                .withId(apiExpectationVo.getId().toString())
                .withCreated(apiExpectationVo.getCreated()).thenRespond(map2HttpResponse(httpResponse));
        return expectation;
    }

    private HttpRequest map2HttpRequest(ApiHttpRequestVo apiHttpRequestVo) {
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

    private HttpResponse map2HttpResponse(ApiHttpResponseVo apiHttpResponseVo) {
        HttpResponse httpResponse = HttpResponse.response();
        Integer statusCode = apiHttpResponseVo.getStatusCode();
        httpResponse.withStatusCode(statusCode);
        String body = apiHttpResponseVo.getBody();
        JsonBody jsonBody = JsonBody.json(body, MediaType.APPLICATION_JSON_UTF_8);
        httpResponse.withBody(jsonBody);
        return httpResponse;
    }

    private Parameters retrieveFormParameters(String parameterString) {
        Parameters parameters = new Parameters();
        Map<String, List<String>> parameterMap = new HashMap<>(8);
        if (isNotBlank(parameterString)) {
            try {
                parameterMap.putAll(new QueryStringDecoder(parameterString, false).parameters());
            } catch (IllegalArgumentException iae) {
                log.error("retrieveFormParameters:" + iae.getMessage());
            }
        }
        return parameters.withEntries(parameterMap);
    }


}
