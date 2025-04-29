package xyz.yygqzzk.domain.trade.adapter.repository;

import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/29
 */
public interface ITradeRepository {




    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo);

    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);
}
