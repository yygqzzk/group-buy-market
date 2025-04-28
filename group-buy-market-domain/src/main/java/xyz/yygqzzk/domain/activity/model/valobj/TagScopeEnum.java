package xyz.yygqzzk.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zzk
 * @version 1.0
 * @description 活动人群标签作用域范围枚举
 * @since 2025/4/28
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum TagScopeEnum {
    VISIBLE(true,false,"是否可看见拼团"),
    ENABLE(true, false,"是否可参与拼团"),
    ;

    private Boolean allow;
    private Boolean refuse;
    private String desc;
}
