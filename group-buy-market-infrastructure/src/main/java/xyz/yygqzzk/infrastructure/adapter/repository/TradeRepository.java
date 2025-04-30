package xyz.yygqzzk.infrastructure.adapter.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import xyz.yygqzzk.domain.trade.model.entity.*;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;
import xyz.yygqzzk.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyActivityDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyOrderDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyOrderListDao;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyActivity;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrder;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyOrderList;
import xyz.yygqzzk.types.common.Constants;
import xyz.yygqzzk.types.enums.ActivityStatusEnumVO;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

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
                    .build();

            groupBuyOrderDao.insert(groupBuyOrder);
        } else {
            /* 加入已有拼团 */
            int count = groupBuyOrderDao.updateAddLockCount(teamId);
            if (1 != count) {
                /* 拼团人员已满 */
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
}




