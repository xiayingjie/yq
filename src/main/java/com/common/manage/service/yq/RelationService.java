package com.common.manage.service.yq;

import com.common.manage.dao.yq.FansDAO;
import com.common.manage.dao.yq.FllowDAO;
import com.common.manage.dao.yq.UserDAO;
import com.common.manage.entity.yq.Fans;
import com.common.manage.entity.yq.Fllow;
import com.common.manage.entity.yq.User;
import com.common.manage.tool.MyPage;
import com.common.manage.tool.RedisKey;
import com.common.manage.view.head.dto.UserDTO;
import com.google.common.collect.Lists;
import com.xyj.common.tool.RedisSpringUtil;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import com.xyj.common.web.query.SpecificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @classDescription:用户收藏Service
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
@Transactional
public class RelationService extends BaseService<Fans> {
    @Autowired
    private FansDAO fansDAO;

    @Autowired
    private FllowDAO fllowDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RedisSpringUtil redisUtil;

    /**
     * 添加或者取消好友
     * @param user
     * @param userId
     * @param type
     * @return
     */
    public boolean action(User user,int userId,int type){
        boolean flag=false;
        if(user.getId()==userId){
            return flag;
        }
        //关注的对象
        Fllow fllow=new Fllow(user.getId(),userId);
        //粉丝对象
        Fans fans=new Fans(userId,user.getId());

        String userFllows=String.format(RedisKey.USER_FLLOWS_IDS, user.getId());
        String userFans=String.format(RedisKey.USER_FANS_IDS, userId);



        if(type==1){//添加对象更新缓存
            this.fansDAO.save(fans);
            this.fllowDAO.save(fllow);

            initRelationCache(user.getId());
            initRelationCache(userId);
            this.redisUtil.zAdd(userFllows, System.currentTimeMillis(), userId);
            this.redisUtil.zAdd(userFans, System.currentTimeMillis(), user.getId());
            flag=true;

        }else{//删除对象更新缓存
            this.fllowDAO.deleteFllow(user.getId(), userId);
            this.fansDAO.deleteFans(userId, user.getId());
            this.redisUtil.zDelete(userFllows, userId);
            this.redisUtil.zDelete(userFans, user.getId());
            flag=true;
        }
        return flag;

    }

    /**
     * 获取关系列表
     * @param userId
     * @param type  1表示我关注的 2表示粉丝
     * @param page  分页 从1开始s
     * @param size
     * @return
     */
    public MyPage getList(int userId,int otherUserId,int type,int page,int size){

        initRelationCache(userId);
        initRelationCache(otherUserId);
        String key=null;
        MyPage<UserDTO> myPage=new MyPage<UserDTO>();

        //是否是自己的个人关注或者粉丝
        boolean flag=(otherUserId==0||userId==otherUserId);
        if(type==1){
            if(flag){//查看个人关注
                //userFllows
                key=String.format(RedisKey.USER_FLLOWS_IDS, userId);
                myPage=this.getList(myPage, key, page, size, true, false);
                String key1=String.format(RedisKey.USER_FANSFLLOWS_IDS, userId);
                //自己关注的列表和自己的粉丝取交集
                long len=this.redisUtil.zInter(key1, key, String.format(RedisKey.USER_FANS_IDS, userId));
                myPage=this.setRelation(myPage,key1,len,false);
            }else{//查看他人关注 //只查看我是否关注某个用户
                key=String.format(RedisKey.USER_FLLOWS_IDS, otherUserId);
                myPage=this.getList(myPage, key, page, size, false, false);
                //1.我的关注和他人的关注取交集
                String key1=String.format(RedisKey.USER_FLLOWS_FLLOWS_IDS, userId, otherUserId);
                long len=this.redisUtil.zInter(key1,key,String.format(RedisKey.USER_FLLOWS_IDS, userId));
                myPage=this.setRelation(myPage,key1,len,true);

                //2.我的粉丝和他人的关注取交集 ==暂时不取
//                Set set1=this.redisUtil.zInter(key,String.format(RedisKey.USER_FANS_IDS, userId));
//                myPage=this.setRelation(set1,myPage,false);
            }

        }else{

            //userFans
            if(flag){//查看个人粉丝
                key=String.format(RedisKey.USER_FANS_IDS, userId);
                myPage=this.getList(myPage, key, page, size, false, true);
                String key1=String.format(RedisKey.USER_FANSFLLOWS_IDS, userId);
                //自己关注的列表和自己的粉丝取并集
                long len=this.redisUtil.zInter(key1, key, String.format(RedisKey.USER_FLLOWS_IDS, userId));
                myPage=this.setRelation(myPage,key1,len,true);


            }else{//查看他人粉丝
                key=String.format(RedisKey.USER_FANS_IDS, otherUserId);
                myPage=this.getList(myPage, key, page, size, false, false);
                String key1=String.format(RedisKey.USER_FLLOWS_FANS_IDS, userId, otherUserId);
                //1.我的关注和他人的粉丝取交集
                long len=this.redisUtil.zInter(key1,key,String.format(RedisKey.USER_FLLOWS_IDS, userId));
                myPage=this.setRelation(myPage,key1,len,true);
                //2.我的粉丝和它人粉丝取交集 =====性能原因，暂时不取
//                Set set1=this.redisUtil.zInter(key,String.format(RedisKey.USER_FANS_IDS, userId));
//                myPage=this.setRelation(set1, myPage, false);
            }

        }


        return myPage;
    }

    /**
     * redis分页获取list
     * @param mypage
     * @param key
     * @param page
     * @param size
     * @return
     */
    private MyPage getList(MyPage mypage,String key,int page,int size,boolean isFllow,boolean isFans){
        int begin =page<1?1:page-1;
        long allCount=this.redisUtil.zCard(key);
        long end =page*size>allCount?allCount:page*size;

        Set set=this.redisUtil.zRevRange(key, begin, end);

        List<User> list=(List)userDAO.findAll(set);
        List<UserDTO> userDTOList= Lists.newArrayList();
        for(User user:list) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setImg(user.getImg());
            userDTO.setNickName(user.getNickName());
            userDTO.setSex(user.getSex());
            userDTO.setTweet(user.getTweet());
            userDTO.setIsFollow(isFllow);
            userDTO.setIsFans(isFans);
            userDTOList.add(userDTO);
        }
        mypage.setPage(page);
        mypage.setSize(size);
        mypage.setAllCount(allCount);
        mypage.setList(userDTOList);
        return mypage;
    }

    /**
     * 设置关系
     * @param myPage
     * @param flag true为关注，false为粉丝
     * @return
     */
    private MyPage setRelation(MyPage<UserDTO> myPage,String key,long len,boolean flag){
        Set set=null;
        if(len>0){
            set=this.redisUtil.zRevRange(key,0,-1);
        }
        if(set==null){
            return myPage;
        }

        List<UserDTO> list =Lists.newArrayList();
        for(UserDTO userDTO:myPage.getList()){
            if(set.contains(userDTO.getId())){
                if(flag) {
                    userDTO.setIsFollow(true);
                }else{
                    userDTO.setIsFans(true);
                }
            }
            list.add(userDTO);
        }
        myPage.setList(list);
        this.redisUtil.delete(key);
        return myPage;
    }

    /**
     * 初始化用户关系缓存
     * @param userId
     */
    private void initRelationCache(int userId){
        if(userId==0){
            return ;
        }
        String userFllows=String.format(RedisKey.USER_FLLOWS_IDS, userId);
        String userFans=String.format(RedisKey.USER_FANS_IDS, userId);
        Map searchMap=new HashMap();
        searchMap.put("search_userId__EQ_I", String.valueOf(userId));
        Specification specification=null;
        //如果不存在关注列表，则初始化
        if(!this.redisUtil.getRedisTemplate().hasKey(userFllows)){
            specification= SpecificationUtil.bySearchFilter(searchMap, Fllow.class);
            List<Fllow> list=  this.fllowDAO.findAll(specification);

            for (Fllow f : list) {
                this.redisUtil.zAdd(userFllows, f.getCreateAt(), f.getFllowId());
            }

        }
        //如果不存在粉丝列表，则初始化
        if(!this.redisUtil.getRedisTemplate().hasKey(userFans)){
            specification= SpecificationUtil.bySearchFilter(searchMap, Fans.class);
            List<Fans> list=  this.fansDAO.findAll(specification);

            for (Fans f : list) {
                this.redisUtil.zAdd(userFans, f.getCreateAt(), f.getFansId());
            }

        }


    }

    @Override
    public BaseDAO getDAO() {
        return fansDAO;
    }

    public FllowDAO getFllowDAO(){return fllowDAO;};


}
