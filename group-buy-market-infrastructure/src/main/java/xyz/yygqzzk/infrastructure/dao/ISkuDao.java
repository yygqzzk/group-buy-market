package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.Sku;

/**
 * @author zzk
 * @version 1.0
 * @description 商品查询
 * @since 2025/4/27
 */
@Mapper
public interface ISkuDao {
    Sku querySkuByGoodsId(String goodsId);
}




