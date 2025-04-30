package xyz.yygqzzk.test.types.rules01.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.test.types.rules01.factory.Rule01TradeRuleFactory;
import xyz.yygqzzk.types.design.framework.link.model1.AbstractLogicLink;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Slf4j
@Service
public class RuleLogic101 extends AbstractLogicLink<String, Rule01TradeRuleFactory.DynamicContext, String> {
    @Override
    public String apply(String requestParameter, Rule01TradeRuleFactory.DynamicContext dynamicContext) throws Exception {
        log.info("link model101 RuleLogic101");
        return next(requestParameter, dynamicContext);
    }
}




