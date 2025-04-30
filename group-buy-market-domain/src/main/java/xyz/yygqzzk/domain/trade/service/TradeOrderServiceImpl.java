package xyz.yygqzzk.domain.trade.service;

import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import xyz.yygqzzk.domain.trade.model.entity.*;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;
import xyz.yygqzzk.domain.trade.service.factory.TradeRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description 订单交易服务
 * @since 2025/4/29
 */
@Service
public class TradeOrderServiceImpl implements ITradeOrderService {
    @Resource
    private ITradeRepository repository;

    @Resource
    private BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterEntity> tradeRuleFilter;



    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivity, PayDiscountEntity payDiscountEntity) throws Exception {

        /* 通过责任链来校验活动和用户 */
        TradeRuleFilterEntity tradeRuleFilterEntity = tradeRuleFilter.apply(TradeRuleCommandEntity.builder()
                        .userId(userEntity.getUserId())
                        .activityId(payActivity.getActivityId())
                        .build(),
                new TradeRuleFilterFactory.DynamicContext());



        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivity)
                .payDiscountEntity(payDiscountEntity)
                .userTakeOrderCount(tradeRuleFilterEntity.getUserTakeOrderCount())
                .build();

        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        return repository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        return repository.queryNoPayMarketPayOrderByOutTradeNo(userId, outTradeNo);
    }
}




