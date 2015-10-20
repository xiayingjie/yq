package com.common.manage.entity.yq;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;




/**
 * @classDescription:寻找对象
 * @author:xiayingjie
 * @createTime:15/8/29
 */

@Document(indexName = "test", type = "discovery")
public class Dis {
    @Id
    private int id;//   信息描述


    private String detail;//   信息描述


    private String    category;//     物品类型 1物品 2人 3其他


    private int type;//   发布类型 1表示寻找，2表示招领


    private int reward ;// 酬金   非必须


    private long discoveryTime ;//丢失时间


    private String discoveryAddress ;//丢失地点

    private String sort;//  距离


    private long createAt;//注册时间


    private String location;//geo 位置


    private String image;//   图片  非必须

    private String infoAddress;// 物品丢失或者捡到的位置


    private int favoriteCount=0;//收藏数，


    private int replyCount=0;//评论数


    private int status=1;//1表示审核通过 2表示需要审核 3.结贴 4被拒绝


    private int adoptAnswer=0;//采纳答案 reply


    private UserInfo user ;//模块



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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getAdoptAnswer() {
        return adoptAnswer;
    }

    public void setAdoptAnswer(int adoptAnswer) {
        this.adoptAnswer = adoptAnswer;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }


    public class UserInfo {
        private int  id;
        private String nickName;
        private int sex;
        private String img;

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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
