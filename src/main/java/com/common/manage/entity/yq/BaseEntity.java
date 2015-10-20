package com.common.manage.entity.yq;

import com.common.manage.entity.IdEntity;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/9
 */
@MappedSuperclass
public abstract class BaseEntity extends IdEntity implements java.io.Serializable {

    @Column(name="createAt",nullable=false)
    private long createAt;//注册时间

    public BaseEntity(){
        createAt=System.currentTimeMillis();
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
