package cn.mockserver.plus.web.controller;


import cn.mockserver.plus.domain.service.ApiGroupService;
import cn.mockserver.plus.web.view.ApiGroupVo;
import cn.mockserver.plus.web.view.ResultAjax;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangdengwu
 */
@RestController
@RequestMapping("/_mock_server/group")
public class ApiGroupController extends BaseController {
    @Autowired
    private ApiGroupService apiGroupService;

    @GetMapping("/all")
    public ResultAjax all() {
        List<ApiGroupVo> all = apiGroupService.all();
        return success("ok", all);
    }

    @PostMapping("/add")
    public ResultAjax add(@RequestParam Integer parentId, @RequestParam String label) {
        if(StringUtils.isBlank(label)){
            return error("内容不能为空!");
        }
        ApiGroupVo added = apiGroupService.add(parentId, label);
        if (added != null) {
            return success("ok", added);
        } else {
            return error("添加失败!");
        }
    }

    @PostMapping("/delete")
    public ResultAjax delete(@RequestParam Integer id) {
        ApiGroupVo delete = apiGroupService.delete(id);
        if (delete != null) {
            return success("ok", delete);
        } else {
            return error("删除失败，请检查是否有子节点。");
        }
    }

    @PostMapping("/edit")
    public ResultAjax edit(@RequestParam Integer id, @RequestParam String label) {
        if(StringUtils.isBlank(label)){
            return error("内容不能为空!");
        }
        ApiGroupVo edit = apiGroupService.edit(id, label);
        if (edit != null) {
            return success("ok", edit);
        } else {
            return error("修改失败!");
        }
    }
}
