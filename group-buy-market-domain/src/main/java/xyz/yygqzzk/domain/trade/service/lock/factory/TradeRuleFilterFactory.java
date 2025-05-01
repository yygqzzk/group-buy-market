package xyz.yygqzzk.domain.trade.service.lock.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeRuleCommandEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeRuleFilterEntity;
import xyz.yygqzzk.domain.trade.service.lock.filter.ActivityUsabilityRuleFilter;
import xyz.yygqzzk.domain.trade.service.lock.filter.UserTakeLimitRuleFilter;
import xyz.yygqzzk.types.design.framework.link.model2.LinkArmory;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Service
@Slf4j
public class TradeRuleFilterFactory {

    @Bean
    public BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterEntity> tradeRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter, UserTakeLimitRuleFilter userTakeLimitRuleFilter) {
        LinkArmory<TradeRuleCommandEntity, DynamicContext, TradeRuleFilterEntity> tradeRuleFilter = new LinkArmory<>("tradeRuleFilter", activityUsabilityRuleFilter, userTakeLimitRuleFilter);

        return tradeRuleFilter.getLogicLink();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        /* 营销活动实体 */
        GroupBuyActivityEntity groupBuyActivity;
    }
}




