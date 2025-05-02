package xyz.yygqzzk.domain.trade.service;

import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayDiscountEntity;
import xyz.yygqzzk.domain.trade.model.entity.UserEntity;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * @author zzk
 * @version 1.0
 * @description 交易订单服务接口
 * @since 2025/4/29
 */
public interface ITradeLockOrderService {


    /* 锁单 */
    MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivity, PayDiscountEntity payDiscountEntity) throws Exception;

    /* 查询拼团进度 */
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    /* 查询用户锁单的对应订单信息 */
    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo);




}
