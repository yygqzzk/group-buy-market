package xyz.yygqzzk.trigger.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.yygqzzk.api.IMarketTradeService;
import xyz.yygqzzk.api.dto.LockMarketPayOrderRequestDTO;
import xyz.yygqzzk.api.dto.LockMarketPayOrderResponseDTO;
import xyz.yygqzzk.api.response.Response;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.service.IIndexGroupBuyMarketService;
import xyz.yygqzzk.domain.trade.model.entity.MarketPayOrderEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayActivityEntity;
import xyz.yygqzzk.domain.trade.model.entity.PayDiscountEntity;
import xyz.yygqzzk.domain.trade.model.entity.UserEntity;
import xyz.yygqzzk.domain.trade.model.valobj.GroupBuyProgressVO;
import xyz.yygqzzk.domain.trade.service.ITradeLockOrderService;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/29
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/gbm/trade/")
@CrossOrigin("*")
public class MarketTradeController implements IMarketTradeService {
    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @Resource
    private ITradeLockOrderService tradeOrderService;

    /* 锁单 */
    @RequestMapping(value = "lock_market_pay_order", method = RequestMethod.POST)
    @Override
    public Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(@RequestBody LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO) {

        try {
            // 参数
            String userId = lockMarketPayOrderRequestDTO.getUserId();
            String source = lockMarketPayOrderRequestDTO.getSource();
            String channel = lockMarketPayOrderRequestDTO.getChannel();
            String goodsId = lockMarketPayOrderRequestDTO.getGoodsId();
            Long activityId = lockMarketPayOrderRequestDTO.getActivityId();
            String outTradeNo = lockMarketPayOrderRequestDTO.getOutTradeNo();
            String notifyUrl = lockMarketPayOrderRequestDTO.getNotifyUrl();
            /* 根据传入的teamId参数来判断用户是自己成团，还是参加拼团 */
            String teamId = lockMarketPayOrderRequestDTO.getTeamId();

            log.info("营销交易锁单:{} LockMarketPayOrderRequestDTO:{}", userId, JSON.toJSONString(lockMarketPayOrderRequestDTO));

            if (StringUtils.isBlank(userId) || StringUtils.isBlank(source) || StringUtils.isBlank(channel) || StringUtils.isBlank(goodsId) || StringUtils.isBlank(outTradeNo) || null == activityId || StringUtils.isBlank(notifyUrl)) {
                return Response.<LockMarketPayOrderResponseDTO>builder().code(ResponseCode.ILLEGAL_PARAMETER.getCode()).info(ResponseCode.ILLEGAL_PARAMETER.getInfo()).build();
            }

            /* 先判断是否存在订单，保证幂等性 */
            MarketPayOrderEntity marketPayOrderEntity = tradeOrderService.queryNoPayMarketPayOrderByOutTradeNo(outTradeNo, teamId);

            if (null != marketPayOrderEntity) {
                /* 已存在交易锁单记录，直接返回已有交易锁单记录 */
                LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO = LockMarketPayOrderResponseDTO.builder().orderId(marketPayOrderEntity.getOrderId()).deductionPrice(marketPayOrderEntity.getDeductionPrice()).tradeOrderStatus(marketPayOrderEntity.getTradeOrderStatusEnumVO().getCode()).build();

                log.info("交易锁单记录(存在):{} marketPayOrderEntity:{}", userId, JSON.toJSONString(marketPayOrderEntity));
                return Response.<LockMarketPayOrderResponseDTO>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).data(lockMarketPayOrderResponseDTO).build();

            }

            /* 不存在已锁单交易记录 */

            /* 若是加入拼团 */
            if (null != teamId) {
                /* 判断拼团锁单人数达到了目标人数 */
                GroupBuyProgressVO groupBuyProgressVO = tradeOrderService.queryGroupBuyProgress(teamId);
                if (groupBuyProgressVO != null && Objects.equals(groupBuyProgressVO.getLockCount(), groupBuyProgressVO.getTargetCount())) {
                    log.info("交易锁单拦截-拼单目标已达成:{} {}", userId, teamId);
                    /* 已达到拼团人数 */
                    return Response.<LockMarketPayOrderResponseDTO>builder().code(ResponseCode.E0006.getCode()).info(ResponseCode.E0006.getInfo()).build();
                }
            }

            MarketProductEntity marketProductEntity = MarketProductEntity.builder().userId(userId).channel(channel).source(source).goodsId(goodsId).build();

            /* 试算扣减金额 */
            TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);
            GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = trialBalanceEntity.getGroupBuyActivityDiscountVO();

            Date validEndTime = new Date();

            /* 创建锁单 */
            MarketPayOrderEntity marketPayOrderRes = tradeOrderService.lockMarketPayOrder(
                    UserEntity.builder()
                            .userId(userId)
                            .build(),
                    PayActivityEntity.builder()
                            .activityName(groupBuyActivityDiscountVO.getActivityName())
                            .activityId(groupBuyActivityDiscountVO.getActivityId())
                            .teamId(teamId).targetCount(trialBalanceEntity.getTargetCount()).
                            startTime(trialBalanceEntity.getStartTime())
                            .endTime(trialBalanceEntity.getEndTime())
                            .validTime(groupBuyActivityDiscountVO.getValidTime())
                            .build(),
                    PayDiscountEntity.builder()
                            .source(source)
                            .channel(channel)
                            .goodsId(trialBalanceEntity.getGoodsId())
                            .goodsName(trialBalanceEntity.getGoodsName())
                            .originalPrice(trialBalanceEntity.getOriginalPrice())
                            .deductionPrice(trialBalanceEntity.getDeductionPrice())
                            .payPrice(trialBalanceEntity.getPayPrice())
                            .outTradeNo(outTradeNo)
                            .notifyUrl(notifyUrl)
                            .build());

            log.info("交易锁单记录(新):{} marketPayOrderEntity:{}", userId, JSON.toJSONString(marketPayOrderEntity));

            // 返回结果
            return Response.<LockMarketPayOrderResponseDTO>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).data(LockMarketPayOrderResponseDTO.builder().orderId(marketPayOrderRes.getOrderId()).deductionPrice(marketPayOrderRes.getDeductionPrice()).tradeOrderStatus(marketPayOrderRes.getTradeOrderStatusEnumVO().getCode()).build()).build();
        } catch (AppException e) {
            log.error("营销交易锁单业务异常:{} LockMarketPayOrderRequestDTO:{}", lockMarketPayOrderRequestDTO.getUserId(), JSON.toJSONString(lockMarketPayOrderRequestDTO), e);
            return Response.<LockMarketPayOrderResponseDTO>builder().code(e.getCode()).info(e.getInfo()).build();
        } catch (Exception e) {
            log.error("营销交易锁单服务失败:{} LockMarketPayOrderRequestDTO:{}", lockMarketPayOrderRequestDTO.getUserId(), JSON.toJSONString(lockMarketPayOrderRequestDTO), e);
            return Response.<LockMarketPayOrderResponseDTO>builder().code(ResponseCode.UN_ERROR.getCode()).info(ResponseCode.UN_ERROR.getInfo()).build();
        }
    }
}




