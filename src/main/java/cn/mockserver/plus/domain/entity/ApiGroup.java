package cn.mockserver.plus.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wangdengwu
 */
@Entity
@Data
@Table(indexes = {@Index(name = "index_parent_id", columnList = "parentId")})
public class ApiGroup {
    public static final int RootParentId = -1;
    public static final int MaxLevel = 3;
    public static final int RootParentLevel = 0;
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 32, nullable = false)
    private String label;
    @Column(nullable = false)
    private Integer parentId = -1;
    @Column(nullable = false)
    private Integer level;
}
