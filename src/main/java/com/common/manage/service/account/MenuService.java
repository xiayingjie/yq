package com.common.manage.service.account;


import com.common.manage.dao.account.MenuDAO;
import com.common.manage.entity.Menu;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @classDescription:菜单管理Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class MenuService extends BaseService<Menu> {
    @Autowired
    private MenuDAO menuDAO;


    @Override
    public BaseDAO getDAO() {
        return menuDAO;
    }


}
