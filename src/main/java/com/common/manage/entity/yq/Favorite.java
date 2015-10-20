package com.common.manage.entity.yq;

import javax.persistence.*;

/**
 * @classDescription:收藏列表
 * @author:xiayingjie
 * @createTime:15/8/9
 */
@Entity
@Table(name = "favorite")
public class Favorite   extends BaseEntity{
    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "discoveryId")
    private Discovery discovery ;//信息对象

    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "userId")
    private User user ;//用户对象



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Discovery getDiscovery() {
        return discovery;
    }

    public void setDiscovery(Discovery discovery) {
        this.discovery = discovery;
    }
}
