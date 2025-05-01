package xyz.yygqzzk.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团交易,过略反馈实体
 * @since 2025/4/30
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeLockRuleFilterEntity {

    // 用户参与活动的订单量
    private Integer userTakeOrderCount;
}




