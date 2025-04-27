package xyz.yygqzzk.domain.activity.service.trial.node;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import xyz.yygqzzk.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 * @description 开关节点
 */
@Slf4j
@Service
public class SwitchRoot extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private MarketNode marketNode;

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicParameter) throws Exception {
        return marketNode;
    }
}




