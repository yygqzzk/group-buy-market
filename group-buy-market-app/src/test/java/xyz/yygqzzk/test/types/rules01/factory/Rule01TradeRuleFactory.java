package xyz.yygqzzk.test.types.rules01.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.test.types.rules01.logic.RuleLogic101;
import xyz.yygqzzk.test.types.rules01.logic.RuleLogic102;
import xyz.yygqzzk.types.design.framework.link.model1.ILogicLink;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Service
public class Rule01TradeRuleFactory {

    @Resource
    private RuleLogic101 ruleLogic101;

    @Resource
    private RuleLogic102 ruleLogic102;

    public ILogicLink<String, DynamicContext, String> openLogicLink() {
        ruleLogic101.appendNext(ruleLogic102);
        return ruleLogic101;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private String age;
    }
}




