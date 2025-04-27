package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyActivity;

import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @since 2025/4/26
 */
@Mapper
public interface IGroupBuyActivityDao {
    List<GroupBuyActivity> queryGroupBuyActivityList();

    GroupBuyActivity queryValidGroupBuyActivity(GroupBuyActivity groupBuyActivityReq);
}
