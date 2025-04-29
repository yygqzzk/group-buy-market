package xyz.yygqzzk.domain.tag.service;

/**
 * @author zzk
 * @version 1.0
 * @description 人群标签服务接口
 * @since 2025/4/29
 */

public interface ITagService {

    /**
     * 执行人群标签批次任务
     *
     * @param tagId   人群ID
     * @param batchId 批次ID
     */
    void execTagBatchJob(String tagId, String batchId);

}

