package cn.mockserver.plus.web.view;


import cn.mockserver.plus.domain.entity.ApiGroup;
import lombok.Data;

import java.util.List;

/**
 * @author wangdengwu
 */
@Data
public class ApiGroupVo {
    private Integer id;
    private String label;
    private Integer level;
    private List<ApiGroupVo> children;

    public ApiGroupVo(ApiGroup apiGroup) {
        this.id = apiGroup.getId();
        this.label = apiGroup.getLabel();
        this.level = apiGroup.getLevel();
    }
}
