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
 * @description 直减
 * @since 2025/4/27
 */
@Slf4j
@Service("ZJ")
public class ZJCalculateService extends AbstractDiscountCalculateService {
    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {

        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 折扣表达式 - 直减为扣减金额
        String marketExpr = groupBuyDiscount.getMarketExpr();

        // 折扣价格
        BigDecimal deductionPrice = originalPrice.subtract(new BigDecimal(marketExpr));

        // 判断折扣后金额，最低支付1分钱
        if (deductionPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal(Constants.MIN_PRICE);
        }

        return deductionPrice;
    }
}




