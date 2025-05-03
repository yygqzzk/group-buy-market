package xyz.yygqzzk.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 营销支付锁单应答对象
 * @since 2025/4/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockMarketPayOrderResponseDTO {

    /** 预购订单ID */
    private String orderId;
    /* 原始金额 */
    private BigDecimal originalPrice;
    /** 折扣金额 */
    private BigDecimal deductionPrice;
    /* 支付金额 */
    private BigDecimal payPrice;
    /** 交易订单状态 */
    private Integer tradeOrderStatus;
}




