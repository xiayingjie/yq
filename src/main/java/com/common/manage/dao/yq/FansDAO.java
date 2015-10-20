package com.common.manage.dao.yq;

import com.common.manage.entity.yq.Fans;
import com.xyj.common.web.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/9
 */

public interface FansDAO  extends BaseDAO<Fans,Integer> {


    @Modifying
    @Query("delete from Fans  f where f.userId=?1 and f.fansId=?2")
    public int deleteFans(int userId,int fansId);
}
