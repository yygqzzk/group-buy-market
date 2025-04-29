package xyz.yygqzzk.domain.activity.adapter.repository;

import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SCSkuActivityVO;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;

import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/27
 */
public interface IActivityRepository {
    SkuVO querySkuByGoodsId(String goodsId);
    GroupBuyActivityDiscountVO queryValidGroupBuyActivity(Long activityId);

    SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId);

    boolean isTagCrowdRange(String tagId, String userId);

    boolean downgradeSwitch();

    boolean cutRange(String userId);
}
