package xyz.yygqzzk.domain.activity.service.trial.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;
import xyz.yygqzzk.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import xyz.yygqzzk.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author zzk
 * @version 1.0
 * @description 结束节点
 * @since 2025/4/27
 */
@Slf4j
@Service
public class EndNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        SkuVO skuVO = dynamicContext.getSkuVO();
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = dynamicContext.getGroupBuyActivityDiscountVO();
        // 返回空结果
        return TrialBalanceEntity.builder()
                .goodsId(skuVO.getGoodsId())
                .goodsName(skuVO.getGoodsName())
                .originalPrice(skuVO.getOriginalPrice())
                .deductionPrice(new BigDecimal("0.00"))
                .targetCount(groupBuyActivityDiscountVO.getTarget())
                .startTime(groupBuyActivityDiscountVO.getStartTime())
                .endTime(groupBuyActivityDiscountVO.getEndTime())
                .isVisible(false)
                .isEnable(false)
                .build();
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicParameter) throws Exception {
        return defaultStrategyHandler;
    }
}




