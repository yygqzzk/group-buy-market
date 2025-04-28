package xyz.yygqzzk.domain.tag.model.entity;

import lombok.*;

import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description 批次任务对象
 * @since 2025/4/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CrowdTagsJobEntity {
    /** 标签类型（参与量、消费金额） */
    private Integer tagType;
    /** 标签规则（限定类型 N次） */
    private String tagRule;
    /** 统计数据，开始时间 */
    private Date statStartTime;
    /** 统计数据，结束时间 */
    private Date statEndTime;
}




