package xyz.yygqzzk.domain.trade.service.settlement;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.port.ITradePort;
import xyz.yygqzzk.domain.trade.adapter.repository.ITradeRepository;
import xyz.yygqzzk.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import xyz.yygqzzk.domain.trade.model.entity.*;
import xyz.yygqzzk.domain.trade.service.ITradeSettlementOrderService;
import xyz.yygqzzk.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import xyz.yygqzzk.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;
import xyz.yygqzzk.types.enums.NotifyTaskHTTPEnumVO;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzk
 * @version 1.0
 * @description 拼团交易结算服务
 * @since 2025/4/30
 */
@Slf4j
@Service
public class TradeSettlementOrderServiceImpl implements ITradeSettlementOrderService {

    @Resource
    private ITradeRepository repository;

    @Resource
    private ITradePort port;


    @Resource
    private BusinessLinkedList<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> tradeSettlementRuleFilter;

    @Override
    public TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception {
        log.info("拼团交易-支付订单结算:{} outTradeNo:{}", tradePaySuccessEntity.getUserId(), tradePaySuccessEntity.getOutTradeNo());

        /* 通过责任链来规则过滤: SC渠道管控、有效外部交易单号、交易时间有效性 */
        // 1. 结算规则过滤
        TradeSettlementRuleCommandEntity tradeSettlementRuleCommandEntity = TradeSettlementRuleCommandEntity.builder().source(tradePaySuccessEntity.getSource()).channel(tradePaySuccessEntity.getChannel()).userId(tradePaySuccessEntity.getUserId()).outTradeNo(tradePaySuccessEntity.getOutTradeNo()).outTradeTime(tradePaySuccessEntity.getOutTradeTime()).build();

        TradeSettlementRuleFilterBackEntity tradeSettlementRuleFilterBackEntity = tradeSettlementRuleFilter.apply(tradeSettlementRuleCommandEntity, new TradeSettlementRuleFilterFactory.DynamicContext());

        String teamId = tradeSettlementRuleFilterBackEntity.getTeamId();

        // 2. 查询组团信息
        GroupBuyTeamEntity groupBuyTeamEntity = GroupBuyTeamEntity.builder().teamId(tradeSettlementRuleFilterBackEntity.getTeamId()).activityId(tradeSettlementRuleFilterBackEntity.getActivityId()).targetCount(tradeSettlementRuleFilterBackEntity.getTargetCount()).completeCount(tradeSettlementRuleFilterBackEntity.getCompleteCount()).lockCount(tradeSettlementRuleFilterBackEntity.getLockCount()).status(tradeSettlementRuleFilterBackEntity.getStatus()).validStartTime(tradeSettlementRuleFilterBackEntity.getValidStartTime()).validEndTime(tradeSettlementRuleFilterBackEntity.getValidEndTime()).notifyUrl(tradeSettlementRuleFilterBackEntity.getNotifyUrl()).build();


        // 3. 构建聚合对象
        GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate = GroupBuyTeamSettlementAggregate.builder().userEntity(UserEntity.builder().userId(tradePaySuccessEntity.getUserId()).build()).groupBuyTeamEntity(groupBuyTeamEntity).tradePaySuccessEntity(tradePaySuccessEntity).build();


        // 4. 拼团交易结算
        boolean isNotify = repository.settlementMarketPayOrder(groupBuyTeamSettlementAggregate);


        /* 5. 组队回调处理 - 处理失败也会有定时任务补偿，通过这样的方式，可以减轻任务调度，提高时效性  */
        if(isNotify){
            Map<String, Integer> notifyResultMap = execSettlementNotifyJob(teamId);
            log.info("回调通知拼团完结 result:{}", JSON.toJSONString(notifyResultMap));
        }

        // 6. 返回结算信息 - 公司中开发这样的流程时候，会根据外部需要进行值的设置
        return TradePaySettlementEntity.builder().source(tradePaySuccessEntity.getSource()).channel(tradePaySuccessEntity.getChannel()).userId(tradePaySuccessEntity.getUserId()).teamId(teamId).activityId(groupBuyTeamEntity.getActivityId()).outTradeNo(tradePaySuccessEntity.getOutTradeNo()).build();
    }

    @Override
    public Map<String, Integer> execSettlementNotifyJob() throws Exception {
        log.info("拼团交易-执行结算通知任务");
        // 查询所有未执行任务
        List<NotifyTaskEntity> notifyTaskEntityList = repository.queryUnExecutedNotifyTaskList();

        return execSettlementNotifyJob(notifyTaskEntityList);
    }

    @Override
    public Map<String, Integer> execSettlementNotifyJob(String teamId) throws Exception {
        log.info("拼团交易-执行结算通知回调，指定 teamId:{}", teamId);
        List<NotifyTaskEntity> notifyTaskEntityList = repository.queryUnExecutedNotifyTaskList(teamId);
        return execSettlementNotifyJob(notifyTaskEntityList);
    }


    private Map<String, Integer> execSettlementNotifyJob(List<NotifyTaskEntity> notifyTaskEntityList) throws Exception {
        int successCount = 0, errorCount = 0, retryCount = 0;

        for (NotifyTaskEntity notifyTaskEntity : notifyTaskEntityList) {
            // 回调处理 success 成功，error 失败
            String response = port.groupBuyNotify(notifyTaskEntity);

            // 更新状态判断&变更数据库表回调任务状态
            if (NotifyTaskHTTPEnumVO.SUCCESS.getCode().equals(response)) {
                int updateCount = repository.updateNotifyTaskStatusSuccess(notifyTaskEntity.getTeamId());
                if (1 == updateCount) {
                    successCount += 1;
                }
            } else if(NotifyTaskHTTPEnumVO.ERROR.getCode().equals(response)) {
                if (notifyTaskEntity.getNotifyCount() < 5) {
                    int updateCount = repository.updateNotifyTaskStatusError(notifyTaskEntity.getTeamId());
                    if (1 == updateCount) {
                        errorCount += 1;
                    }
                } else {
                    int updateCount = repository.updateNotifyTaskStatusRetry(notifyTaskEntity.getTeamId());
                    if (1 == updateCount) {
                        retryCount += 1;
                    }
                }
            }
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("waitCount", notifyTaskEntityList.size());
        resultMap.put("successCount", successCount);
        resultMap.put("errorCount", errorCount);
        resultMap.put("retryCount", retryCount);

        return resultMap;
    }
}




