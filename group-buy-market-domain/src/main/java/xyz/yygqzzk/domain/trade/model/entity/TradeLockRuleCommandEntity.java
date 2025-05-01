package xyz.yygqzzk.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团交易命令实体
 * @since 2025/4/30
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeLockRuleCommandEntity {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
}




