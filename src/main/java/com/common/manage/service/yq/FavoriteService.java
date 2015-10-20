package com.common.manage.service.yq;


import com.common.manage.dao.yq.FavoriteDAO;
import com.common.manage.dao.yq.UserDAO;
import com.common.manage.entity.yq.Discovery;
import com.common.manage.entity.yq.DiscoveryAudit;
import com.common.manage.entity.yq.Favorite;
import com.common.manage.entity.yq.User;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import com.xyj.common.web.query.QueryDTO;
import com.xyj.common.web.query.SpecificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @classDescription:用户收藏Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class FavoriteService extends BaseService<Favorite> {
    @Autowired
    private FavoriteDAO favoriteDAO;


    /**
     * 添加或者取消收藏,用户只能搜藏一次
     * @param discovery
     * @param user
     * @param type
     * @return
     */
    public boolean action(Discovery discovery,User user,int type){
        boolean flag=false;
        try {
            if (type == 1) {
                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setDiscovery(discovery);
                int count=this.favoriteDAO.countFavoriteByDiscovery_idAndUser_id(discovery.getId(),user.getId());
                if(count == 0) {
                    this.favoriteDAO.save(favorite);
                }
            } else {
                this.favoriteDAO.cancelFavorite(discovery.getId(), user.getId());
            }
            flag=true;
        } catch (Exception e){
                flag=false;
        }
        return flag;
    }

    /**
     * 根据用户id查询相关的Favorite
     * @param queryDTO
     * @param userId
     * @return
     */
    public Page findFavoriteList(QueryDTO queryDTO,int userId){
        //查询结果
        Map searchMap=new HashMap();
        searchMap.put("search_user_id__EQ_I", String.valueOf(userId));
        queryDTO.setPage(queryDTO.getPage() - 1);
        PageRequest pageRequest=SpecificationUtil.getPageRequest(queryDTO);
        Specification specification = null;
        Page page = null;
        specification= SpecificationUtil.bySearchFilter(searchMap, Favorite.class);
        page=this.favoriteDAO.findAll(specification, pageRequest);

        return page;
    }
    @Override
    public BaseDAO getDAO() {
        return favoriteDAO;
    }


}
