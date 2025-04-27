package xyz.yygqzzk.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description 营销商品实体信息，通过这样一个信息获取商品优惠信息
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketProductEntity {
    /** 用户ID */
    private String userId;
    /** 商品ID */
    private String goodsId;
    /** 渠道 */
    private String source;
    /** 来源 */
    private String channel;
}




