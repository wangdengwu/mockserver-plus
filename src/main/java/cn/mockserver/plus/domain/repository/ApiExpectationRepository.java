package cn.mockserver.plus.domain.repository;

import cn.mockserver.plus.domain.entity.ApiExpectation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangdengwu
 */
@Repository
public interface ApiExpectationRepository extends CrudRepository<ApiExpectation, Long> {
    /**
     * 获取对应分组下的Expectation
     *
     * @param groupId
     * @return Expectation List
     */
    List<ApiExpectation> findByGroupIdOrderByCreatedDesc(Integer groupId);
}
