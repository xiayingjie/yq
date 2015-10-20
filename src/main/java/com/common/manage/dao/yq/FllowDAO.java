package com.common.manage.dao.yq;

import com.common.manage.entity.yq.Fllow;
import com.xyj.common.web.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/9
 */

public interface FllowDAO  extends BaseDAO<Fllow,Integer> {

    @Modifying
    @Query("delete from Fllow  f where f.userId=?1 and f.fllowId=?2")
    public int deleteFllow(int userId,int fllowId);
}
