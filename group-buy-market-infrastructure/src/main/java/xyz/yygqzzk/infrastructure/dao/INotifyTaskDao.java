package xyz.yygqzzk.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.yygqzzk.infrastructure.dao.po.NotifyTask;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/30
 */
@Mapper
public interface INotifyTaskDao {

    void insert(NotifyTask notifyTask);

}




