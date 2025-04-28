package xyz.yygqzzk.infrastructure.adapter.repository;

import org.redisson.api.RBitSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.valobj.DiscountTypeEnum;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SCSkuActivityVO;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyActivityDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyDiscountDao;
import xyz.yygqzzk.infrastructure.dao.ISCSkuActivityDao;
import xyz.yygqzzk.infrastructure.dao.ISkuDao;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyActivity;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyDiscount;
import xyz.yygqzzk.infrastructure.dao.po.SCSkuActivity;
import xyz.yygqzzk.infrastructure.dao.po.Sku;
import xyz.yygqzzk.infrastructure.redis.RedissonService;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;
import java.util.List;

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
    private ISCSkuActivityDao scSkuActivityDao;
    @Resource
    private ISkuDao skuDao;
    @Autowired
    private RedissonService redissonService;

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        Sku sku = skuDao.querySkuByGoodsId(goodsId);
        if(null == sku)
            return null;
        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public GroupBuyActivityDiscountVO queryValidGroupBuyActivity(Long activityId) {

        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryValidGroupBuyActivity(activityId);

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

    @Override
    public SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId) {
        SCSkuActivity scSkuActivityReq = SCSkuActivity.builder()
                .source(source)
                .channel(channel)
                .goodsId(goodsId)
                .build();

        SCSkuActivity scSkuActivityRes = scSkuActivityDao.querySCSkuActivityBySCGoodsId(scSkuActivityReq);
        if(null == scSkuActivityRes) {
            return null;
        }
        SCSkuActivityVO scSkuActivityVO = new SCSkuActivityVO();
        scSkuActivityVO.setSource(scSkuActivityRes.getSource());
        scSkuActivityVO.setChannel(scSkuActivityRes.getChannel());
        scSkuActivityVO.setGoodsId(scSkuActivityRes.getGoodsId());
        scSkuActivityVO.setActivityId(scSkuActivityRes.getActivityId());
        return scSkuActivityVO;
    }

    @Override
    public boolean isTagCrowdRange(String tagId, String userId) {
        /* 在人群标签批次任务中，已经为人群标签创建了redis bitSet, 每当为一个用户添加一个标签时， 会在对应标签的bitSet中计算出用户 userId的下标位置并赋值
        *   故判断用户是否属于某一个人群，只需要从redis 对应人群标签的bitMap中判断
        * */

        RBitSet bitSet = redissonService.getBitSet(tagId);
        if(!bitSet.isExists()) {
            return true;
        }


        return bitSet.get(redissonService.getIndexFromUserId(userId));
    }


}




