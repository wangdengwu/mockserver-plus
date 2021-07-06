package cn.mockserver.plus.web.controller;


import cn.mockserver.plus.domain.service.ApiGroupService;
import cn.mockserver.plus.web.view.ApiGroupVo;
import cn.mockserver.plus.web.view.ResultAjax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangdengwu
 */
@RestController
@RequestMapping("/_mock_server")
public class ApiGroupController extends BaseController {
    @Autowired
    private ApiGroupService apiGroupService;

    @GetMapping("/group/all")
    public ResultAjax all() {
        List<ApiGroupVo> all = apiGroupService.all();
        return success("ok", all);
    }

    @PostMapping("/group/add")
    public ResultAjax add(@RequestParam Integer parentId, @RequestParam String label) {
        ApiGroupVo added = apiGroupService.add(parentId, label);
        if (added != null) {
            return success("ok", added);
        } else {
            return error("添加失败!");
        }
    }

    @PostMapping("/group/delete")
    public ResultAjax delete(@RequestParam Integer id) {
        ApiGroupVo delete = apiGroupService.delete(id);
        if (delete != null) {
            return success("ok", delete);
        } else {
            return error("删除失败，请检查是否有子节点。");
        }
    }

    @PostMapping("/group/edit")
    public ResultAjax edit(@RequestParam Integer id, @RequestParam String label) {
        ApiGroupVo edit = apiGroupService.edit(id, label);
        if (edit != null) {
            return success("ok", edit);
        } else {
            return error("修改失败!");
        }
    }
}
