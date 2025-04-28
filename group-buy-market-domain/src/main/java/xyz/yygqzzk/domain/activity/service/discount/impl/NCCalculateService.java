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
 * @description n元购
 * @since 2025/4/27
 */
@Slf4j
@Service("N")
public class NCCalculateService extends AbstractDiscountCalculateService {
    @Override
    public BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 折扣表达式 - 直接为优惠后的金额
        String marketExpr = groupBuyDiscount.getMarketExpr();
        // n元购
        return new BigDecimal(marketExpr);
    }
}




