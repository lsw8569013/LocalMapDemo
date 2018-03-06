package com.map.demo.bean;

import java.util.ArrayList;

/**
 * Created by wb-lsw350290 on 2017/12/9.
 * 某一张地图 ，地图上面有6类图标
 * map 地图 bena
 */
public class MapBean {

    private String name;
    private String description;
    private String image_url;
    private int height;
    /**
     * 1 区别 多个地图 的type  展馆
     */
    private int mapType;
    private int width;

    private LableBean myLocation ;

    private ArrayList<LableBean> venueLableData = new ArrayList<LableBean>();
    private ArrayList<LableBean> busLableData = new ArrayList<LableBean>();
    private ArrayList<LableBean> shopLableData = new ArrayList<LableBean>();
    private ArrayList<LableBean> parkLableData = new ArrayList<LableBean>();
    private ArrayList<LableBean> washroomLableData = new ArrayList<LableBean>();
    private ArrayList<LableBean> RestanruntLableData = new ArrayList<LableBean>();


    public LableBean getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(LableBean myLocation) {
        this.myLocation = myLocation;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ArrayList<LableBean> getVenueLableData() {
        return venueLableData;
    }

    public void setVenueLableData(ArrayList<LableBean> venueLableData) {
        this.venueLableData = venueLableData;
    }

    public ArrayList<LableBean> getBusLableData() {
        return busLableData;
    }

    public void setBusLableData(ArrayList<LableBean> busLableData) {
        this.busLableData = busLableData;
    }

    public ArrayList<LableBean> getShopLableData() {
        return shopLableData;
    }

    public void setShopLableData(ArrayList<LableBean> shopLableData) {
        this.shopLableData = shopLableData;
    }

    public ArrayList<LableBean> getParkLableData() {
        return parkLableData;
    }

    public void setParkLableData(ArrayList<LableBean> parkLableData) {
        this.parkLableData = parkLableData;
    }

    public ArrayList<LableBean> getWashroomLableData() {
        return washroomLableData;
    }

    public void setWashroomLableData(ArrayList<LableBean> washroomLableData) {
        this.washroomLableData = washroomLableData;
    }

    public ArrayList<LableBean> getRestanruntLableData() {
        return RestanruntLableData;
    }

    public void setRestanruntLableData(ArrayList<LableBean> restanruntLableData) {
        RestanruntLableData = restanruntLableData;
    }

    @Override
    public String toString() {
        return "MapBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", height=" + height +
                ", mapType=" + mapType +
                ", width=" + width +
                ", myLocation=" + myLocation +
                ", venueLableData=" + venueLableData +
                ", busLableData=" + busLableData +
                ", shopLableData=" + shopLableData +
                ", parkLableData=" + parkLableData +
                ", washroomLableData=" + washroomLableData +
                ", RestanruntLableData=" + RestanruntLableData +
                '}';
    }
}
