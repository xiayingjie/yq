package com.common.manage.service.yq;


import com.common.manage.dao.yq.FavoriteDAO;
import com.common.manage.dao.yq.ReplyDAO;
import com.common.manage.entity.yq.Discovery;
import com.common.manage.entity.yq.Favorite;
import com.common.manage.entity.yq.Reply;
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
 * @classDescription:评论Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class CommentService extends BaseService<Reply> {
    @Autowired
    private ReplyDAO replyDAO;


    /**
     * 分页查询评论列表
     * @param queryDTO
     * @param disconveryId
     * @return
     */
    public Page findReplyList(QueryDTO queryDTO,int disconveryId){
        //查询结果
        Map searchMap=new HashMap();
        searchMap.put("search_discovery_id__EQ_I", String.valueOf(disconveryId));
        queryDTO.setPage(queryDTO.getPage() - 1);
        PageRequest pageRequest=SpecificationUtil.getPageRequest(queryDTO);
        Specification specification = null;
        Page page = null;
        specification= SpecificationUtil.bySearchFilter(searchMap, Reply.class);
        page=this.replyDAO.findAll(specification, pageRequest);

        return page;
    }
    @Override
    public BaseDAO getDAO() {
        return replyDAO;
    }


}
