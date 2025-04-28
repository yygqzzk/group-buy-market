package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.SCSkuActivity;

import java.util.List;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/28
 */
@Mapper
public interface ISCSkuActivityDao {
    SCSkuActivity querySCSkuActivityBySCGoodsId(SCSkuActivity scSkuActivity);


}
