package xyz.yygqzzk.domain.activity.service.discount;

import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 折扣计算服务
 * @since 2025/4/27
 */
public interface IDiscountCalculateService {

    /**
     * 折扣计算
     *
     * @param userId           用户ID
     * @param originalPrice    商品原始价格
     * @param groupBuyDiscount 折扣计划配置
     * @return 商品优惠价格
     */
    BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);
}
