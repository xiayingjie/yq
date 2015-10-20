package com.common.manage.view.head;

import com.common.manage.entity.yq.Discovery;
import com.common.manage.entity.yq.Reply;
import com.common.manage.entity.yq.User;
import com.common.manage.view.BaseController;
import com.google.common.collect.Maps;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.web.query.QueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:评论接口
 * @author:xiayingjie
 * @createTime:15/9/6
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController extends BaseController {

    //添加或者取消收藏 type=1关注 2取消
    @RequestMapping(value = "reply")
    public void reply(HttpServletRequest request,HttpServletResponse response,String accessToken,int discoveryId,Reply reply){
        //检查令牌
        User user=this.checkAccessTocken(response, accessToken);
        if(null ==user){
            return;
        }
        //检查帖子
        Object obj=this.discoveryService.findDiscoveryById(discoveryId,1);
        if(!this.checkObject(response,obj)){
            return;
        }
        reply.setUser(user);
        reply.setDiscovery((Discovery)obj);
        Object object=this.commentService.save(reply);

        Map map=null;

        if(object==null){
            map=this.getFaild(0);

        }else{
            map=this.getSuccess();
        }
        this.out(response,map);

    }

    /**
     * 获取评论列表
     * @param request
     * @param response
     * @param discoveryId
     */
    @RequestMapping(value = "list")
    public void list(HttpServletRequest request,HttpServletResponse response,QueryDTO queryDTO,int discoveryId){
        //检查帖子
        Object obj=this.discoveryService.findDiscoveryById(discoveryId,1);
        if(!this.checkObject(response,obj)){
            return;
        }

        queryDTO.setOrder("-createAt");
        Page<Reply> p=this.commentService.findReplyList(queryDTO, discoveryId);

        //将结果转换成map
        Map map=this.getSuccess();
        List listMap = new  ArrayList();
        for(Reply reply:p.getContent()){
            Map replyMap= Maps.newHashMap();
            if(reply.getReplyUserId()!=0) {
                User replyUser =this.userService.findById(reply.getReplyUserId());
                replyMap.put("replyUser",BeanInfoUtil.bean2mapi(replyUser,"id,nickName,img"));
            }
            replyMap.put("user",BeanInfoUtil.bean2mapi(reply.getUser(),"id,nickName,img"));
            replyMap.put("tweet",reply.getTweet());
            replyMap.put("createAt",reply.getCreateAt());
            replyMap.put("id",reply.getId());
            listMap.add(replyMap);

        }


        map.put("data",listMap);
        map.put("extra",this.putPage(queryDTO.getPage()<1?1:queryDTO.getPage(),queryDTO.getSize(),p.getTotalElements(),p.getTotalPages()));
        super.out(response, map);
    }
}

