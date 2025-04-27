package xyz.yygqzzk.domain.activity.service.trial.thread;

import lombok.RequiredArgsConstructor;
import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

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
    private final IActivityRepository activityRepository;

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        return activityRepository.queryValidGroupBuyActivity(source, channel);
    }
}




