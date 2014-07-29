package org.jplus.hyb.database.util;

import java.util.List;

/**
 * 分页要用到的
 * @author hyberbin
 */
public class Pager<T> {

    /** 当前页 */
    private Integer current;
    /** 每一页的条数 */
    private Integer size;
    /** 总共多少条 */
    private Integer items;
    /**下一页*/
    private Integer nextPage;
    /**上一页*/
    private Integer upPage;
    /**总页数*/
    private Integer totalPage;
    /**第一页*/
    private Integer first;
    /**页面数据*/
    private List<T> data;

    public Pager(Integer size) {
        this();
        this.size = size;
    }

    public Pager() {
        current = 1;
        size = 5;
        items = 0;
        nextPage = 1;
        upPage = 1;
        totalPage = 1;
        first = 1;
    }

    public Integer getCurrent() {
        if (current < 1) {
            current = 1;
        }
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public Integer getTop() {
        return (current - 1) >= 0 ? (current - 1)* size : 0;
    }

    public Integer getNextPage() {
        if (current >= getTotalPage()) {
            nextPage = current;
        } else {
            nextPage = current + 1;
        }
        return nextPage;
    }

    public Integer getUpPage() {
        if (current > 1) {
            upPage = current - 1;
        } else {
            upPage = 1;
        }
        return upPage;
    }

    public Integer getTotalPage() {
        totalPage = items % size == 0 ? items / size : items / size + 1;
        return totalPage;
    }

    public Integer getFirst() {
        first = 1;
        return first;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

   
    
}
