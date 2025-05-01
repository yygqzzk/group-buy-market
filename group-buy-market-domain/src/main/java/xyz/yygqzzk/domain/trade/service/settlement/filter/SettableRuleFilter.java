package xyz.yygqzzk.domain.trade.service.settlement.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyTeamEntity;
import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import xyz.yygqzzk.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.handler.ILogicHandler;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description 可结算规则过滤；交易时间
 * @since 2025/5/1
 */
@Service
@Slf4j
public class SettableRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("结算规则过滤-有效时间校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // 上下文；获取数据
        MarketPayOrderEntity marketPayOrderEntity = dynamicContext.getMarketPayOrderEntity();

        // 查询拼团信息
        GroupBuyTeamEntity groupBuyTeamEntity = repository.queryGroupBuyTeamByTeamId(marketPayOrderEntity.getTeamId());

        // 外部交易时间 - 也就是用户支付完成的时间，这个时间要在拼团有效时间范围内
        Date outTradeTime = requestParameter.getOutTradeTime();

        // 判断，外部交易时间，要小于拼团结束时间。否则抛异常。
        if (!outTradeTime.before(groupBuyTeamEntity.getValidEndTime())) {
            log.error("订单交易时间不在拼团有效时间范围内");
            throw new AppException(ResponseCode.E0106);
        }

        // 设置上下文
        dynamicContext.setGroupBuyTeamEntity(groupBuyTeamEntity);

        return next(requestParameter, dynamicContext);
    }
}




