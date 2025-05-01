package xyz.yygqzzk.domain.trade.service.settlement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import xyz.yygqzzk.domain.trade.model.entity.*;
import xyz.yygqzzk.domain.trade.service.ITradeSettlementOrderService;
import xyz.yygqzzk.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import xyz.yygqzzk.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团交易结算服务
 * @since 2025/4/30
 */
@Slf4j
@Service
public class TradeSettlementOrderServiceImpl implements ITradeSettlementOrderService {

    @Resource
    private ITradeRepository repository;
    @Resource
    private BusinessLinkedList<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> tradeSettlementRuleFilter;

    @Override
    public TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception {
        log.info("拼团交易-支付订单结算:{} outTradeNo:{}", tradePaySuccessEntity.getUserId(), tradePaySuccessEntity.getOutTradeNo());

        /* 通过责任链来规则过滤: SC渠道管控、有效外部交易单号、交易时间有效性 */
        // 1. 结算规则过滤
        TradeSettlementRuleCommandEntity tradeSettlementRuleCommandEntity = TradeSettlementRuleCommandEntity.builder().source(tradePaySuccessEntity.getSource()).channel(tradePaySuccessEntity.getChannel()).userId(tradePaySuccessEntity.getUserId()).outTradeNo(tradePaySuccessEntity.getOutTradeNo()).outTradeTime(tradePaySuccessEntity.getOutTradeTime()).build();

        TradeSettlementRuleFilterBackEntity tradeSettlementRuleFilterBackEntity = tradeSettlementRuleFilter.apply(tradeSettlementRuleCommandEntity, new TradeSettlementRuleFilterFactory.DynamicContext());

        String teamId = tradeSettlementRuleFilterBackEntity.getTeamId();

        // 2. 查询组团信息
        GroupBuyTeamEntity groupBuyTeamEntity = GroupBuyTeamEntity.builder().teamId(tradeSettlementRuleFilterBackEntity.getTeamId()).activityId(tradeSettlementRuleFilterBackEntity.getActivityId()).targetCount(tradeSettlementRuleFilterBackEntity.getTargetCount()).completeCount(tradeSettlementRuleFilterBackEntity.getCompleteCount()).lockCount(tradeSettlementRuleFilterBackEntity.getLockCount()).status(tradeSettlementRuleFilterBackEntity.getStatus()).validStartTime(tradeSettlementRuleFilterBackEntity.getValidStartTime()).validEndTime(tradeSettlementRuleFilterBackEntity.getValidEndTime()).build();


        // 3. 构建聚合对象
        GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate = GroupBuyTeamSettlementAggregate.builder().userEntity(UserEntity.builder().userId(tradePaySuccessEntity.getUserId()).build()).groupBuyTeamEntity(groupBuyTeamEntity).tradePaySuccessEntity(tradePaySuccessEntity).build();


        // 4. 拼团交易结算
        repository.settlementMarketPayOrder(groupBuyTeamSettlementAggregate);


        return TradePaySettlementEntity.builder().source(tradePaySuccessEntity.getSource()).channel(tradePaySuccessEntity.getChannel()).userId(tradePaySuccessEntity.getUserId()).teamId(teamId).activityId(groupBuyTeamEntity.getActivityId()).outTradeNo(tradePaySuccessEntity.getOutTradeNo()).build();
    }
}




