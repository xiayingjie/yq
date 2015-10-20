package com.common.manage.service.account;


import com.common.manage.dao.account.RoleDAO;
import com.common.manage.entity.Role;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @classDescription:角色管理Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class RoleService extends BaseService<Role> {
    @Autowired
    private RoleDAO roleDAO;

    @Override
    public BaseDAO getDAO() {
        return roleDAO;
    }
}
