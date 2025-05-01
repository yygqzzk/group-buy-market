package xyz.yygqzzk.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.yygqzzk.types.enums.GroupBuyOrderEnumVO;

import java.util.Date;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团交易结算规则反馈
 * @since 2025/5/1
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeSettlementRuleFilterBackEntity {

    /** 拼单组队ID */
    private String teamId;
    /** 活动ID */
    private Long activityId;
    /** 目标数量 */
    private Integer targetCount;
    /** 完成数量 */
    private Integer completeCount;
    /** 锁单数量 */
    private Integer lockCount;
    /** 状态（0-拼单中、1-完成、2-失败） */
    private GroupBuyOrderEnumVO status;
    /** 拼团开始时间 - 参与拼团时间 */
    private Date validStartTime;
    /** 拼团结束时间 - 拼团有效时长 */
    private Date validEndTime;


}




