package com.common.manage.tool;

/**
 * Created by xiayingjie on 15/8/28.
 */

public interface RedisKey {
    //用户发送短信验证 0为注册，1为找回密码
    public static String  USER_PHONE_CODE="user:%s:phonecode:%s";

    //用户粉丝列表
    public static String  USER_FANS_IDS="user_fans:%s:ids_zset";

    //用户关注列表
    public static String USER_FLLOWS_IDS="user_fllow:%s:ids_zset";

    //我的关注和他人关注列表
    public static String USER_FANSFLLOWS_IDS="user_fllow_fans:%s:ids_zset";

    //我的关注和他人关注列表
    public static String USER_FLLOWS_FLLOWS_IDS="user_fllow:%s:other_fllow:%s:ids_zset";

    //我的关注和他人粉丝列表
    public static String USER_FLLOWS_FANS_IDS="user_fllow:%s:other_fans:%s:ids_zset";

}
