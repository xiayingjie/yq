package com.common.manage.dao.yq;

import com.common.manage.entity.yq.DiscoveryAudit;
import com.common.manage.entity.yq.User;
import com.xyj.common.web.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/9
 */

public interface DiscoveryAuditDAO extends BaseDAO<DiscoveryAudit,Integer> {

    @Modifying
    @Query("update DiscoveryAudit  d set d.agreeCount=d.agreeCount+1 where d.id=?1")
    public int updateAgree(int discoveyAuditId);

    @Modifying
    @Query("update DiscoveryAudit  d set d.refusCount=d.refusCount+1 where d.id=?1")
    public int updateRefus(int discoveyAuditId);

    /**
     * 根据discoveryId 查询对象
     * @param discoveryId
     * @return
     */
    public DiscoveryAudit findByDiscoveryId(int discoveryId);

}
