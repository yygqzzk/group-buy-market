package xyz.yygqzzk.infrastructure.dao.po.base;

import lombok.Builder;
import lombok.Data;

/**
 * @author zzk
 * @version 1.0
 * @description 限制查询个数
 * @since 2025/5/2
 */
public class Page {

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}




