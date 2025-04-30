package xyz.yygqzzk.domain.trade.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.yygqzzk.domain.trade.model.entity.PayActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayDiscountEntity;
import xyz.yygqzzk.domain.trade.model.entity.UserEntity;
import xyz.yygqzzk.domain.trade.model.valobj.TradeOrderStatusEnumVO;

import java.math.BigDecimal;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团订单聚合对象；聚合可以理解用各个四肢、身体、头等组装出来一个人
 * @since 2025/4/29
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GroupBuyOrderAggregate {

    /** 用户实体对象 */
    private UserEntity userEntity;
    /** 支付活动实体对象 */
    private PayActivityEntity payActivityEntity;
    /** 支付优惠实体对象 */
    private PayDiscountEntity payDiscountEntity;
    /** 已参与拼团量 */
    private Integer userTakeOrderCount;


}




