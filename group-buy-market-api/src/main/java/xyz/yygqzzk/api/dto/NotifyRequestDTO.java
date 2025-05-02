package xyz.yygqzzk.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @description 回调请求对象
 * @since 2025/5/2
 */
@Data
public class NotifyRequestDTO {
    /** 组队ID */
    private String teamId;
    /** 外部单号 */
    private List<String> outTradeNoList;
}




