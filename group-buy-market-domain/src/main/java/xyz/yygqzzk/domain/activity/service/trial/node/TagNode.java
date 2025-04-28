package xyz.yygqzzk.domain.activity.service.trial.node;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.TagScopeEnum;
import xyz.yygqzzk.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import xyz.yygqzzk.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import xyz.yygqzzk.domain.tag.adapter.repository.ITagRepository;
import xyz.yygqzzk.domain.tag.service.ITagService;
import xyz.yygqzzk.types.design.framework.tree.AbstractMultiThreadStrategyRouter;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author zzk
 * @version 1.0
 * @description 人群标签判断
 * @since 2025/4/28
 */
@Service
@RequiredArgsConstructor
public class TagNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {
    private final EndNode endNode;


    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {

        /* 得到用户人群标签 */
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = dynamicContext.getGroupBuyActivityDiscountVO();

        String tagId = groupBuyActivityDiscountVO.getTagId();
        boolean enable = groupBuyActivityDiscountVO.isEnable();
        boolean visible = groupBuyActivityDiscountVO.isVisible();

        /* 未配置人群标签规则 */
        if(StringUtils.isBlank(tagId)) {
            dynamicContext.setVisible(TagScopeEnum.VISIBLE.getAllow());
            dynamicContext.setEnable(TagScopeEnum.VISIBLE.getAllow());
            router(requestParameter, dynamicContext);
        }

        boolean within = activityRepository.isTagCrowdRange(tagId, requestParameter.getUserId());

        /**
         *  如果该用户是目标标签人群用户，则可见并且可参与
         *  如果该用户不是目标标签人群用户，则根据活动的tag_scope来决定能否可见活动或者参与活动
         */
        dynamicContext.setVisible(within || visible);
        dynamicContext.setEnable(within || enable);


        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicParameter) throws Exception {
        return endNode;
    }
}




