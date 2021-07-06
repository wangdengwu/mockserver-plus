package cn.mockserver.plus.domain.repository;


import cn.mockserver.plus.domain.entity.ApiGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangdengwu
 */
@Repository
public interface ApiGroupRepository extends CrudRepository<ApiGroup, Integer> {
    /**
     * 根据parentId查询对应子节点，根节点对应parentId为-1
     *
     * @param parentId
     * @return 对应子节点
     */
    List<ApiGroup> findByParentId(Integer parentId);
}
