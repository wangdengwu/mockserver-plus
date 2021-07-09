package cn.mockserver.plus.domain.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * @author wangdengwu
 */
@Entity
@Data
@Table(indexes = {@Index(name = "index_header_expectation_id", columnList = "expectationId")})
public class ApiHeader {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Long expectationId;

    @Column(length = 32)
    private String name;

    @Column(length = 2048)
    private String value;
}
