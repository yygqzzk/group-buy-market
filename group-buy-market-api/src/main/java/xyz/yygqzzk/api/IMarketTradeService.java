package xyz.yygqzzk.api;

import xyz.yygqzzk.api.dto.LockMarketPayOrderRequestDTO;
import xyz.yygqzzk.api.dto.LockMarketPayOrderResponseDTO;
import xyz.yygqzzk.api.response.Response;

/**
 * @author zzk
 * @version 1.0
 * @description 营销交易服务接口
 * @since 2025/4/29
 */
public interface IMarketTradeService {

    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

}
