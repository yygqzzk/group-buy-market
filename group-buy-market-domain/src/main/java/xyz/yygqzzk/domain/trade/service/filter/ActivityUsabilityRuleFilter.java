package xyz.yygqzzk.domain.trade.service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeRuleCommandEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeRuleFilterEntity;
import xyz.yygqzzk.domain.trade.service.factory.TradeRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;
import xyz.yygqzzk.types.enums.ActivityStatusEnumVO;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Slf4j
@Service
public class ActivityUsabilityRuleFilter implements ILogicHandler<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterEntity> {

    @Resource
    private ITradeRepository repository;


    @Override
    public TradeRuleFilterEntity apply(TradeRuleCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("交易规则过滤-活动的可用性校验{} activityId: {}", requestParameter, requestParameter.getActivityId());
        // 查询拼团活动
        GroupBuyActivityEntity groupBuyActivityEntity = repository.queryGroupBuyActivityByActivityId(requestParameter.getActivityId());

        // 校验；活动状态 - 可以抛业务异常code，或者把code写入到动态上下文dynamicContext中，最后获取。
        if(!ActivityStatusEnumVO.EFFECTIVE.equals(groupBuyActivityEntity.getStatus())) {
            log.info("活动的可用性校验，非生效状态 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0101);
        }

        // 校验；活动时间
        Date currentTime = new Date();
        if(currentTime.before(groupBuyActivityEntity.getStartTime()) || currentTime.after(groupBuyActivityEntity.getEndTime())) {
            log.info("活动的可用性校验，非可参与时间范围 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0102);
        }


        // 写入动态上下文
        dynamicContext.setGroupBuyActivity(groupBuyActivityEntity);

        return next(requestParameter, dynamicContext);
    }
}




