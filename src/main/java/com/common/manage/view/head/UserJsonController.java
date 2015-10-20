package com.common.manage.view.head;

import com.common.manage.entity.UserInfo;
import com.common.manage.entity.yq.User;
import com.common.manage.service.account.AccountService;
import com.common.manage.service.account.MenuService;
import com.common.manage.service.yq.UserService;
import com.common.manage.tool.RedisKey;
import com.common.manage.view.BaseController;
import com.google.common.reflect.TypeToken;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.tool.RedisSpringUtil;
import com.xyj.common.web.query.QueryDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @classDescription: 用户模块
 * @author:xiayingjie
 * @createTime:15/5/26
 */
@Controller
@RequestMapping(value = "/user")
public class UserJsonController extends BaseController {

    @Autowired
    private UserService userService;

    //注册
    @RequestMapping(value = "register")
    public void register(HttpServletRequest request,HttpServletResponse response, User user) {
        //用户名存在则直接返回
        boolean flag=this.userService.checkName(user.getLoginName(), 0);
        Map map=this.checkNameCode(response,flag);
        if(flag){
            return;
        }

        user.setAccessToken(UUID.randomUUID().toString().replaceAll("-", ""));
        Object obj=userService.save(user);

        if(null==obj){
            map=this.getFaild(0);
        }else{
            map=this.getSuccess();
            map.put("data",BeanInfoUtil.bean2map(obj,"password"));

        }
        super.out(response, map);
    }
    //查询用户信息
    @RequestMapping(value = "info")
    public void info(HttpServletRequest request,HttpServletResponse response, int uid) {
        //查询用户信息
        User user=this.userService.findById(uid);
        Map map;
        if(null==user){
            map=this.getFaild(0);
        }else{
            map=this.getSuccess();
            map.put("data",BeanInfoUtil.bean2map(user,"accessToken,loginName,password,createAt,messageCount"));

        }
        super.out(response, map);
    }

    //查询clientId用户信息
    @RequestMapping(value = "listByClientId")
    public void listByClientId(HttpServletRequest request,HttpServletResponse response, String clientIds) {
        try {
            if(StringUtils.isBlank(clientIds)){
                throw new Exception("clientIds不能为空");
            }
            ArrayList<String> ls=this.gson.fromJson(clientIds,new TypeToken<ArrayList<String>>(){}.getType());
            //String []ids=clientIds.substring(1,clientIds.length()-2).split(",");
            List list = new ArrayList();
            for (String id : ls) {
                list.add(Integer.parseInt(StringUtils.substringAfter(id, "xz")));
            }
            //查询用户信息
            List userList = (List) this.userService.getDAO().findAll(list);
            Map map = this.getSuccess();
            map.put("data", BeanInfoUtil.list2map(userList, "accessToken,loginName,password,createAt,messageCount"));

            super.out(response, map);
        }catch (Exception e){
            Map map=this.getFaild(0);
            map.put("msg",e.getMessage());
            super.out(response, map);
        }
    }
    //登陆
    @RequestMapping(value = "login")
    public void login(HttpServletRequest request,HttpServletResponse response, String loginName,String password) {
        User user = this.userService.login(loginName, password);
        Map map;
        if (null == user) {
            map = this.getFaild(0);
            map.put("msg","登陆验证失败");
        } else {
            map = this.getSuccess();
            map.put("data", BeanInfoUtil.bean2map(user, "password"));

        }
        super.out(response, map);
    }
    //检查用户名
    @RequestMapping(value = "checkName")
    public void checkName(HttpServletRequest request,HttpServletResponse response,String name, int type) {

        boolean flag=this.userService.checkName(name, type);
        Map map =this.checkNameCode(response,flag);
        if(!flag) {
            super.out(response, map);
        }

    }

    //修改用户信息
    @RequestMapping(value = "modifyUser")
    public void alterUser(HttpServletRequest request,HttpServletResponse response,User user) {
        User userInfo=this.checkAccessTocken(response,user.getAccessToken());
        if(null==userInfo){
            return;
        }
        //修改生日
        if(user.getBirthday()>0){
            userInfo.setBirthday(user.getBirthday());
        }
        //修改个人简介
        if(!StringUtils.isBlank(user.getTweet())){
            userInfo.setTweet(user.getTweet());
        }
        //修改个人头像
        if(!StringUtils.isBlank(user.getImg())){
            userInfo.setImg(user.getImg());
        }

        //修改个人昵称
        if(!StringUtils.isBlank(user.getNickName())){
            Pattern p = Pattern.compile("^\\d+$");
            Matcher m = p.matcher(userInfo.getNickName());
            if(m.matches()) {
                userInfo.setNickName(user.getNickName());
            }
        }
        //修改性别
        if(user.getSex()>0&&userInfo.getSex()<0){
            userInfo.setSex(user.getSex());
        }
        Object obj=this.userService.save(userInfo);
        Map map=this.getSuccess();
        map.put("data",BeanInfoUtil.bean2map(obj));
        super.out(response, map);
    }

//---------------未完成--------------

    //第三方登陆
    @RequestMapping(value = "thirdLogin")
    public void thirdLogin(HttpServletRequest request,HttpServletResponse response,User user) {
        RedisSpringUtil s;

    }


    //修改密码
    @RequestMapping(value = "modifyPw")
    public void modifyPw(HttpServletRequest request,HttpServletResponse response,String accessToken,String oldPassword,String password) {
        User userInfo = this.checkAccessTocken(response, accessToken);
        if (null == userInfo) {
            return;
        }
        Map map = null;
        if (userInfo.getPassword().equals(oldPassword)) {
            userInfo.setPassword(password);
            this.userService.save(userInfo);
            map = this.getSuccess();
        } else {
            map=this.getFaild(0);
        }
        super.out(response, map);
    }
    //找回密码
    @RequestMapping(value = "retrievePw")
    public void retrievePw(HttpServletRequest request,HttpServletResponse response,String phone,String captcha,String password,int type) {
        Map map=null;
        User userInfo = this.userService.findUserByLoginName(phone);

        if (null != userInfo) {
            //获取key,放入redis中 缓存24小时
            String key=String.format(RedisKey.USER_PHONE_CODE,phone,type);
            Object obj=redisUtil.get(key);
            if(null!=obj && captcha.equals(obj)) {
                userInfo.setPassword(password);
                this.userService.save(userInfo);
                map=this.getSuccess();
                super.out(response,map);
                return;
            }
        }
        map=this.getFaild(0);
        super.out(response, map);

    }



}