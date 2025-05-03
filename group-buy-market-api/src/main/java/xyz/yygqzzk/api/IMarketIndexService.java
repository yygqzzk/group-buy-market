package xyz.yygqzzk.api;

import xyz.yygqzzk.api.dto.GoodsMarketRequestDTO;
import xyz.yygqzzk.api.dto.GoodsMarketResponseDTO;
import xyz.yygqzzk.api.response.Response;

/**
 * @author zzk
 * @version 1.0
 * @description 营销首页服务接口
 * @since 2025/5/2
 */
public interface IMarketIndexService {

    /**
     * 查询拼团营销配置
     *
     * @param goodsMarketRequestDTO 营销商品信息
     * @return 营销配置信息
     */
    Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(GoodsMarketRequestDTO goodsMarketRequestDTO);
}




