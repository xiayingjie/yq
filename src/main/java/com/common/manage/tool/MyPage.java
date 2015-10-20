package com.common.manage.tool;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/9/13
 */

public class MyPage<T> {
    private int page;
    private int size;
    private long allCount;
    private List<T> list= Lists.newArrayList();
    private long allPage;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getAllCount() {
        return allCount;
    }
    public long getAllPage(){
        if(allCount>0) {
            return this.allCount % size > 0 ? allCount / size + 1 : allCount / size;
        }else{
            return 0;
        }
    }

    public void setAllCount(long allCount) {
        this.allCount = allCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
