package cn.mockserver.plus.domain.service;

import cn.mockserver.plus.domain.entity.ApiGroup;
import cn.mockserver.plus.domain.repository.ApiGroupRepository;
import cn.mockserver.plus.web.view.ApiGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangdengwu
 */
@Service
public class ApiGroupService {
    @Autowired
    private ApiGroupRepository apiGroupRepository;

    /**
     * 一次性获取所有节点，暂不考虑大数据情况
     *
     * @return 所有节点
     */
    public List<ApiGroupVo> all() {
        HashMap<Integer, Set<ApiGroup>> childrenSetMap = new HashMap<>();
        List<ApiGroup> root = apiGroupRepository.findByParentId(-1);
        Iterable<ApiGroup> all = apiGroupRepository.findAll();
        all.forEach(apiGroup -> {
            Integer parentId = apiGroup.getParentId();
            Set<ApiGroup> childrenSet = childrenSetMap.get(parentId);
            if (childrenSet == null) {
                childrenSet = new HashSet<>();
                childrenSetMap.put(parentId, childrenSet);
            }
            childrenSet.add(apiGroup);
        });
        return root.stream()
                .map(apiGroup -> buildVo(apiGroup, childrenSetMap))
                .collect(Collectors.toList());
    }

    /**
     * 递归方法
     * 递归获取子节点
     *
     * @param apiGroup
     * @param childrenSetMap
     * @return 包含子节点的树形节点
     */
    private ApiGroupVo buildVo(ApiGroup apiGroup, HashMap<Integer, Set<ApiGroup>> childrenSetMap) {
        ApiGroupVo apiGroupVo = new ApiGroupVo(apiGroup);
        Integer id = apiGroup.getId();
        Set<ApiGroup> childrenSet = childrenSetMap.get(id);
        if (childrenSet != null) {
            List<ApiGroupVo> childrenList = childrenSet.stream()
                    .map(children -> buildVo(children, childrenSetMap))
                    .sorted(Comparator.comparingInt(ApiGroupVo::getId))
                    .collect(Collectors.toList());
            apiGroupVo.setChildren(childrenList);
        }
        return apiGroupVo;
    }

    public ApiGroupVo add(Integer parentId, String label) {
        Optional<ApiGroup> parent = Optional.empty();
        //非根节点才需要查询父节点
        if (parentId != ApiGroup.RootParentId) {
            parent = apiGroupRepository.findById(parentId);
        }
        Integer level = parent.isPresent() ? parent.get().getLevel() : ApiGroup.RootParentLevel;
        if (level > ApiGroup.MaxLevel) {
            return null;
        }
        ApiGroup apiGroup = new ApiGroup();
        apiGroup.setLabel(label);
        apiGroup.setParentId(parentId);
        apiGroup.setLevel(++level);
        ApiGroup save = apiGroupRepository.save(apiGroup);
        return new ApiGroupVo(save);
    }
}
