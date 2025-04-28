package xyz.yygqzzk.domain.activity.service.trial.thread;

import lombok.RequiredArgsConstructor;
import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.SCSkuActivityVO;

import java.util.concurrent.Callable;

/**
 * @author zzk
 * @version 1.0
 * @description 查询营销配置任务
 * @since 2025/4/27
 */
@RequiredArgsConstructor
public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {

    private final String source;
    private final String channel;
    private final String goodsId;
    private final IActivityRepository activityRepository;

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        // 查询渠道商品活动配置关联配置
        SCSkuActivityVO scSkuActivityVO = activityRepository.querySCSkuActivityBySCGoodsId(source, channel, goodsId);
        /* 无营销活动配置 */
        if(scSkuActivityVO == null) {
            return null;
        }
        // 查询活动配置
        return activityRepository.queryValidGroupBuyActivity(scSkuActivityVO.getActivityId());
    }
}




