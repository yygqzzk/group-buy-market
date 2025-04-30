package xyz.yygqzzk.test.types;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yygqzzk.test.types.rules01.factory.Rule01TradeRuleFactory;
import xyz.yygqzzk.test.types.rules02.factory.Rule02TradeRuleFactory;
import xyz.yygqzzk.test.types.rules02.logic.XxxResponse;
import xyz.yygqzzk.types.design.framework.link.model1.ILogicLink;
import xyz.yygqzzk.types.design.framework.link.model2.chain.BusinessLinkedList;

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
public class Link02Test {

    @Resource(name = "demo01")
    private BusinessLinkedList<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> businessLinkedList01;

    @Resource(name = "demo02")
    private BusinessLinkedList<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> businessLinkedList02;


    @Test
    public void test_model02_01() throws Exception {
        XxxResponse res = businessLinkedList01.apply("hello 1", new Rule02TradeRuleFactory.DynamicContext());

        log.info(JSON.toJSONString(res));
    }

    @Test
    public void test_model02_02() throws Exception {
        XxxResponse res = businessLinkedList02.apply("hello 2", new Rule02TradeRuleFactory.DynamicContext());

        log.info(JSON.toJSONString(res));
    }

}




