package xyz.yygqzzk.domain.tag.adapter.repository;

import xyz.yygqzzk.domain.tag.model.entity.CrowdTagsJobEntity;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/28
 */
public interface ITagRepository {
    CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId);

    void addCrowdTagsUserId(String tagId, String userId);

    void updateCrowdTagsStatistics(String tagId, int size);
}
