package com.common.manage.view.head;

import com.common.manage.entity.yq.User;
import com.common.manage.tool.MyPage;
import com.common.manage.view.BaseController;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.web.query.QueryDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/9/6
 */
@Controller
@RequestMapping(value = "/relation")
public class RelationController extends BaseController {
    //添加或者取消 type=1关注 2取消
    @RequestMapping(value = "action")
    public void action(HttpServletRequest request,HttpServletResponse response,String accessToken,int userId,int type){
        User user=this.checkAccessTocken(response, accessToken);
        if(null ==user){
            return;
        }
        User oUser=checkUser(response,userId);
        if(null==oUser){
            return;
        }
        boolean flag=this.relationService.action(user,userId,type);
        Map map=null;
        if(flag==true){
            map=this.getSuccess();
        }else{
            map=this.getFaild(0);
        }
        this.out(response,map);
    }

    //查询好友信息
    @RequestMapping(value = "list")
    public void list(HttpServletRequest request,HttpServletResponse response,String accessToken,int userId,int type,QueryDTO queryDTO){
        User user=null;
        if(!StringUtils.isBlank(accessToken)) {
            user = this.checkAccessTocken(response, accessToken);
            if (null == user) {
                return;
            }
        }
        int page=queryDTO.getPage()<1?1: queryDTO.getPage();
        queryDTO.setOrder("-createAt");
        MyPage myPage=this.relationService.getList(user.getId(),userId,type,page,queryDTO.getSize());

        Map map=this.getSuccess();
        //获取数据
        List list= BeanInfoUtil.list2map(myPage.getList());

        map.put("data",list);
        map.put("extra",this.putPage(page,queryDTO.getSize(),myPage.getAllCount(),(int)myPage.getAllPage()));
        super.out(response, map);

    }
}
