package cn.mockserver.plus.web.controller;


import cn.mockserver.plus.domain.service.ApiExpectationService;
import cn.mockserver.plus.domain.service.ApiGroupService;
import cn.mockserver.plus.web.view.ApiExpectationVo;
import cn.mockserver.plus.web.view.ResultAjax;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangdengwu
 */
@RestController
@RequestMapping("/_mock_server/expectation")
public class ApiExpectationController extends BaseController {
    @Autowired
    private ApiExpectationService apiExpectationService;
    @Autowired
    private ApiGroupService apiGroupService;

    @PostMapping("/add")
    public ResultAjax add(@RequestBody ApiExpectationVo apiExpectationVo) {
        Integer groupId = apiExpectationVo.getGroupId();
        String name = apiExpectationVo.getName();
        String path = apiExpectationVo.getHttpRequest().getPath();
        if (StringUtils.isBlank(name)) {
            return error("名称不能为空!");
        }
        if (StringUtils.isBlank(path)) {
            return error("path不能为空!");
        }
        if (StringUtils.isBlank(name) || groupId == null || !apiGroupService.haveGroup(groupId)) {
            return error("分组不能为空!");
        }
        ApiExpectationVo result = apiExpectationService.add(apiExpectationVo);
        return success("ok", result);
    }

    @PostMapping("/save")
    public ResultAjax save(@RequestBody ApiExpectationVo apiExpectationVo) {
        Long id = apiExpectationVo.getId();
        Integer groupId = apiExpectationVo.getGroupId();
        String name = apiExpectationVo.getName();
        String path = apiExpectationVo.getHttpRequest().getPath();
        if (id == null) {
            return error("主键不能为空!");
        }
        if (StringUtils.isBlank(name)) {
            return error("名称不能为空!");
        }
        if (StringUtils.isBlank(path)) {
            return error("path不能为空!");
        }
        if (StringUtils.isBlank(name) || groupId == null || !apiGroupService.haveGroup(groupId)) {
            return error("分组不能为空!");
        }
        ApiExpectationVo result = apiExpectationService.save(apiExpectationVo);
        return success("ok", result);
    }

    @PostMapping("/list")
    public ResultAjax list(@RequestParam Integer groupId) {
        List<ApiExpectationVo> all = apiExpectationService.list(groupId);
        return success("ok", all);
    }

    @PostMapping("/delete")
    public ResultAjax delete(@RequestParam Long id) {
        apiExpectationService.delete(id);
        return success("ok");
    }
}
