package com.common.manage.view.head;

import com.common.manage.entity.yq.*;
import com.common.manage.service.yq.DisService;
import com.common.manage.view.BaseController;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.tool.EncodeUtil;
import com.xyj.common.web.query.PropertyFilter;
import com.xyj.common.web.query.QueryDTO;
import com.xyj.common.web.query.SpecificationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.net.util.URLUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/29
 */
@Controller
@RequestMapping(value = "/discovery")
public class DiscoveryController extends BaseController{

    @Autowired
    private  DisService disService;

    //发表寻找
    @RequestMapping(value = "list")
    public void list(HttpServletRequest request, HttpServletResponse response,double latitude ,double longitude ,String keyword,String category, QueryDTO queryDTO) {
        try {
            int type = request.getParameter("type") == null ? 0 : Integer.parseInt(request.getParameter("type"));
            int reward = request.getParameter("reward") == null ? 0 : Integer.parseInt(request.getParameter("reward"));
            FacetedPage<Dis> p = this.disService.search(latitude, longitude, 30, type, reward, category, keyword, queryDTO);


            Map map = this.getSuccess();
            List list = new ArrayList<>();
            for (Dis dis : p.getContent()) {
                String[] ss = dis.getLocation().split(",");
                double distance = getDistance(longitude, latitude, Double.parseDouble(ss[0]), Double.parseDouble(ss[1]));
                dis.setSort(String.valueOf(distance));
                Map disMap = BeanInfoUtil.bean2map(dis);
                disMap.put("user", BeanInfoUtil.bean2map(dis.getUser()));
                list.add(disMap);

            }
            map.put("data", list);
            map.put("extra", this.putPage(queryDTO.getPage() < 1 ? 1 : queryDTO.getPage(), p.getSize(), p.getTotalElements(), p.getTotalPages()));
            this.out(response, map);
        }catch (Exception e){
            Map map=this.getFaild(0);
            map.put("msg",e.getMessage());
            this.out(response, map);
        }


    }



    //发表寻找
    @RequestMapping(value = "new")
    public void add(HttpServletRequest request, HttpServletResponse response,DiscoveryAudit discoveryAudit,String accessToken) {
        User user=this.checkAccessTocken(response, accessToken);
        if(null==user){
            return ;
        }
        discoveryAudit.setUser(user);
        Object obj= this.discoveryService.getDiscoveryAuditDAO().save(discoveryAudit);
        Map map=null;
        if(null==obj){
            map=this.getFaild(0);
        }else{
            map=this.getSuccess();

        }
        super.out(response, map);
    }
    //查找寻找对象
    @RequestMapping(value = "detail")
    public void detail(HttpServletRequest request, HttpServletResponse response,int id,int status) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Object obj= this.discoveryService.findDiscoveryById(id, status);
        Map map=null;
        if(null==obj){
            map=this.getFaild(0);
        }else{
            map=this.getSuccess();
            Map userMap=BeanInfoUtil.bean2map(MethodUtils.invokeMethod(obj,"getUser",null),"handler,hibernateLazyInitializer,accessToken,password");
            Map discoveryMap=BeanInfoUtil.bean2map(obj);
            discoveryMap.put("user", userMap);
            map.put("data", discoveryMap);

        }
        super.out(response, map);
    }

    //增加举报对象
    @RequestMapping(value = "accusation")
    public void accusation(HttpServletRequest request, HttpServletResponse response,int id,String accessToken,int type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //检查令牌
        User user=this.checkAccessTocken(response, accessToken);
        if(null==user){
            return ;
        }
        Discovery discovery= this.discoveryService.findById(id);
        Map map=null;
        if(null==discovery){
            map=this.getFaild(0);
        }else{
            map=this.getSuccess();
            Accusation accusation=new Accusation();
            accusation.setUser(user);
            accusation.setDiscovery(discovery);
            if(type==0){
                type=5;
            }
            accusation.setType(type);
            this.discoveryService.getAccusationDAO().save(accusation);

        }
        super.out(response, map);
    }

    //用户贴文
    @RequestMapping(value = "userList")
    public void userList(HttpServletRequest request, HttpServletResponse response,int userId,String accessToken,QueryDTO queryDTO,int type){

        User user=null;
        if(!StringUtils.isBlank(accessToken)){
            //检查令牌
            user=this.checkAccessTocken(response, accessToken);
            if(null==user){
                return ;
            }
        }

        type=type==0?1:type; //1表示通过的帖子，2表示未通过的帖子

        queryDTO.setOrder("-createAt");
        int queryId=userId;
        if(user!=null && user.getId()==userId || userId==0 ){
            queryId=user.getId();
        }else{
            user=this.userService.findById(userId);
        }
        Page p=discoveryService.findDiscoverysByUserId(queryDTO, queryId, type);

        Map map=this.getSuccess();
        //获取数据
        List list= BeanInfoUtil.list2map(p.getContent());
        Map dataMap=new HashMap();
        dataMap.put("discoveryList",list);
        dataMap.put("user",BeanInfoUtil.bean2map(user,"handler,hibernateLazyInitializer,accessToken,password"));

        map.put("data",dataMap);
        map.put("extra",this.putPage(queryDTO.getPage()<1?1:queryDTO.getPage(),p.getSize(),p.getTotalElements(),p.getTotalPages()));
        super.out(response, map);
    }


    //采纳答案
    @RequestMapping(value = "adoptAnswer")
    public void adoptAnswer(HttpServletRequest request, HttpServletResponse response,String accessToken,int id,int replyId){

        //检查令牌
        User user=this.checkAccessTocken(response, accessToken);
        if(null==user){
            return ;
        }

        Discovery discovery=this.discoveryService.findById(id);
        discovery.setAdoptAnswer(replyId);
        Object obj=this.discoveryService.save(discovery);

        Map map=null;
        if(null==obj){
            map=this.getFaild(0);
        }else {
            map = this.getSuccess();
        }
        super.out(response, map);
    }

    //审核 5次里面3次通过，3次否掉就干掉 通过的话放到审核表中
    //status  1过 2不过
    @RequestMapping(value = "review")
    public void review(HttpServletRequest request, HttpServletResponse response,String accessToken,int id,int status){

        //检查令牌
        User user=this.checkAccessTocken(response, accessToken);
        if(null==user){
            return ;
        }

        boolean flag=this.discoveryService.review(id, status);

        Map map=null;
        if(!flag){
            map=this.getFaild(0);
            map.put("msg","对象不存在");
        }else {
            map = this.getSuccess();
        }
        super.out(response, map);
    }

    //修改帖子
    @RequestMapping(value = "modify")
    public void modify(HttpServletRequest request, HttpServletResponse response,String accessToken,Discovery d){
        //检查令牌
        User user=this.checkAccessTocken(response, accessToken);
        if(null==user){
            return ;
        }
        if(d.getStatus()==0 || d.getStatus()==3 ||d.getStatus()==4 ){
            Map map=this.getSuccess();
            map.put("msg","帖子已结贴或者已经被拒绝不能修改");
            this.out(response, map);
            return ;
        }


        Map map=null;
        Object obj=this.discoveryService.modifiy(user.getId(), d);
        if(obj==null){
            map=this.getFaild(0);
            map.put("msg","修改对象失败");
        }else {
            map = this.getSuccess();
            map.put("data",BeanInfoUtil.bean2map(obj));
        }
        super.out(response, map);
    }


    private  double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public  double getDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * 6378137;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


}
