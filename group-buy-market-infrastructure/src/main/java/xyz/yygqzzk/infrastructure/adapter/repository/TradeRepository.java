package xyz.yygqzzk.infrastructure.adapter.repository;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import xyz.yygqzzk.domain.trade.model.entity.*;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;
import xyz.yygqzzk.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyActivityDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyOrderDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyOrderListDao;
import xyz.yygqzzk.infrastructure.dao.INotifyTaskDao;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyActivity;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrder;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrderList;
import xyz.yygqzzk.infrastructure.dao.po.NotifyTask;
import xyz.yygqzzk.infrastructure.dcc.DCCService;
import xyz.yygqzzk.types.common.Constants;
import xyz.yygqzzk.types.enums.ActivityStatusEnumVO;
import xyz.yygqzzk.types.enums.GroupBuyOrderEnumVO;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/29
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeRepository implements ITradeRepository {
    private final IGroupBuyOrderDao groupBuyOrderDao;
    private final IGroupBuyOrderListDao groupBuyOrderListDao;
    private final IGroupBuyActivityDao groupBuyActivityDao;
    private final INotifyTaskDao notifyTaskDao;
    private final DCCService dccService;


    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyProgress(teamId);

        if (groupBuyOrder == null) {
            return null;
        }

        return GroupBuyProgressVO.builder()
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .build();
    }

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userId)
                .outTradeNo(outTradeNo)
                .build();
        GroupBuyOrderList groupBuyOrderListRes = groupBuyOrderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);

        if (null == groupBuyOrderListRes) {
            return null;
        }

        return MarketPayOrderEntity.builder()
                .teamId(groupBuyOrderListRes.getTeamId())
                .orderId(groupBuyOrderListRes.getOrderId())
                .deductionPrice(groupBuyOrderListRes.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.valueOf(groupBuyOrderListRes.getStatus()))
                .build();
    }

    @Override
    @Transactional(timeout = 500)
    public MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate) {

        PayActivityEntity payActivityEntity = groupBuyOrderAggregate.getPayActivityEntity();
        UserEntity userEntity = groupBuyOrderAggregate.getUserEntity();
        PayDiscountEntity payDiscountEntity = groupBuyOrderAggregate.getPayDiscountEntity();

        String teamId = payActivityEntity.getTeamId();

        if (StringUtils.isBlank(teamId)) {
            /* 首次拼团 */
            // 使用 RandomStringUtils.randomNumeric 替代公司里使用的雪花算法UUID
            teamId = RandomStringUtils.randomNumeric(8);

            /* 计算拼团开始时间和结束时间 */
            Date validStartTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(validStartTime);
            calendar.add(Calendar.MINUTE, payActivityEntity.getValidTime());

            // 构建拼团订单
            GroupBuyOrder groupBuyOrder = GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivityEntity.getActivityId())
                    .source(payDiscountEntity.getSource())
                    .channel(payDiscountEntity.getChannel())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .payPrice(payDiscountEntity.getPayPrice())
                    .targetCount(payActivityEntity.getTargetCount())
                    .completeCount(0)
                    .lockCount(1)
                    .validStartTime(validStartTime)
                    .validEndTime(calendar.getTime())
                    .build();

            groupBuyOrderDao.insert(groupBuyOrder);
        } else {
            /* 加入已有拼团 */
            int count = groupBuyOrderDao.updateAddLockCount(teamId);
            if (1 != count) {
                /* 拼团人员已满 */
                log.info("拼团失败，拼团人员已满");
                throw new AppException(ResponseCode.E0005);
            }
        }
        Integer takeCount = groupBuyOrderAggregate.getUserTakeOrderCount() + 1;

        // 使用 RandomStringUtils.randomNumeric 替代公司里使用的雪花算法UUID
        String orderId = RandomStringUtils.randomNumeric(12);

        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userEntity.getUserId())
                .teamId(teamId)
                .orderId(orderId)
                .activityId(payActivityEntity.getActivityId())
                .startTime(payActivityEntity.getStartTime())
                .endTime(payActivityEntity.getEndTime())
                .goodsId(payDiscountEntity.getGoodsId())
                .source(payDiscountEntity.getSource())
                .channel(payDiscountEntity.getChannel())
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .status(TradeOrderStatusEnumVO.CREATE.getCode())
                .outTradeNo(payDiscountEntity.getOutTradeNo())
                .bizId(payActivityEntity.getActivityId() + Constants.UNDERLINE + userEntity.getUserId() + Constants.UNDERLINE + takeCount)
                .build();

        try {
            groupBuyOrderListDao.insert(groupBuyOrderListReq);
        } catch (DuplicateKeyException e) {
            throw new AppException(ResponseCode.INDEX_EXCEPTION);
        }


        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .build();
    }

    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId) {
        GroupBuyActivity groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);


        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .discountId(groupBuyActivity.getDiscountId())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(ActivityStatusEnumVO.valueOf(groupBuyActivity.getStatus()))
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();
    }

    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {

        return groupBuyOrderListDao.queryOrderCountBy(activityId, userId);
    }

    @Override
    public GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId) {
        assert teamId != null;
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyByTeamId(teamId);

        return GroupBuyTeamEntity.builder()
                .teamId(groupBuyOrder.getTeamId())
                .activityId(groupBuyOrder.getActivityId())
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .status(GroupBuyOrderEnumVO.valueOf(groupBuyOrder.getStatus()))
                .validStartTime(groupBuyOrder.getValidStartTime())
                .validEndTime(groupBuyOrder.getValidEndTime())
                .build();
    }

    @Transactional(timeout = 500)
    @Override
    public void settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate) {
        GroupBuyTeamEntity groupBuyTeamEntity = groupBuyTeamSettlementAggregate.getGroupBuyTeamEntity();
        UserEntity userEntity = groupBuyTeamSettlementAggregate.getUserEntity();
        TradePaySuccessEntity tradePaySuccessEntity = groupBuyTeamSettlementAggregate.getTradePaySuccessEntity();

        // 1. 更新拼团订单明细状态
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userEntity.getUserId());
        groupBuyOrderListReq.setOutTradeNo(tradePaySuccessEntity.getOutTradeNo());
        groupBuyOrderListReq.setOutTradeTime(tradePaySuccessEntity.getOutTradeTime());

        /* 2. 更新订单明细的支付状态*/
        Integer updateOrderListStatusCount = groupBuyOrderListDao.updateOrderStatus2COMPLETE(groupBuyOrderListReq);

        if (1 != updateOrderListStatusCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        /* 3. 更新拼团达成数量 */
        int updateAddCount = groupBuyOrderDao.updateAddCompleteCount(groupBuyTeamEntity.getTeamId());
        if (1 != updateAddCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }


        if(groupBuyTeamEntity.getTargetCount() - groupBuyTeamEntity.getCompleteCount() == 1) {
            /* 若当前订单是最后一个完成交易的订单，即拼团成功 */
            /* 更新整个拼单订单得状态 */
            int updateOrderStatusCount = groupBuyOrderDao.updateOrderStatus2COMPLETE(groupBuyTeamEntity.getTeamId());

            if (1 != updateOrderStatusCount) {
                throw new AppException(ResponseCode.UPDATE_ZERO);
            }

            /* 查询所有有效的完成拼团的外部订单号 */
            List<String> outTradeNoList = groupBuyOrderListDao.queryGroupBuyCompleteOrderOutTradeNOListByTeamId(groupBuyTeamEntity.getTeamId());

            /*4. 写入回调任务表 */
            NotifyTask notifyTask = new NotifyTask();

            notifyTask.setActivityId(groupBuyTeamEntity.getActivityId());
            notifyTask.setTeamId(groupBuyTeamEntity.getTeamId());
            notifyTask.setNotifyUrl("暂无");
            notifyTask.setNotifyCount(0);
            notifyTask.setNotifyStatus(0);
            notifyTask.setParameterJson(JSON.toJSONString(new HashMap<String, Object>() { {
                put("teamId", notifyTask.getTeamId());
                put("outTradeNoList", outTradeNoList.toString());
            }
            }));

            notifyTaskDao.insert(notifyTask);

        }

    }

    @Override
    public boolean isSCBlackIntercept(String source, String channel) {
        return dccService.isSCBlackIntercept(source, channel);
    }
}




