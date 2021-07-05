package cn.mockserver.plus.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author wangdengwu
 */
@Entity
@Data
public class ApiGroup {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 32)
    private String label;
    @Column
    private Integer parentId;
}
