package com.common.manage.dao.yq;

import com.common.manage.entity.yq.User;
import com.xyj.common.web.BaseDAO;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/9
 */

public interface UserDAO extends BaseDAO<User,Integer> {

    /**
     * 根据用户名查询用户
     * @param loginName
     * @return
     */
    public User findByLoginName(String loginName);

    /**
     * 根据昵称查询用户
     * @param nickName
     * @return
     */
    public User findByNickName(String nickName);

    /**
     * 根据令牌查询用户
     * @param accessTocken
     * @return
     */
    public User findByAccessToken(String accessTocken);

}
