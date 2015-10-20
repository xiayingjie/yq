package com.common.manage.entity.yq;

import javax.persistence.*;

/**
 * @classDescription:消息对象
 * @author:xiayingjie
 * @createTime:15/8/9
 */
@Entity
@Table(name = "message")
public class Message   extends BaseEntity{
    @Column(name="content",length=200, nullable=false)
    private String content;//评论内容

    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "userId")
    private User user ;//模块

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
