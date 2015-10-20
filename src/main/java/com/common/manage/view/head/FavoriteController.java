package com.common.manage.view.head;

import com.common.manage.dao.yq.FavoriteDAO;
import com.common.manage.entity.yq.Discovery;
import com.common.manage.entity.yq.Favorite;
import com.common.manage.entity.yq.User;
import com.common.manage.view.BaseController;
import com.google.common.collect.Lists;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.web.query.QueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/9/6
 */
@Controller
@RequestMapping(value = "/favorite")
public class FavoriteController extends BaseController {
    //添加或者取消收藏 type=1收藏 2取消
    @RequestMapping(value = "action")
    public void action(HttpServletRequest request,HttpServletResponse response,int id,int type,String accessToken){

            User user=this.checkAccessTocken(response, accessToken);
            if(null ==user){
                return;
            }
            Map map=null;
            Discovery discovery=this.discoveryService.findById(id);
            if(null==discovery){
                map=this.getFaild(0);
                map.put("msg", "收藏或者取消的对象不存在");
            }else{//真实数据库操作
                boolean flag=this.favoriteService.action(discovery,user,type);
                if(flag==true){
                    map=this.getSuccess();
                }else{
                    map=this.getFaild(0);
                }
            }
            this.out(response, map);
    }

    @RequestMapping(value = "list")
    public void list(HttpServletRequest request,HttpServletResponse response,String accessToken,QueryDTO queryDTO){
            User user=this.checkAccessTocken(response, accessToken);
            if(null ==user){
                return;
            }

           queryDTO.setOrder("-createAt");
           Page<Favorite> p=favoriteService.findFavoriteList(queryDTO, user.getId());

            Map map=this.getSuccess();
            List list= Lists.newArrayList();
            for(Favorite f:p.getContent()){
              //  Map fMap= BeanInfoUtil.bean2map(f,"id,user");
                Map dMap= BeanInfoUtil.bean2map(f.getDiscovery(), "handler,hibernateLazyInitializer");
                dMap.put("user",BeanInfoUtil.bean2mapi(f.getUser(), "id,img,sex,nickName"));
              //  fMap.put("discovery",dMap);
                list.add(dMap);
            }
            //获取数据

            map.put("data",list);
            map.put("extra",this.putPage(queryDTO.getPage()<1?1:queryDTO.getPage(),queryDTO.getSize(),p.getTotalElements(),p.getTotalPages()));
            super.out(response, map);
    }


    @RequestMapping(value = "ids")
    public void ids(HttpServletRequest request,HttpServletResponse response,String accessToken){
        User user=this.checkAccessTocken(response, accessToken);
        if(null ==user){
            return;
        }
        List<Integer> ids=((FavoriteDAO)favoriteService.getDAO()).findFavoritesByUser(user.getId());

        Map map=this.getSuccess();
        List list= Lists.newArrayList();

        //获取数据

        map.put("data",ids);
        super.out(response, map);
    }
}
