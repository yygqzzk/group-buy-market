package xyz.yygqzzk.domain.activity.service.trial.thread;

import lombok.RequiredArgsConstructor;
import xyz.yygqzzk.domain.activity.adapter.repository.IActivityRepository;
import xyz.yygqzzk.domain.activity.model.valobj.SkuVO;

import java.util.concurrent.Callable;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/27
 */
@RequiredArgsConstructor
public class QuerySkuFromDBThreadTask implements Callable<SkuVO> {
    private final String goodsId;
    private final IActivityRepository activityRepository;
    @Override
    public SkuVO call() throws Exception {
        return activityRepository.querySkuByGoodsId(goodsId);
    }
}




