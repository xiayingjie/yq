package com.common.manage.service.account;


import com.common.manage.dao.account.ModuleDAO;
import com.common.manage.entity.Module;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @classDescription:模块管理Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class ModuleService extends BaseService<Module> {
    @Autowired
    private ModuleDAO moduleDAO;


    @Override
    public BaseDAO getDAO() {
        return moduleDAO;
    }
}
