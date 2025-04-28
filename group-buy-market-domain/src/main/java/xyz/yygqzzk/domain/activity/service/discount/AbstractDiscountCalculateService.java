package xyz.yygqzzk.domain.activity.service.discount;

import xyz.yygqzzk.domain.activity.model.valobj.DiscountTypeEnum;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 折扣计算服务抽象类
 * @since 2025/4/27
 */
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{
    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())) {
            boolean isCrowRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if(!isCrowRange) return originalPrice;
        }
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    public abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

    private boolean filterTagId(String userId, String tagId) {
        return true;
    }
}




