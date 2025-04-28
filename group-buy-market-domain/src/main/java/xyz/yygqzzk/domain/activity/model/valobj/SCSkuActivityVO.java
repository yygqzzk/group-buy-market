package xyz.yygqzzk.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SCSkuActivityVO {
    /**
     * 来源
     */
    private String source;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 活动id
     */
    private Long activityId;
}




