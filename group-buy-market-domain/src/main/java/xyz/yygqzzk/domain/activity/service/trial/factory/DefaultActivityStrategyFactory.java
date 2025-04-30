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

import java.math.BigDecimal;

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
        // 拼团活动营销配置值对象
        private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;
        // 商品信息
        private SkuVO skuVO;
        /* 折扣金额 */
        private BigDecimal deductionPrice;
        /* 最终支付金额 */
        private BigDecimal payPrice;
        // 活动可见性限制
        private Boolean visible;
        // 活动参与限制
        private Boolean enable;

    }

}




