package xyz.yygqzzk.domain.trade.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团进度值对象
 * @since 2025/4/29
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyProgressVO {
    /** 目标数量 */
    private Integer targetCount;
    /** 完成数量 */
    private Integer completeCount;
    /** 锁单数量 */
    private Integer lockCount;
}




