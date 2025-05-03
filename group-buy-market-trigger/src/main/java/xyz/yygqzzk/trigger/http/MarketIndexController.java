package xyz.yygqzzk.trigger.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.yygqzzk.api.IMarketIndexService;
import xyz.yygqzzk.api.IMarketTradeService;
import xyz.yygqzzk.api.dto.GoodsMarketRequestDTO;
import xyz.yygqzzk.api.dto.GoodsMarketResponseDTO;
import xyz.yygqzzk.api.dto.SettlementMarketPayOrderResponseDTO;
import xyz.yygqzzk.api.response.Response;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import xyz.yygqzzk.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import xyz.yygqzzk.domain.activity.model.valobj.TeamStatisticVO;
import xyz.yygqzzk.domain.activity.service.IIndexGroupBuyMarketService;
import xyz.yygqzzk.domain.trade.service.ITradeLockOrderService;
import xyz.yygqzzk.domain.trade.service.ITradeSettlementOrderService;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @description 营销首页服务
 * @since 2025/5/2
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/index/")
public class MarketIndexController implements IMarketIndexService {
    @Resource
    private ITradeLockOrderService tradeLockOrderService;
    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;
    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;


    @RequestMapping(value = "query_group_buy_market_config", method = RequestMethod.POST)
    @Override
    public Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(@RequestBody GoodsMarketRequestDTO goodsMarketRequestDTO) {

        try {
            log.info("查询拼团营销配置开始:{} goodsId:{}", goodsMarketRequestDTO.getUserId(), goodsMarketRequestDTO.getGoodsId());

            if (StringUtils.isBlank(goodsMarketRequestDTO.getUserId()) || StringUtils.isBlank(goodsMarketRequestDTO.getSource()) || StringUtils.isBlank(goodsMarketRequestDTO.getChannel()) || StringUtils.isBlank(goodsMarketRequestDTO.getGoodsId())) {
                return Response.<GoodsMarketResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            // 1. 营销优惠试算
            MarketProductEntity marketProductEntity = MarketProductEntity.builder()
                    .userId(goodsMarketRequestDTO.getUserId())
                    .goodsId(goodsMarketRequestDTO.getGoodsId())
                    .source(goodsMarketRequestDTO.getSource())
                    .channel(goodsMarketRequestDTO.getChannel())
                    .build();
            TrialBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(marketProductEntity);

            GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = trialBalanceEntity.getGroupBuyActivityDiscountVO();
            Long activityId = groupBuyActivityDiscountVO.getActivityId();


            // 2. 查询拼团组队
            List<UserGroupBuyOrderDetailEntity> userGroupBuyOrderDetailEntities = indexGroupBuyMarketService.queryInProgressUserGroupBuyOrderDetailList(activityId, goodsMarketRequestDTO.getUserId(), 1, 2);

            // 3. 统计拼团数据
            TeamStatisticVO teamStatisticVO = indexGroupBuyMarketService.queryTeamStatisticByActivityId(activityId);

            GoodsMarketResponseDTO.Goods goods = GoodsMarketResponseDTO.Goods.builder()
                    .goodsId(trialBalanceEntity.getGoodsId())
                    .originalPrice(trialBalanceEntity.getOriginalPrice())
                    .deductionPrice(trialBalanceEntity.getDeductionPrice())
                    .payPrice(trialBalanceEntity.getPayPrice())
                    .build();

            List<GoodsMarketResponseDTO.Team> teams = new ArrayList<>();
            if (null != userGroupBuyOrderDetailEntities && !userGroupBuyOrderDetailEntities.isEmpty()) {
                for (UserGroupBuyOrderDetailEntity userGroupBuyOrderDetailEntity : userGroupBuyOrderDetailEntities) {
                    GoodsMarketResponseDTO.Team team = GoodsMarketResponseDTO.Team.builder()
                            .userId(userGroupBuyOrderDetailEntity.getUserId())
                            .teamId(userGroupBuyOrderDetailEntity.getTeamId())
                            .activityId(userGroupBuyOrderDetailEntity.getActivityId())
                            .targetCount(userGroupBuyOrderDetailEntity.getTargetCount())
                            .completeCount(userGroupBuyOrderDetailEntity.getCompleteCount())
                            .lockCount(userGroupBuyOrderDetailEntity.getLockCount())
                            .validStartTime(userGroupBuyOrderDetailEntity.getValidStartTime())
                            .validEndTime(userGroupBuyOrderDetailEntity.getValidEndTime())
                            .validTimeCountdown(GoodsMarketResponseDTO.Team.differenceDateTime2Str(new Date(), userGroupBuyOrderDetailEntity.getValidEndTime()))
                            .outTradeNo(userGroupBuyOrderDetailEntity.getOutTradeNo())
                            .build();
                    teams.add(team);
                }
            }

            GoodsMarketResponseDTO.TeamStatistic teamStatistic = GoodsMarketResponseDTO.TeamStatistic.builder()
                    .allTeamCount(teamStatisticVO.getAllTeamCount())
                    .allTeamCompleteCount(teamStatisticVO.getAllTeamCompleteCount())
                    .allTeamUserCount(teamStatisticVO.getAllTeamUserCount())
                    .build();

            GoodsMarketResponseDTO goodsMarketResponseDTO = GoodsMarketResponseDTO.builder()
                    .goods(goods)
                    .teamList(teams)
                    .teamStatistic(teamStatistic)
                    .build();

            log.info("查询拼团营销配置完成:{} goodsId:{} response:{}", goodsMarketRequestDTO.getUserId(), goodsMarketRequestDTO.getGoodsId(), JSON.toJSONString(goodsMarketResponseDTO));

            return Response.<GoodsMarketResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(goodsMarketResponseDTO)
                    .build();



        } catch (AppException e) {
            log.error("查询拼团营销配置异常:{} goodsMarketRequestDTO:{}", goodsMarketRequestDTO.getUserId(), JSON.toJSONString(goodsMarketRequestDTO), e);
            return Response.<GoodsMarketResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("查询拼团营销配置失败:{} goodsMarketRequestDTO:{}", goodsMarketRequestDTO.getUserId(), JSON.toJSONString(goodsMarketRequestDTO), e);
            return Response.<GoodsMarketResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
}




