package xyz.yygqzzk.domain.activity.service.trial.node;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;
import xyz.yygqzzk.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import xyz.yygqzzk.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import xyz.yygqzzk.domain.activity.service.trial.thread.QueryGroupBuyActivityDiscountVOThreadTask;
import xyz.yygqzzk.domain.activity.service.trial.thread.QuerySkuFromDBThreadTask;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;

import java.util.concurrent.*;

/**
 * @author zzk
 * @version 1.0
 * @description 营销优惠节点
 * @since 2025/4/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {
    private final ThreadPoolExecutor threadPoolExecutor;

    private final EndNode endNode;


    @Override
    protected void multiThread(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        // 异步查询活动配置
        QueryGroupBuyActivityDiscountVOThreadTask queryGroupBuyActivityDiscountVOThreadTask = new QueryGroupBuyActivityDiscountVOThreadTask(requestParameter.getSource(), requestParameter.getChannel(), activityRepository);
        FutureTask<GroupBuyActivityDiscountVO> groupBuyActivityDiscountVOFutureTask = new FutureTask<>(queryGroupBuyActivityDiscountVOThreadTask);
        threadPoolExecutor.execute(groupBuyActivityDiscountVOFutureTask);
        // 异步查询商品信息 - 在实际生产中，商品有同步库或者调用接口查询。这里暂时使用DB方式查询。
        QuerySkuFromDBThreadTask querySkuFromDBThreadTask = new QuerySkuFromDBThreadTask(requestParameter.getGoodsId(), activityRepository);
        FutureTask<SkuVO> skuVOFutureTask = new FutureTask<>(querySkuFromDBThreadTask);

        threadPoolExecutor.execute(skuVOFutureTask);

        // 写入上下文 - 对于一些复杂场景，获取数据的操作，有时候会在下N个节点获取，这样前置查询数据，可以提高接口响应效率
        dynamicContext.setSkuVO(skuVOFutureTask.get(timeout, TimeUnit.MINUTES));
        dynamicContext.setGroupBuyActivityDiscountVO(groupBuyActivityDiscountVOFutureTask.get(timeout, TimeUnit.MINUTES));
    }

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicParameter) throws Exception {
        return endNode;
    }


}




