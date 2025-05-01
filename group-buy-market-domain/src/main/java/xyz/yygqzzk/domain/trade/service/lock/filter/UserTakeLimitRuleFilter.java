package xyz.yygqzzk.domain.trade.service.lock.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeLockRuleCommandEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeLockRuleFilterEntity;
import xyz.yygqzzk.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Service
@Slf4j
public class UserTakeLimitRuleFilter implements ILogicHandler<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext, TradeLockRuleFilterEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeLockRuleFilterEntity apply(TradeLockRuleCommandEntity requestParameter, TradeLockRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("交易规则过滤-用户参与次数校验{} activityId: {}", requestParameter, requestParameter.getActivityId());

        GroupBuyActivityEntity groupBuyActivity = dynamicContext.getGroupBuyActivity();

        /* 查询用户在活动中的参与次数 */
        Integer count = repository.queryOrderCountByActivityId(requestParameter.getActivityId(), requestParameter.getUserId());

        if(null != groupBuyActivity.getTakeLimitCount() && count >= groupBuyActivity.getTakeLimitCount()){
            /* 营销活动有参与限制，并且参与次数已超过参与限制 */
            throw new AppException(ResponseCode.E0103);
        }

        return TradeLockRuleFilterEntity.builder()
                .userTakeOrderCount(count)
                .build();
    }
}




