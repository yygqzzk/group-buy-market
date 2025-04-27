package xyz.yygqzzk.domain.activity.service.trial.node;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.activity.model.entity.MarketProductEntity;
import xyz.yygqzzk.domain.activity.model.entity.TrialBalanceEntity;
import xyz.yygqzzk.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import xyz.yygqzzk.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import xyz.yygqzzk.types.design.framework.tree.StrategyHandler;
import xyz.yygqzzk.types.enums.ResponseCode;
import xyz.yygqzzk.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @since 2025/4/27
 * @description 根节点
 */
@Service
@Slf4j
public class RootNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private SwitchRoot switchRoot;


    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-RootNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));
        // 参数判断
        if (StringUtils.isBlank(requestParameter.getUserId()) || StringUtils.isBlank(requestParameter.getGoodsId()) ||
                StringUtils.isBlank(requestParameter.getSource()) || StringUtils.isBlank(requestParameter.getChannel())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicParameter) throws Exception {
        return switchRoot;
    }
}




