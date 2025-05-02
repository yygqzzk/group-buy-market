package xyz.yygqzzk.domain.trade.adapter.port;

import xyz.yygqzzk.domain.trade.model.entity.NotifyTaskEntity;

/**
 * @author zzk
 * @version 1.0
 * @description 交易接口服务接口
 * @since 2025/5/2
 */
public interface ITradePort {
    String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception;
}




