package xyz.yygqzzk.infrastructure.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBitSet;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import xyz.yygqzzk.domain.tag.adapter.repository.ITagRepository;
import xyz.yygqzzk.domain.tag.model.entity.CrowdTagsJobEntity;
import xyz.yygqzzk.infrastructure.dao.ICrowdTagsDao;
import xyz.yygqzzk.infrastructure.dao.ICrowdTagsDetailDao;
import xyz.yygqzzk.infrastructure.dao.ICrowdTagsJobDao;
import xyz.yygqzzk.infrastructure.dao.po.CrowdTags;
import xyz.yygqzzk.infrastructure.dao.po.CrowdTagsDetail;
import xyz.yygqzzk.infrastructure.dao.po.CrowdTagsJob;
import xyz.yygqzzk.infrastructure.redis.IRedisService;

/**
 * @author zzk
 * @version 1.0
 * @description
 * @since 2025/4/28
 */
@Repository
@RequiredArgsConstructor
public class TagRepository implements ITagRepository {
    private final ICrowdTagsDao crowdTagsDao;
    private final ICrowdTagsDetailDao crowdTagsDetailDao;
    private final ICrowdTagsJobDao crowdTagsJobDao;
    private final IRedisService redisService;

    @Override
    public CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId) {
        CrowdTagsJob crowdTagsJobReq = CrowdTagsJob.builder()
                .tagId(tagId)
                .batchId(batchId).build();
        CrowdTagsJob crowdTagsJobRes = crowdTagsJobDao.queryCrowdTagsJob(crowdTagsJobReq);

        return CrowdTagsJobEntity.builder()
                .tagType(crowdTagsJobRes.getTagType())
                .tagRule(crowdTagsJobRes.getTagRule())
                .statStartTime(crowdTagsJobRes.getStatStartTime())
                .statEndTime(crowdTagsJobRes.getStatEndTime())
                .build();
    }

    @Override
    public void addCrowdTagsUserId(String tagId, String userId) {
        CrowdTagsDetail crowdTagsDetailReq = CrowdTagsDetail.builder().tagId(tagId).userId(userId).build();
        try {
            crowdTagsDetailDao.addCrowdTagsUserId(crowdTagsDetailReq);

            // 使用redis bitMap 做一个过滤，快速判断用户是否是标签人群
            RBitSet bitSet = redisService.getBitSet(tagId);
            bitSet.set(redisService.getIndexFromUserId(userId));
        } catch (DuplicateKeyException ignore) {
        }
    }

    @Override
    public void updateCrowdTagsStatistics(String tagId, int size) {
        CrowdTags crowdTagsReq = new CrowdTags();
        crowdTagsReq.setTagId(tagId);
        crowdTagsReq.setStatistics(size);

        crowdTagsDao.updateCrowdTagsStatistics(crowdTagsReq);
    }
}




