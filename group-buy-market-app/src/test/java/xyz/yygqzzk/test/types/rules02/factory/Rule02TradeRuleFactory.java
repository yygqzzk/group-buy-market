package xyz.yygqzzk.test.types.rules02.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.test.types.rules02.logic.RuleLogic201;
import xyz.yygqzzk.test.types.rules02.logic.RuleLogic202;
import xyz.yygqzzk.test.types.rules02.logic.XxxResponse;
import xyz.yygqzzk.types.design.framework.link.model2.LinkArmory;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Service
public class Rule02TradeRuleFactory {

    @Bean("demo01")
    public BusinessLinkedList<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> demo01(RuleLogic201 ruleLogic201, RuleLogic202 ruleLogic202) {
        LinkArmory<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> linkArmory = new LinkArmory<>("demo01", ruleLogic201, ruleLogic202);

        return linkArmory.getLogicLink();
    }

    @Bean("demo02")
    public BusinessLinkedList<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> demo02(RuleLogic201 ruleLogic201, RuleLogic202 ruleLogic202) {
        LinkArmory<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> linkArmory = new LinkArmory<>("demo02", ruleLogic202, ruleLogic201);

        return linkArmory.getLogicLink();
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private String age;
    }
}




