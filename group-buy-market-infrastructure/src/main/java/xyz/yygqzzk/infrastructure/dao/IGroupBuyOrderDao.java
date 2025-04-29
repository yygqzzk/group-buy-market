package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrder;

/**
 * @author zzk
 * @version 1.0
 * @description 用户拼单
 * @since 2025/4/29
 */
@Mapper
public interface IGroupBuyOrderDao {

    void insert(GroupBuyOrder groupBuyOrder);

    int updateAddLockCount(String teamId);

    int updateSubtractionLockCount(String teamId);

    GroupBuyOrder queryGroupBuyProgress(String teamId);

}
