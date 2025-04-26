package xyz.yygqzzk.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyActivityDao;
import xyz.yygqzzk.infrastructure.dao.IGroupBuyDiscountDao;
import xyz.yygqzzk.infrastructure.dao.po.GroupBuyActivity;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;


    @Test
    public void test() {
        List<GroupBuyActivity> groupBuyActivities = groupBuyActivityDao.queryGroupBuyActivityList();
        log.info(JSON.toJSONString(groupBuyActivities));
    }

}
