package xyz.yygqzzk.domain.activity.service.discount;

import lombok.extern.slf4j.Slf4j;
import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.valobj.DiscountTypeEnum;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.tag.adapter.repository.ITagRepository;
import xyz.yygqzzk.domain.tag.service.ITagService;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 折扣计算服务抽象类
 * @since 2025/4/27
 */
@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Resource
    protected IActivityRepository repository;

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {

        /* 人群标签过滤 */
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())) {
            boolean isCrowRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if(!isCrowRange) {
                log.info("折扣优惠计算拦截，用户不在优惠人群标签范围内 userId:{}", userId);
                return originalPrice;
            }
        }

        /* 折扣优惠计算 */
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    public abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

    private boolean filterTagId(String userId, String tagId) {
        return repository.isTagCrowdRange(tagId, userId);
    }
}




