package com.zhangjingbo.account.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class MyPage<T> extends Page {


    private String path;

    public MyPage(long current, long size) {
        super(current, size, 0);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTotalPage() {
        return (int) Math.ceil(((double) this.total) / ((double) this.size));
    }

    public int getFrom() {
        int from = (int) (current - 2);
        return from < 1 ? 1 : from;
    }

    public int getTo() {
        int to = (int) (current + 2);
        int totalPage = getTotalPage();
        return to > totalPage ? totalPage : to;
    }
}
