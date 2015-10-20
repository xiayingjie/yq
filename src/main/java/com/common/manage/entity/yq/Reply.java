package com.common.manage.entity.yq;

import com.xyj.common.tool.EncodeUtil;
import org.apache.http.client.utils.URLEncodedUtils;

import javax.persistence.*;

/**
 * @classDescription:评论对象
 * @author:xiayingjie
 * @createTime:15/8/9
 */
@Entity
@Table(name = "reply")
public class Reply extends BaseEntity {
    @Column(name="tweet",length=200, nullable=false)
    private String tweet="";//评论内容

    @Column(name="replyUserId",length=200, nullable=true)
    private int replyUserId=0;

    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "discoveryId")
    private Discovery discovery;
    //多对一
    @ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.REFRESH, CascadeType.MERGE} ,optional = false)
    @JoinColumn(name = "userId")
    private User user ;//模块


    public String getTweet() {
        return  EncodeUtil.urlDecode(tweet);
    }

    public void setTweet(String tweet) {
        this.tweet = EncodeUtil.urlEncode(tweet);
    }

    public int getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        this.replyUserId = replyUserId;
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
