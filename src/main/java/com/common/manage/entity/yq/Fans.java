package com.common.manage.entity.yq;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @classDescription:粉丝对象
 * @author:xiayingjie
 * @createTime:15/8/9
 */


@Entity
@Table(name = "fans",indexes={@Index(columnList="userId")})
public class Fans extends BaseEntity{
    @Column(name="userId")
    private int userId;//用户id
    @Column(name="fansId")
    private int fansId;//fans id
//    @Column(name="createAt",nullable=false)
//    private long createAt;//注册时间

    public Fans(){}
    public Fans(int userId,int fansId){
        this.userId=userId;
        this.fansId=fansId;
  //      this.createAt=System.currentTimeMillis();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFansId() {
        return fansId;
    }

    public void setFansId(int fansId) {
        this.fansId = fansId;
    }


}
