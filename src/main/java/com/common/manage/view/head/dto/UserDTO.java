package com.common.manage.view.head.dto;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/9/13
 */

public class UserDTO {

    private int id;
    private String nickName;//别名
    private String img;//用户头像
    private String tweet="";//用户简介
    private int sex;//0表示女，1表示男 -1表示没设置
    private boolean isFollow;//我是否关注它
    private boolean isFans;//是否被我关注

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public boolean getIsFans() {
        return isFans;
    }

    public void setIsFans(boolean isFans) {
        this.isFans = isFans;
    }
}
