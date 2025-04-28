package xyz.yygqzzk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SCSkuActivity {
    /**
     * id
     */
    private Long id;
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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}




