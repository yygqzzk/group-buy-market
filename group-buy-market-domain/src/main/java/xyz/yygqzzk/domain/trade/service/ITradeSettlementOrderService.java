package xyz.yygqzzk.domain.trade.service;

import xyz.yygqzzk.domain.trade.model.entity.TradePaySettlementEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradePaySuccessEntity;

import java.util.Map;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
public interface ITradeSettlementOrderService {

    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception;

    /* 结算回调通知 */
    /* 定时任务处理结算回调通知 */
    Map<String, Integer> execSettlementNotifyJob() throws Exception;
    /* 指定teamId进行结算回调 */
    Map<String, Integer> execSettlementNotifyJob(String teamId) throws Exception;
}
