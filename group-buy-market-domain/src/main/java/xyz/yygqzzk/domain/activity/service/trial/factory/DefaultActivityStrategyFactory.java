package xyz.yygqzzk.domain.activity.service.trial.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;
import xyz.yygqzzk.domain.activity.service.trial.node.RootNode;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;

/**
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 * @description 活动策略工厂
 */
@Service
public class DefaultActivityStrategyFactory {

    private final RootNode rootNode;

    public DefaultActivityStrategyFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    public StrategyHandler<MarketProductEntity, DynamicContext, TrialBalanceEntity> strategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;
        private SkuVO skuVO;
    }

}




