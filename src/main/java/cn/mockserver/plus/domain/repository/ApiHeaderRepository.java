package cn.mockserver.plus.domain.repository;

import cn.mockserver.plus.domain.entity.ApiHeader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangdengwu
 */
@Repository
public interface ApiHeaderRepository extends CrudRepository<ApiHeader, Integer> {
    /**
     * 根据expectationId获取对应的Header信息
     *
     * @param expectationId
     * @return 所有Header信息
     */
    List<ApiHeader> findByExpectationId(Long expectationId);

    /**
     * 删除所有expectationId对应的Header
     *
     * @param expectationId
     */
    void deleteByExpectationId(Long expectationId);
}
