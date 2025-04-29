package xyz.yygqzzk.domain.trade.service;

import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayDiscountEntity;
import xyz.yygqzzk.domain.trade.model.entity.UserEntity;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description 订单交易服务
 * @since 2025/4/29
 */
@Service
public class TradeOrderServiceImpl implements ITradeOrderService {
    @Resource
    private ITradeRepository repository;


    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivity, PayDiscountEntity payDiscountEntity) {

        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivity)
                .payDiscountEntity(payDiscountEntity)
                .build();



        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        return repository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        return repository.queryNoPayMarketPayOrderByOutTradeNo(userId, outTradeNo);
    }
}




