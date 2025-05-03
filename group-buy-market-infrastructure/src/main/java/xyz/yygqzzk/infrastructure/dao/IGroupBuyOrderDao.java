package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrder;

import java.util.List;
import java.util.Set;

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
    GroupBuyOrder queryGroupBuyByTeamId(String teamId);

    int updateAddCompleteCount(String teamId);

    int updateOrderStatus2COMPLETE(String teamId);

    List<GroupBuyOrder> queryGroupBuyProgressByTeamIds(Set<String> teamIds);

    Integer queryAllTeamCount(Set<String> teamIds);

    Integer queryAllTeamCompleteCount(Set<String> teamIds);

    Integer queryAllUserCount(Set<String> teamIds);
}
