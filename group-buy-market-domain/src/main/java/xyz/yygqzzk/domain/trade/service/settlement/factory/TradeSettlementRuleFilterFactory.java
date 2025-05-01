package xyz.yygqzzk.domain.trade.service.settlement.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.model.entity.*;
import xyz.yygqzzk.domain.trade.service.settlement.filter.EndRuleFilter;
import xyz.yygqzzk.domain.trade.service.settlement.filter.OutTradeNoRuleFilter;
import xyz.yygqzzk.domain.trade.service.settlement.filter.SCRuleFilter;
import xyz.yygqzzk.domain.trade.service.settlement.filter.SettableRuleFilter;
import xyz.yygqzzk.types.design.framework.link.model2.LinkArmory;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/5/1
 */
@Slf4j
@Service
public class TradeSettlementRuleFilterFactory {

    @Bean(name = "tradeSettlementRuleFilter")
    public BusinessLinkedList<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> tradeSettlementRuleFilter(EndRuleFilter endRuleFilter, OutTradeNoRuleFilter outTradeNoRuleFilter, SCRuleFilter scRuleFilter, SettableRuleFilter settableRuleFilter) {
        LinkArmory<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> linkArmory = new LinkArmory<>("tradeSettlementRuleFilter", scRuleFilter, outTradeNoRuleFilter, settableRuleFilter, endRuleFilter);
        return linkArmory.getLogicLink();
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        // 订单营销实体对象
        private MarketPayOrderEntity marketPayOrderEntity;
        // 拼团组队实体对象
        private GroupBuyTeamEntity groupBuyTeamEntity;
    }

}




