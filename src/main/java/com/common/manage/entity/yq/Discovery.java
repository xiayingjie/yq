package com.common.manage.entity.yq;


import com.xyj.common.tool.EncodeUtil;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;


/**
 * @classDescription:寻找对象
 * @author:xiayingjie
 * @createTime:15/8/29
 */

@Entity
@Table(name = "discovery")
public class Discovery extends BaseEntity{


    @Column(name="detail",length=200, nullable=false)
    private String detail;//   信息描述


    @Column(name="category", nullable=false)
    private String    category;//     物品类型 1物品 2人 3其他


    @Column(name="type", nullable=false)
    private int type;//   发布类型 1表示寻找，2表示招领


    @Column(name="reward")
    private int reward ;// 酬金   非必须


    @Column(name="discoveryTime")
    private long discoveryTime ;//丢失时间

    @Column(name="discoveryAddress")
    private String discoveryAddress ;//丢失地点

    @Column(name="latitude",length=10)
    private String latitude;//  纬度

    @Column(name="longitude",length=10)
    private String longitude;// 经度

    @Column(name="image",length=200)
    private String image;//   图片  非必须

    @Column(name="infoAddress",length=200)
    private String infoAddress;// 物品丢失或者捡到的位置

    @Column(name="favoriteCount")
    private int favoriteCount=0;//收藏数，

    @Column(name="replyCount")
    private int replyCount=0;//评论数


    @Column(name="status")
    private int status=1;//1表示审核通过 2表示需要审核 3.结贴 4被拒绝

    @Column(name="adoptAnswer")
    private int adoptAnswer=0;//采纳答案 reply


    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "userId")
    private User user ;//模块

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public long getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(long discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public String getDiscoveryAddress() {
        return discoveryAddress;
    }

    public void setDiscoveryAddress(String discoveryAddress) {
        this.discoveryAddress = discoveryAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = EncodeUtil.htmlEscape(image);
    }

    public String getInfoAddress() {
        return infoAddress;
    }

    public void setInfoAddress(String infoAddress) {
        this.infoAddress = infoAddress;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAdoptAnswer() {
        return adoptAnswer;
    }

    public void setAdoptAnswer(int adoptAnswer) {
        this.adoptAnswer = adoptAnswer;
    }
}
