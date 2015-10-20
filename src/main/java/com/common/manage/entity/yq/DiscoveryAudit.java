package com.common.manage.entity.yq;

import com.xyj.common.tool.EncodeUtil;

import javax.persistence.*;

/**
 * @classDescription:寻找审核对象
 * @author:xiayingjie
 * @createTime:15/8/29
 */
@Entity
@Table(name = "discoveryAudit",indexes={@Index(columnList="discoveryId")})
public class DiscoveryAudit extends BaseEntity{

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
    @Column(name="status")
    private int status=2;//1表示审核通过 2表示需要审核 3.结贴 4被拒绝
    @Column(name="agreeCount")
    private int agreeCount;//投票同意次数
    @Column(name="refuseCount")
    private int refusCount;//投票拒绝次数
    @Column(name="discoveryId")
    private int discoveryId; //如果是修改已经审核过的帖子，那么此处会关联其id
    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "userId")
    private User user ;//模块


    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public int getRefusCount() {
        return refusCount;
    }

    public void setRefusCount(int refusCount) {
        this.refusCount = refusCount;
    }

    public int getDiscoveryId() {
        return discoveryId;
    }

    public void setDiscoveryId(int discoveryId) {
        this.discoveryId = discoveryId;
    }

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
}
