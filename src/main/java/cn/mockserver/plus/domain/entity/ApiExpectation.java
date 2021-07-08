package cn.mockserver.plus.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wangdengwu
 */
@Entity
@Data
@Table(indexes = {@Index(name = "index_group_id", columnList = "groupId")})
public class ApiExpectation {
    @Id
    private Long id;
    @Column(length = 32, nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer groupId;
    @Column(nullable = false)
    private Long created;
    @Column(nullable = false)
    private Integer priority;
    @Column(length = 16, nullable = false)
    private String apiStatus;
    @Column(length = 8, nullable = false)
    private String method;
    @Column(nullable = false)
    private String path;
    @Column(length = 1024)
    private String requestBody;
    @Column(length = 32)
    private String requestBodyType;
    @Column(nullable = false)
    private Integer statusCode;
    @Column(length = 4096, nullable = false)
    private String responseBody;
}
