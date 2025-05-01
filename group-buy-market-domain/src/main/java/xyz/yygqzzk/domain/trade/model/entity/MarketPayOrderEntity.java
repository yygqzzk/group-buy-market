package xyz.yygqzzk.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.yygqzzk.domain.trade.model.valobj.TradeOrderStatusEnumVO;

import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团，预购订单营销实体对象
 * @since 2025/4/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPayOrderEntity {

    /* 拼团组队Id */
    private String teamId;
    /** 预购订单ID */
    private String orderId;
    /** 折扣金额 */
    private BigDecimal deductionPrice;
    /** 交易订单状态枚举 */
    private TradeOrderStatusEnumVO tradeOrderStatusEnumVO;

}




