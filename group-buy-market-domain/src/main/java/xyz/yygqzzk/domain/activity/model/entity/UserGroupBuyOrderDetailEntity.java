package xyz.yygqzzk.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团组队实体对象
 * @since 2025/5/2
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupBuyOrderDetailEntity {
    // 用户ID
    private String userId;
    // 拼单组队ID
    private String teamId;
    // 活动ID
    private Long activityId;
    // 目标数量
    private Integer targetCount;
    // 完成数量
    private Integer completeCount;
    // 锁单数量
    private Integer lockCount;
    // 拼团开始时间 - 参与拼团时间
    private Date validStartTime;
    // 拼团结束时间 - 拼团有效时长
    private Date validEndTime;
    // 倒计时(字符串) validEndTime - validStartTime
    private String validTimeCountdown;
    /** 外部交易单号-确保外部调用唯一幂等 */
    private String outTradeNo;
}




