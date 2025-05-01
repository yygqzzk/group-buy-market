package xyz.yygqzzk.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团，支付活动实体对象
 * @since 2025/4/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {
    /** 拼单组队ID */
    private String teamId;
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 拼团开始时间 */
    private Date startTime;
    /** 拼团结束时间 */
    private Date endTime;
    /* 拼团时长（分钟） */
    private Integer validTime;
    /** 目标数量 */
    private Integer targetCount;
}




