package xyz.yygqzzk.types.annotation;

import java.lang.annotation.*;

/**
 * @author zzk
 * @version 1.0
 * @description 动态配置注解
 * @since 2025/4/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface DCCValue {
    String value() default "";
}
