package xyz.yygqzzk.infrastructure.dcc;

import org.springframework.stereotype.Service;
import xyz.yygqzzk.types.annotation.DCCValue;
import xyz.yygqzzk.types.common.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @description 动态配置服务
 * @since 2025/4/28
 */
@Service
public class DCCService {

    /**
     * 降级开关 0关闭、1开启
     */
    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch;

    /* 切量开关 控制进入流量*/
    @DCCValue("cutRange:100")
    private String cutRange;

    /* sc黑名单 */
    @DCCValue("scBlacklist:s02c02")
    private String scBlacklist;


    /*
    * 切量和降级是系统设计中常见的两种策略，主要用于应对高并发、系统过载或部分功能异常的场景。

    切量是一种流量控制策略，指在系统面临高负载或部分服务不可用时，通过限制进入系统的请求量来保护系统。

    降级是指在系统资源不足或部分功能异常时，暂时关闭或简化某些非核心功能，以确保核心功能的正常运行。

    * */

    public boolean isDowngradeSwitch() {
        return Constants.DOWNGRADE_SWITCH_ENABLE.equals(downgradeSwitch);
    }


    /* 通过计算hash来减少访问量 */
    public boolean isCutRange(String userId) {
        // 计算哈希码的绝对值
        int hash = Math.abs(userId.hashCode());
        // 获取最后两位
        int lastTwoDigits = hash % 100;


        // 判断是否在切量范围内
        return lastTwoDigits <= Integer.parseInt(cutRange);
    }

    public boolean isSCBlackIntercept(String source, String channel) {
        List<String> list = Arrays.asList(scBlacklist.split(Constants.SPLIT));
        return list.contains(source + channel);

    }
}




