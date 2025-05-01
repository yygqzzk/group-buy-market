package xyz.yygqzzk.domain.trade.service.settlement.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
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
 * @description 外部交易单号过滤；外部交易单号是否为退单
 * @since 2025/5/1
 */
@Service
@Slf4j
public class OutTradeNoRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("结算规则过滤-外部单号校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // 查询拼团信息
        /* 查询对应的订单信息 */
        MarketPayOrderEntity marketPayOrderEntity = repository.queryNoPayMarketPayOrderByOutTradeNo(requestParameter.getUserId(), requestParameter.getOutTradeNo());
        if(null == marketPayOrderEntity){
            /* 订单关闭或退单 */
            log.info("不存在的外部交易单号或用户已退单，不需要做支付订单结算:{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());
            throw new AppException(ResponseCode.E0104);
        }

        dynamicContext.setMarketPayOrderEntity(marketPayOrderEntity);

        return next(requestParameter, dynamicContext);
    }
}




