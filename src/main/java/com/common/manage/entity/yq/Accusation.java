package com.common.manage.entity.yq;



import javax.persistence.*;


/**
 * @classDescription:举报
 * @author:xiayingjie
 * @createTime:15/8/29
 */

@Entity
@Table(name = "accusation")
public class Accusation extends BaseEntity{


    @Column(name="type", nullable=false)
    private int type;//    type  广告 1   2 色情  3骚扰 4反对政治 5 其他

    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "discoveryId")
    private Discovery discovery ;//模块
    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "userId")
    private User user ;//模块


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Discovery getDiscovery() {
        return discovery;
    }

    public void setDiscovery(Discovery discovery) {
        this.discovery = discovery;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
