package xyz.yygqzzk.domain.trade.service;

import xyz.yygqzzk.domain.trade.model.entity.TradePaySettlementEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradePaySuccessEntity;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
public interface ITradeSettlementOrderService {

    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity);


}
