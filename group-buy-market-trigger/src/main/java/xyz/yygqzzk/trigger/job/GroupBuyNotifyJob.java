package xyz.yygqzzk.trigger.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.service.ITradeSettlementOrderService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zzk
 * @version 1.0
 * @description 定时任务执行回调通知
 * @since 2025/5/2
 */
@Slf4j
@Service
public class GroupBuyNotifyJob {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;


    @Scheduled(cron = "0/15 * * * * ?")
    public void exec() {
        try {
            Map<String, Integer> result = tradeSettlementOrderService.execSettlementNotifyJob();
            log.info("定时任务, 回调通知拼团完结任务 result: {}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("定时任务, 回调通知拼团完结任务失败", e);
        }
    }

}




