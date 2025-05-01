package xyz.yygqzzk.domain.trade.service.settlement.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import xyz.yygqzzk.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description SC 渠道来源过滤 - 当某个签约渠道下架后，则不会记账
 * @since 2025/5/1
 */
@Service
@Slf4j
public class SCRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    ITradeRepository repository;


    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("结算规则过滤-渠道黑名单校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // sc 渠道黑名单拦截
        boolean intercept = repository.isSCBlackIntercept(requestParameter.getSource(), requestParameter.getChannel());
        if (intercept) {
            log.error("{}{} 渠道黑名单拦截", requestParameter.getSource(), requestParameter.getChannel());
            throw new AppException(ResponseCode.E0105);
        }


        return next(requestParameter, dynamicContext);
    }
}




