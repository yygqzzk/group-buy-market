package xyz.yygqzzk.trigger.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.yygqzzk.api.dto.NotifyRequestDTO;

/**
 * @author zzk
 * @version 1.0
 * @description 回调服务接口测试
 * @since 2025/5/2
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin("*")
public class TestApiClientController {

    /* 第三方拼团回调 */
    @RequestMapping(value = "group_buy_notify", method = RequestMethod.POST)
    public String groupBuyNotify(@RequestBody NotifyRequestDTO notifyRequestDTO) {

        log.info("模拟测试第三方服务接收拼团回调 {}", JSON.toJSONString(notifyRequestDTO));

        return "success";
    }

}




