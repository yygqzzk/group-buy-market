package xyz.yygqzzk.domain.activity.service.trial;

import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.types.design.framework.tree.AbstractMultiThreadStrategyRouter;
import xyz.yygqzzk.types.design.framework.tree.AbstractStrategyRouter;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 * @description 抽象的拼团营销支撑类
 */
public abstract class AbstractGroupBuyMarketSupport<ProductMarketEntity, DynamicContext, TrialBalanceEntity> extends AbstractMultiThreadStrategyRouter<ProductMarketEntity, DynamicContext, TrialBalanceEntity> {
    @Resource
    protected IActivityRepository activityRepository;

    protected long timeout = 500L;

    @Override
    protected void multiThread(ProductMarketEntity requestParameter, DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }
}




