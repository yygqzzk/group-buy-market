package xyz.yygqzzk.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description 商品营销请求对象
 * @since 2025/5/2
 */
@Data
public class SettlementMarketPayOrderRequestDTO {
    /** 渠道 */
    private String source;
    /** 来源 */
    private String channel;
    /** 用户ID */
    private String userId;
    /** 外部交易单号 */
    private String outTradeNo;
    /** 外部交易时间 */
    private Date outTradeTime;

}




