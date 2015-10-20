package com.common.manage.service.yq;


import com.common.manage.dao.account.RoleDAO;
import com.common.manage.dao.yq.UserDAO;
import com.common.manage.entity.Role;
import com.common.manage.entity.yq.User;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @classDescription:用户管理Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class UserService extends BaseService<User> {
    @Autowired
    private UserDAO userDAO;


    /**
     * 根据用户名密码进行登陆
     *
     * @param userName
     * @param password
     * @return
     */
    public User login(String userName, String password) {
        User u = this.userDAO.findByLoginName(userName);
        if (null != u) {
            if (u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    /**
     * 检查名称是否存在
     * @param name 0表示LoginName,1表示NickName
     * @param type
     * @return
     */
    public boolean checkName(String name ,int type){
        boolean flag=false;
        User u;
        if(0==type){
            u=this.userDAO.findByLoginName(name);
        }else{
            u=this.userDAO.findByNickName(name);
        }
        if(null!=u){
            flag=true;
        }
        return flag;
    }

    /**
     * 修改用户信息
     * @return
     */
    public void alterUser(User user){
        this.userDAO.save(user);
    }

    /**
     * 检查令牌
     * @return
     */
    public User checkAccessTocken(String accessToken){
       return this.userDAO.findByAccessToken(accessToken);
    }


    public boolean fllowUser(String name ,int type){
        boolean flag=false;
        User u;
        if(0==type){
            u=this.userDAO.findByLoginName(name);
        }else{
            u=this.userDAO.findByNickName(name);
        }
        if(null!=u){
            flag=true;
        }
        return flag;
    }

    public User findUserByLoginName(String loginName){
        return this.userDAO.findByLoginName(loginName);
    }
    @Override
    public BaseDAO getDAO() {
        return userDAO;
    }


}
