package com.common.manage.view;

import com.common.manage.entity.yq.Favorite;
import com.common.manage.entity.yq.User;
import com.common.manage.service.yq.*;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xyj.common.tool.RedisSpringUtil;
import com.xyj.common.web.BaseService;
import com.xyj.common.web.ServletUtil;
import com.xyj.common.web.query.PropertyFilter;
import com.xyj.common.web.query.QueryDTO;
import com.xyj.common.web.query.SpecificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/5/21
 */

public abstract class BaseController {

    protected Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd")//时间转化为特定格式
//             .setDateFormat(DateFormat.LONG)
                    // .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
            .setPrettyPrinting() //对json结果格式化.
            .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                    //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                    //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
            .create();

    @Autowired
    protected UserService userService;

    @Autowired
    protected DiscoveryService discoveryService;

    @Autowired
    protected FavoriteService favoriteService;

    @Autowired
    protected RelationService relationService;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected RedisSpringUtil redisUtil;


    /**
     * 分页整合
     * @param request
     * @param response
     * @param queryDTO
     * @param service
     * @param clz
     * @param path
     */
    public void list(HttpServletRequest request, HttpServletResponse response,QueryDTO queryDTO,BaseService service,Class clz,String path) {

        Map<String,String> searchMap= ServletUtil.getParametersStartingWith(request, "search");

        //查询结果
        Specification specification= SpecificationUtil.bySearchFilter(searchMap, clz);
        PageRequest pageRequest=SpecificationUtil.getPageRequest(queryDTO);
        Page page = service.findAll(specification,pageRequest);
        //获取查询条件
        Map conditionMap= PropertyFilter.parseCondition(searchMap, queryDTO, path);

        PropertyFilter.setAttribute(request,searchMap);
        request.setAttribute("conditionMap",conditionMap);
        request.setAttribute("pageInfo", page);
        request.getSession().setAttribute("currentPage",path+"?"+PropertyFilter.parseUrl(searchMap,queryDTO));

    }
    /**
     * 分页查询
     * @param queryDTO
     * @param service
     * @param clz
     */
    public Page headList(PropertyFilter pf,QueryDTO queryDTO,BaseService service,Class clz) {
        //查询结果
        Specification specification= SpecificationUtil.bySearchFilter(pf.getFilterList(), clz);
        PageRequest pageRequest=SpecificationUtil.getPageRequest(queryDTO);
        Page page = service.findAll(specification, pageRequest);
        return page;

    }
    /**
     * 分页查询
     * @param queryDTO
     * @param service
     * @param clz
     */
    public Page headList(QueryDTO queryDTO,BaseService service,Class clz) {
        return headList(new PropertyFilter(),queryDTO,service,clz);

    }
    /**
     * 分页查询
     * @param service
     * @param clz
     */
    public Page headList(BaseService service,Class clz) {
        return headList(new PropertyFilter(),new QueryDTO(),service,clz);

    }


    /**
     * 获取成功map
     * @return
     */
    public Map<String,Object> getSuccess() {
       return getCode(1, "ok");

    }

    /**
     * 获取失败的map
     * @param code
     * @return
     */
    public Map<String,Object>getFaild(int code){
        return getCode(0, "error");
    }

    /**
     * 自定义map
     * @param code
     * @param msg
     * @return
     */
    public Map<String,Object>getCode(int code,String msg) {
        Map map=Maps.newHashMap();
        map.put("code",code);
        map.put("msg",msg);
        map.put("data",new Object());
        return map;
    }

    /**
     * 检查登录名code
     * @param flag
     * @return
     */
    public Map checkNameCode(HttpServletResponse response,boolean flag){
        Map map=this.getSuccess();
        if (flag == true) {
            map.put("msg","登陆名已经存在");
            map.put("code",102);
            this.out(response,map);
        } else {
            map.put("code", 1);

        }
        return map;
    }
    /**
     * 检查令牌
     * @param response
     * @param accessTocken
     * @return
     */
    public User checkAccessTocken(HttpServletResponse response,String accessTocken){
        User user=this.userService.checkAccessTocken(accessTocken);
        if(null==user){
            Map map=this.getFaild(100);
            map.put("msg", "令牌错误");

            this.out(response, map);
            return null;
        }
        return user;
    }

    /**
     * 检查令牌
     * @param response
     * @param userId
     * @return
     */
    public User checkUser(HttpServletResponse response,int userId){
        User user=this.userService.findById(userId);
        if(null==user){
            Map map=this.getFaild(101);
            map.put("msg", "用户不存在");

            this.out(response, map);
            return null;
        }
        return user;
    }

    /**
     * 检查对象是否存在
     * @param response
     * @param object
     * @return
     */
    public boolean checkObject(HttpServletResponse response,Object object){
        boolean flag=false;
        if(null==object){
            Map map=this.getFaild(0);
            map.put("msg", "对象不存在");
            this.out(response, map);
        }else{
            flag=true;
        }
        return flag;
    }

    //输出
    protected void out(HttpServletResponse response, Map map) {
        try {
            response.setHeader("Content-Type", "application/json");

            response.getWriter().write(gson.toJson(map));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * 获取分页map
     * @param page
     * @param size
     * @param allSize
     * @param allPage
     * @return
     */
    public Map putPage(int page,int size,long allSize,int allPage){
        Map map=new HashMap();
        map.put("page",page);
        map.put("totalPage",allPage);
        map.put("size",size);
        map.put("totalSize",allSize);
        return map;
    }

    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd")//时间转化为特定格式
//              .setDateFormat(DateFormat.LONG)
                        // .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                .setPrettyPrinting() //对json结果格式化.
                .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
                .create();

        Map map=gson.fromJson("{\"uid\":1,\"message\":\"hello world\"}", HashMap.class);
        System.out.println(map);
    }



}
