package cn.mockserver.plus.domain.service;

import cn.mockserver.plus.domain.entity.ApiExpectation;
import cn.mockserver.plus.domain.entity.ApiHeader;
import cn.mockserver.plus.domain.entity.ApiQueryStringParameter;
import cn.mockserver.plus.domain.mapper.ApiExpectationVoMapper;
import cn.mockserver.plus.domain.repository.ApiExpectationRepository;
import cn.mockserver.plus.domain.repository.ApiHeaderRepository;
import cn.mockserver.plus.domain.repository.ApiQueryStringParameterRepository;
import cn.mockserver.plus.interceptor.MockServerInterceptor;
import cn.mockserver.plus.web.view.ApiExpectationVo;
import com.github.yitter.idgen.YitIdHelper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.mockserver.mock.Expectation;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private ApiExpectationVoMapper apiExpectationVoMapper;

    @PostConstruct
    public void init() {
        apiExpectationRepository.findAll().forEach(apiExpectation -> {
            List<ApiHeader> apiHeaderList = apiHeaderRepository.findByExpectationId(apiExpectation.getId());
            List<ApiQueryStringParameter> apiQueryStringParameterList = apiQueryStringParameterRepository.findByExpectationId(apiExpectation.getId());
            Expectation expectation = apiExpectationVoMapper.toExpectation(apiExpectationVoMapper.toApiExpectationVo(apiExpectation, apiHeaderList, apiQueryStringParameterList));
            mockServerInterceptor.add(expectation);
        });
    }

    @Transactional
    public ApiExpectationVo add(ApiExpectationVo apiExpectationVo) {
        apiExpectationVo.setCreated(System.currentTimeMillis());
        apiExpectationVo.setId(YitIdHelper.nextId());
        apiExpectationRepository.save(apiExpectationVoMapper.toApiExpectation(apiExpectationVo));
        Map<String, String> headers = apiExpectationVo.getHttpRequest().getHeaders();
        if (headers != null && !headers.isEmpty()) {
            List<ApiHeader> headerList = getApiHeaders(apiExpectationVo.getId(), headers);
            apiHeaderRepository.saveAll(headerList);
        }
        Map<String, String> queryStringParameters = apiExpectationVo.getHttpRequest().getQueryStringParameters();
        if (queryStringParameters != null && !queryStringParameters.isEmpty()) {
            List<ApiQueryStringParameter> queryStringParameterList = getApiQueryStringParameters(apiExpectationVo.getId(), queryStringParameters);
            apiQueryStringParameterRepository.saveAll(queryStringParameterList);
        }

        if (!apiExpectationVo.isDone()) {
            mockServerInterceptor.add(apiExpectationVoMapper.toExpectation(apiExpectationVo));
        }
        return apiExpectationVo;
    }

    @Transactional
    @SuppressWarnings("all")
    public ApiExpectationVo save(ApiExpectationVo apiExpectationVo) {
        apiExpectationRepository.save(apiExpectationVoMapper.toApiExpectation(apiExpectationVo));
        Map<String, String> headers = apiExpectationVo.getHttpRequest().getHeaders();
        if (headers != null && !headers.isEmpty()) {
            List<ApiHeader> headerList = getApiHeaders(apiExpectationVo.getId(), headers);
            apiHeaderRepository.deleteByExpectationId(apiExpectationVo.getId());
            apiHeaderRepository.saveAll(headerList);
        }
        Map<String, String> queryStringParameters = apiExpectationVo.getHttpRequest().getQueryStringParameters();
        if (queryStringParameters != null && !queryStringParameters.isEmpty()) {
            List<ApiQueryStringParameter> queryStringParameterList = getApiQueryStringParameters(apiExpectationVo.getId(), queryStringParameters);
            apiQueryStringParameterRepository.deleteByExpectationId(apiExpectationVo.getId());
            apiQueryStringParameterRepository.saveAll(queryStringParameterList);
        }

        deleteExpectation(apiExpectationVo.getId());

        if (!apiExpectationVo.isDone()) {
            mockServerInterceptor.add(apiExpectationVoMapper.toExpectation(apiExpectationVo));
        }
        return apiExpectationVo;
    }

    @Transactional
    @SuppressWarnings("all")
    public void delete(Long id) {
        Optional<ApiExpectation> apiExpectation = apiExpectationRepository.findById(id);
        deleteExpectation(apiExpectation.get().getId());
        apiQueryStringParameterRepository.deleteByExpectationId(id);
        apiHeaderRepository.deleteByExpectationId(id);
        apiExpectationRepository.deleteById(id);
    }

    public List<ApiExpectationVo> list(Integer groupId) {
        List<ApiExpectation> apiExpectationList = apiExpectationRepository.findByGroupIdOrderByCreatedDesc(groupId);
        return apiExpectationList.stream().map(apiExpectation -> {
            List<ApiHeader> apiHeaderList = apiHeaderRepository.findByExpectationId(apiExpectation.getId());
            List<ApiQueryStringParameter> apiQueryStringParameterList = apiQueryStringParameterRepository.findByExpectationId(apiExpectation.getId());
            return apiExpectationVoMapper.toApiExpectationVo(apiExpectation, apiHeaderList, apiQueryStringParameterList);
        }).collect(Collectors.toList());
    }

    private void deleteExpectation(Long id) {
        JSONObject body = new JSONObject();
        body.put("id", id.toString());
        HttpRequest httpRequest = HttpRequest.request().withBody(body.toJSONString()).withQueryStringParameter("type", "EXPECTATIONS");
        mockServerInterceptor.delete(httpRequest);
    }

    private List<ApiQueryStringParameter> getApiQueryStringParameters(Long expectationId, Map<String, String> queryStringParameters) {
        return queryStringParameters.entrySet().stream().map(stringStringEntry -> {
            ApiQueryStringParameter apiQueryStringParameter = new ApiQueryStringParameter();
            apiQueryStringParameter.setExpectationId(expectationId);
            apiQueryStringParameter.setName(stringStringEntry.getKey());
            apiQueryStringParameter.setValue(stringStringEntry.getValue());
            return apiQueryStringParameter;
        }).collect(Collectors.toList());
    }

    private List<ApiHeader> getApiHeaders(Long expectationId, Map<String, String> headers) {
        return headers.entrySet().stream().map(stringStringEntry -> {
            ApiHeader apiHeader = new ApiHeader();
            apiHeader.setExpectationId(expectationId);
            apiHeader.setName(stringStringEntry.getKey());
            apiHeader.setValue(stringStringEntry.getValue());
            return apiHeader;
        }).collect(Collectors.toList());
    }
}
