package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrderList;

/**
 * @author zzk
 * @version 1.0
 * @description 用户拼单明细
 * @since 2025/4/29
 */
@Mapper
public interface IGroupBuyOrderListDao {

    void insert(GroupBuyOrderList groupBuyOrderListReq);

    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);


    Integer queryOrderCountBy(Long activityId, String userId);
}




