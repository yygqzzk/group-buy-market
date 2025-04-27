package xyz.yygqzzk.domain.activity.service;

import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;

/**
 * @author zzk
 * @version 1.0
 * @description 首页营销服务接口
 * @since 2025/4/27
 */
public interface IIndexGroupBuyMarketService {
    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;
}
