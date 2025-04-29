package xyz.yygqzzk.domain.activity.service.discount.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.service.discount.AbstractDiscountCalculateService;
import xyz.yygqzzk.types.common.Constants;

import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 折扣优惠计算
 * @since 2025/4/27
 */
@Slf4j
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {
    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getMarketPlan());

        // 折扣表达式 - 100,10 满100减10元
        String marketExpr = groupBuyDiscount.getMarketExpr();
        String[] split = marketExpr.split(Constants.SPLIT);
        BigDecimal x = new BigDecimal(split[0].trim());
        BigDecimal y = new BigDecimal(split[1].trim());

        // 不满足最低满减约束，则按照原价
        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }

        // 折扣价格
        BigDecimal deductionPrice = originalPrice.subtract(y);

        // 判断折扣后金额，最低支付1分钱
        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal(Constants.MIN_PRICE);
        }

        return deductionPrice;
    }

}




