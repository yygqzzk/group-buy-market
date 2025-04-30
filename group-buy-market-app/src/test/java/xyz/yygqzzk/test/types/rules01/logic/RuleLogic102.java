package xyz.yygqzzk.test.types.rules01.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.test.types.rules01.factory.Rule01TradeRuleFactory;
import xyz.yygqzzk.types.design.framework.link.model1.AbstractLogicLink;
import xyz.yygqzzk.types.design.framework.link.model1.ILogicLink;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Slf4j
@Service
public class RuleLogic102 extends AbstractLogicLink<String, Rule01TradeRuleFactory.DynamicContext, String> {
    @Override
    public String apply(String requestParameter, Rule01TradeRuleFactory.DynamicContext dynamicContext) throws Exception {
        log.info("link model102 RuleLogic102");
        return "link model101 单实例链";
    }
}




