package com.common.manage.service.yq;


import com.common.manage.dao.yq.*;
import com.common.manage.entity.yq.*;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import com.xyj.common.web.query.QueryDTO;
import com.xyj.common.web.query.SpecificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:用户管理Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class DiscoveryService extends BaseService<Discovery> {
    @Autowired
    private DiscoveryDAO discoveryDAO;
    @Autowired
    private AccusationDAO accusationDAO;
    @Autowired
    private DiscoveryAuditDAO discoveryAuditDAO;






    /**
     * 查找寻找对象
     * @param id
     * @param status
     * @return
     */
    public Object findDiscoveryById(int id,int status){
        Object obj=null;
        if(id==0){
            return obj;
        }
        if(status==0||status==1||status==3){
            obj=this.discoveryDAO.findOne(id);
        }else{
            obj=this.discoveryAuditDAO.findOne(id);
        }
        return obj;
    }

    /**
     * 根据用户id查询相关的discovery
     * @param queryDTO
     * @param userId
     * @param type
     * @return
     */
    public Page findDiscoverysByUserId(QueryDTO queryDTO,int userId,int type){
        //查询结果
        Map searchMap=new HashMap();
        searchMap.put("search_user_id__EQ_I", String.valueOf(userId));
        queryDTO.setPage(queryDTO.getPage()-1);
        PageRequest pageRequest=SpecificationUtil.getPageRequest(queryDTO);
        Specification specification = null;
        Page page = null;

        if(type==1){//查找通过的对象
            specification= SpecificationUtil.bySearchFilter(searchMap, Discovery.class);
            page=this.discoveryDAO.findAll(specification, pageRequest);
        }else{//查找未审核的对象
            specification=SpecificationUtil.bySearchFilter(searchMap, DiscoveryAudit.class);
            page=this.discoveryAuditDAO.findAll(specification, pageRequest);
        }
        return page;
    }

    /**
     * 审核discoverys
     * @param id
     * @param status
     * @return
     */
    public boolean review(int id,int status){

        //进行逻辑逻辑判断
        DiscoveryAudit discoveryAudit=this.discoveryAuditDAO.findOne(id);
        //如果对象不存在或者已经结贴，则返回错误
        if(null==discoveryAudit||discoveryAudit.getStatus()==3){
            return false;
        }
        int count=discoveryAudit.getAgreeCount()+discoveryAudit.getRefusCount();

        //投票超过==3次，2次通过就表示通过
        if(count==3){
            if(discoveryAudit.getAgreeCount()>=2){
                this.updateDiscovery(discoveryAudit);
            }
        }else if(count==5){ //投票等于5次，如果同意超过3次，则表示通过，否则表示拒绝
            if(discoveryAudit.getAgreeCount()>=3){
                this.updateDiscovery(discoveryAudit);
            }else{
                discoveryAudit.setStatus(4);
                this.discoveryAuditDAO.save(discoveryAudit);
            }
        }

        //更新投票数
        if(status==1){
            this.discoveryAuditDAO.updateAgree(id);
        }else{
            this.discoveryAuditDAO.updateRefus(id);
        }

        return true;
    }

    /**
     * 修改discovery
     * @return
     */
    public Object modifiy(int userId,Discovery d){
        Object obj=null;
        //根据不同的状态查询不同的表 1表示通过，2表示审核中
        if(d.getStatus()==1){
            Discovery discovery=this.discoveryDAO.findOne(d.getId());
            //只有用户才能修改自己的帖子
            if(discovery.getUser().getId()!=userId){
                return null;
            }
            Discovery dis=(Discovery)BeanInfoUtil.bean2bean(d, discovery);
            dis.setStatus(2);
            DiscoveryAudit da1=this.discoveryAuditDAO.findByDiscoveryId(d.getId());
            if(da1==null){
                da1=new DiscoveryAudit();
            }
            //如果帖子审核通过修改的话，将另外创建一个帖子到审核区
            DiscoveryAudit discoveryAudit=(DiscoveryAudit)BeanInfoUtil.bean2bean(dis,da1,"id");
            discoveryAudit.setDiscoveryId(dis.getId());
            obj=this.discoveryAuditDAO.save(discoveryAudit);
        }else if(d.getStatus()==2){
            DiscoveryAudit discoveryAudit=this.discoveryAuditDAO.findOne(d.getId());
            if(discoveryAudit.getUser().getId()!=userId){
                return null;
            }
            DiscoveryAudit da=(DiscoveryAudit)BeanInfoUtil.bean2bean(d, discoveryAudit,"id");
            obj=this.discoveryAuditDAO.save(da);

        }
        return obj;
    }

    /**
     * 更新discovery
     * @param discoveryAudit
     */
    public void  updateDiscovery(DiscoveryAudit discoveryAudit){
        //删除未审核对象
        discoveryAuditDAO.delete(discoveryAudit);
        Discovery discovery=(Discovery)BeanInfoUtil.bean2bean(discoveryAudit,new Discovery(),"id");
        discovery.setStatus(1);
        //如果关联到discovery对象，那么就更新，否则新增
        if(discoveryAudit.getDiscoveryId()!=0){
            discovery.setId(discoveryAudit.getDiscoveryId());
        }
        this.discoveryDAO.save(discovery);

    }



   // 获取审核帖子对象
    public DiscoveryAuditDAO getDiscoveryAuditDAO(){
        return this.discoveryAuditDAO;
    }

    // 获取举报dao对象
    public AccusationDAO getAccusationDAO() {
        return accusationDAO;
    }

    public void setAccusationDAO(AccusationDAO accusationDAO) {
        this.accusationDAO = accusationDAO;
    }

    @Override
    public BaseDAO getDAO() {
        return discoveryDAO;
    }
}
