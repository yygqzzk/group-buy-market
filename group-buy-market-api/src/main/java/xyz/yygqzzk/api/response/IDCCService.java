package xyz.yygqzzk.api.response;

/**
 * @author zzk
 * @version 1.0
 * @description DCC 动态配置中心
 * @since 2025/4/29
 */
public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}




