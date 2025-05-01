package xyz.yygqzzk.domain.trade.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyTeamEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradePaySuccessEntity;
import xyz.yygqzzk.domain.trade.model.entity.UserEntity;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyTeamSettlementAggregate {

    /** 用户实体对象 */
    private UserEntity userEntity;
    /** 拼团组队实体对象 */
    private GroupBuyTeamEntity groupBuyTeamEntity;
    /** 交易支付订单实体对象 */
    private TradePaySuccessEntity tradePaySuccessEntity;
}




