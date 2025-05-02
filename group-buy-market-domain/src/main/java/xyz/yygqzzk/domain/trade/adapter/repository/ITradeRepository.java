package xyz.yygqzzk.domain.trade.adapter.repository;

import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.GroupBuyTeamEntity;
import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
import xyz.yygqzzk.domain.trade.model.entity.NotifyTaskEntity;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;

import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/29
 */
public interface ITradeRepository {

    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo);

    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId);

    Integer queryOrderCountByActivityId(Long activityId, String userId);

    GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId);

    boolean settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate);

    boolean isSCBlackIntercept(String source, String channel);

    // 查询所有未执行回调通知的回调任务
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList();

    // 查询指定team的回调任务
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList(String teamId);

    /* 更新notify_task数据库状态 */
    int updateNotifyTaskStatusSuccess(String teamId);

    int updateNotifyTaskStatusError(String teamId);

    int updateNotifyTaskStatusRetry(String teamId);
}
