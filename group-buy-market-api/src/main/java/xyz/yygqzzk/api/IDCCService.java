package xyz.yygqzzk.api;

import xyz.yygqzzk.api.response.Response;

/**
 * @author zzk
 * @version 1.0
 * @description DCC 动态配置中心
 * @since 2025/4/29
 */
public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}




