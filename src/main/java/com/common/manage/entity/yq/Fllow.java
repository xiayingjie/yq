package com.common.manage.entity.yq;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @classDescription:关注对象
 * @author:xiayingjie
 * @createTime:15/8/9
 */

@Entity
@Table(name = "fllow",indexes={@Index(columnList="userId")})
public class Fllow extends BaseEntity{
    @Column(name="userId")
    private int userId;//用户id
    @Column(name="fllowId")
    private int fllowId;//关注用户 id
//    @Column(name="createAt",nullable=false)
//    private long createAt;//注册时间
    public Fllow(){}
    public Fllow(int userId,int fllowId){
        this.userId=userId;
        this.fllowId=fllowId;
 //       this.createAt=System.currentTimeMillis();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFllowId() {
        return fllowId;
    }

    public void setFllowId(int fllowId) {
        this.fllowId = fllowId;
    }


}
