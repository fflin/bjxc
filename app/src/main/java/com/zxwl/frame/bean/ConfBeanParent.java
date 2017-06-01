package com.zxwl.frame.bean;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * author：hw
 * data:2017/5/22 14:32
 * 可收缩的列表的bean的父类
 */
public class ConfBeanParent implements Parent<ConfBean> {
    public String type;
    public int count;
    public List<ConfBean> beanList;

    public ConfBeanParent(String type, int count, List<ConfBean> beanList) {
        this.type = type;
        this.count = count;
        this.beanList = beanList;
    }

    @Override
    public List<ConfBean> getChildList() {
        return beanList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public ConfBean getConfBeanForPosition(int position) {
        return beanList.get(position);
    }
}
