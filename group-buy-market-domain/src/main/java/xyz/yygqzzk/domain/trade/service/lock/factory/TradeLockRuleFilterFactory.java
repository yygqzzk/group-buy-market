package xyz.yygqzzk.domain.trade.service.lock.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeLockRuleCommandEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeLockRuleFilterEntity;
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
public class TradeLockRuleFilterFactory {

    @Bean("tradeLockRuleFilter")
    public BusinessLinkedList<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext, TradeLockRuleFilterEntity> tradeLockRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter, UserTakeLimitRuleFilter userTakeLimitRuleFilter) {
        LinkArmory<TradeLockRuleCommandEntity, DynamicContext, TradeLockRuleFilterEntity> tradeRuleFilter = new LinkArmory<>("tradeLockRuleFilter", activityUsabilityRuleFilter, userTakeLimitRuleFilter);

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




