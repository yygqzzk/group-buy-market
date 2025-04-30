package xyz.yygqzzk.test.types.rules02.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.test.types.rules02.factory.Rule02TradeRuleFactory;
import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Slf4j
@Service
public class RuleLogic202 implements ILogicHandler<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> {

    @Override
    public XxxResponse apply(String requestParameter, Rule02TradeRuleFactory.DynamicContext dynamicContext) throws Exception {
        log.info("link model02 RuleLogic202");
        return new XxxResponse("End.");
    }
}




