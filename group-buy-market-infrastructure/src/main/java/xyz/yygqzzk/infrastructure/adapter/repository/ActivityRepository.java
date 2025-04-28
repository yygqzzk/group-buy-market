package xyz.yygqzzk.infrastructure.adapter.repository;

import org.springframework.stereotype.Repository;
import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.valobj.DiscountTypeEnum;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyActivityDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyDiscountDao;
import xyz.yygqzzk.infrastructure.dao.ISkuDao;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyActivity;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyDiscount;
import xyz.yygqzzk.infrastructure.dao.po.Sku;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/27
 */
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;
    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;
    @Resource
    private ISkuDao skuDao;

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        Sku sku = skuDao.querySkuByGoodsId(goodsId);
        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public GroupBuyActivityDiscountVO queryValidGroupBuyActivity(String source, String channel) {
        GroupBuyActivity groupBuyActivityReq = GroupBuyActivity.builder()
                .source(source)
                .channel(channel)
                .build();
        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryValidGroupBuyActivity(groupBuyActivityReq);
        String discountId = groupBuyActivityRes.getDiscountId();
        GroupBuyDiscount groupBuyDiscountRes = groupBuyDiscountDao.queryGroupBuyActivityDiscountByDiscountId(discountId);

        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                .discountName(groupBuyDiscountRes.getDiscountName())
                .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                .discountType(DiscountTypeEnum.get(groupBuyDiscountRes.getDiscountType()))
                .marketPlan(groupBuyDiscountRes.getMarketPlan())
                .marketExpr(groupBuyDiscountRes.getMarketExpr())
                .tagId(groupBuyDiscountRes.getTagId())
                .build();


        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .source(groupBuyActivityRes.getSource())
                .channel(groupBuyActivityRes.getChannel())
                .goodsId(groupBuyActivityRes.getGoodsId())
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(groupBuyActivityRes.getGroupType())
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(groupBuyActivityRes.getStatus())
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope())
                .build();
    }
}




