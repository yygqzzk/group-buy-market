package xyz.yygqzzk.infrastructure.adapter.port;

import jodd.time.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import xyz.yygqzzk.domain.trade.adapter.port.ITradePort;
import xyz.yygqzzk.domain.trade.model.entity.NotifyTaskEntity;
import xyz.yygqzzk.infrastructure.gateway.GroupBuyNotifyService;
import xyz.yygqzzk.infrastructure.redis.IRedisService;
import xyz.yygqzzk.types.enums.NotifyTaskHTTPEnumVO;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zzk
 * @version 1.0
 * @description 交易接口服务接口
 * @since 2025/5/2
 */
@Service
public class TradePort implements ITradePort {
    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;
    @Resource
    private IRedisService redisService;


    @Override
    public String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception {
        RLock lock = redisService.getLock(notifyTask.lockKey());

        // group-buy-market 拼团服务端会被部署到多台应用服务器上，那么就会有很多任务一起执行。这个时候要进行抢占，避免被多次执行
        try {
            if(lock.tryLock(3, 0, TimeUnit.SECONDS)) {
                try {
                    // 无效的 notifyUrl 则直接返回成功
                    if (StringUtils.isBlank(notifyTask.getNotifyUrl()) || "暂无".equals(notifyTask.getNotifyUrl())) {
                        return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                    }
                    return groupBuyNotifyService.groupBuyNotify(notifyTask.getNotifyUrl(), notifyTask.getParameterJson());

                } finally {
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }


    }
}




