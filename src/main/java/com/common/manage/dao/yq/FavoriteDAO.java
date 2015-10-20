package com.common.manage.dao.yq;

import com.common.manage.entity.yq.Fans;
import com.common.manage.entity.yq.Favorite;
import com.xyj.common.web.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/9
 */

public interface FavoriteDAO extends BaseDAO<Favorite,Integer> {

    @Modifying
    @Query("delete from Favorite  f where f.discovery.id=?1 and f.user.id=?2")
    public int cancelFavorite(int id,int userId);


    public int countFavoriteByDiscovery_idAndUser_id(int discoveryId,int userId);


    @Query("select f.discovery.id from Favorite f where f.user.id =?1")
    public  List<Integer> findFavoritesByUser(int userId);

}
