package cn.mockserver.plus.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wangdengwu
 */
@Entity
@Data
@Table(indexes = {@Index(name = "index_parameter_expectation_id", columnList = "expectationId")})
public class ApiQueryStringParameter {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Long expectationId;

    @Column(length = 32)
    private String name;

    @Column
    private String value;
}
