package xyz.yygqzzk.test.domain.trade;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yygqzzk.domain.activity.service.IIndexGroupBuyMarketService;
import xyz.yygqzzk.domain.trade.model.entity.TradePaySettlementEntity;
import xyz.yygqzzk.domain.trade.model.entity.TradePaySuccessEntity;
import xyz.yygqzzk.domain.trade.service.ITradeLockOrderService;
import xyz.yygqzzk.domain.trade.service.ITradeSettlementOrderService;
import xyz.yygqzzk.domain.trade.service.settlement.TradeSettlementOrderServiceImpl;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/5/1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeSettlementOrderServiceTest {
    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @Resource
    private ITradeLockOrderService tradeOrderService;
    @Autowired
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @Test
    public void testSettlementOrder() {
        TradePaySuccessEntity tradePaySuccessEntity = new TradePaySuccessEntity() {
            {
                setSource("s01");
                setChannel("c01");
                setUserId("yygqzzk");
                setOutTradeNo("909000098113");
            }
        };

        TradePaySettlementEntity tradePaySettlementEntity = tradeSettlementOrderService.settlementMarketPayOrder(tradePaySuccessEntity);

        log.info("请求参数: {}", tradePaySuccessEntity);
        log.info("返回结果: {}", tradePaySettlementEntity);

    }

}




