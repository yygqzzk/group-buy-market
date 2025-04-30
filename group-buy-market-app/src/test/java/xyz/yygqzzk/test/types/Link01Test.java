package xyz.yygqzzk.test.types;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yygqzzk.test.types.rules01.factory.Rule01TradeRuleFactory;
import xyz.yygqzzk.types.design.framework.link.model1.ILogicLink;

import javax.annotation.Resource;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class Link01Test {

    @Resource
    public Rule01TradeRuleFactory rule01TradeRuleFactory;


    @Test
    public void test_model01_01() throws Exception {
        ILogicLink<String, Rule01TradeRuleFactory.DynamicContext, String> root = rule01TradeRuleFactory.openLogicLink();

        String res = root.apply("123", new Rule01TradeRuleFactory.DynamicContext());

        log.info("测试结果:{}", JSON.toJSONString(res));
    }

}




