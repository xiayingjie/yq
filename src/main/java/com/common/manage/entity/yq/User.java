package com.common.manage.entity.yq;

import com.common.manage.entity.IdEntity;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.sql.Date;

/**
 * @classDescription:用户对象
 * @author:xiayingjie
 * @createTime:15/8/9
 */
//
@Entity
@Table(name = "user",indexes={@Index(columnList="loginName"),@Index(columnList="nickName"),@Index(columnList="accessToken")})
//@DynamicUpdate(true)
public class User  extends BaseEntity implements java.io.Serializable {
    @Column(name="accessToken",length=50, nullable=false)
    private String accessToken;//令牌

    @Column(name="loginName",length=50, nullable=false)
    private String loginName ;//用户名  一般为手机号码

    @Column(name="nickName",length=50, nullable=false)
    private String nickName=String.valueOf(System.currentTimeMillis());//别名
    @Column(name="password",length=50, nullable=false)
    private String password;//密码

    @Column(name="img",length=200)
    private String img;//用户头像
    @Column(name="tweet",length=200)
    private String tweet;//用户简介
    @Column(name="sex",length=50)
    private int sex=-1;//0表示女，1表示男 -1表示没设置
    @Column(name="birthday",length=50)
    private long birthday=0;//生日
    @Column(name="followCount",length=50)
    private int followCount=0;//关注数 默认为0
    @Column(name="fansCount",length=50)
    private int fansCount=0;//粉丝数 默认为0
    @Column(name="messageCount",length=50)
    private int messageCount=0;//消息数 默认为0
    @Column(name="status",length=50)
    private int status=1;//用户状态 1表示可用，0表示不可用 默认为1

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }


    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }





    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public static void main(String[] args) {
        User user=new User();
        System.out.println(user.getCreateAt());
    }
}
