package cn.mockserver.plus.domain.repository;

import cn.mockserver.plus.domain.entity.ApiQueryStringParameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangdengwu
 */
@Repository
public interface ApiQueryStringParameterRepository extends CrudRepository<ApiQueryStringParameter, Integer> {
    /**
     * 根据expectationId获取对应的Parameter信息
     *
     * @param expectationId
     * @return 所有Parameter信息
     */
    List<ApiQueryStringParameter> findByExpectationId(Long expectationId);

    /**
     * 删除所有expectationId对应的Parameter
     *
     * @param expectationId
     */
    void deleteByExpectationId(Long expectationId);
}
