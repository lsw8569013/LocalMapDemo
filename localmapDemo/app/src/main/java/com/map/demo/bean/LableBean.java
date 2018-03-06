package com.map.demo.bean;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by wb-lsw350290 on 2017/12/9.
 * 标注
 */

public class LableBean {

    private int id;
    /**
     * 标注类型  1 展馆 2 shop 3 停车场 4 公交站 5 wc 6 餐厅 ，888 我的位置
     */
    private int lableType;
    private int pos_x;
    private int pos_y;
    private String name;
    private String description;
    private String image_url;
    private String icon;
    private int goTime;

    public int getGoTime() {
        return goTime;
    }

    public void setGoTime(int goTime) {
        this.goTime = goTime;
    }

    private ArrayList<Point> path;

    public ArrayList<Point> getPath() {
        return path;
    }

    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLableType() {
        return lableType;
    }

    public void setLableType(int lableType) {
        this.lableType = lableType;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "LableBean{" +
                "id=" + id +
                ", lableType=" + lableType +
                ", pos_x=" + pos_x +
                ", pos_y=" + pos_y +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
