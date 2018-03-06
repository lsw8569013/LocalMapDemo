package com.map.demo.bean;

/**
 * Created by wb-lsw350290 on 2017/12/9.
 *
 */
@Deprecated
public class MapData {

    private MapBean busData;

    private MapBean shopData;
    /**
     * 展馆
     */
    private MapBean venueData;

    private MapBean parkData;

    private MapBean washroomData;

//

    public MapBean getBusData() {
        return busData;
    }

    public void setBusData(MapBean busData) {
        this.busData = busData;
    }

    public MapBean getShopData() {
        return shopData;
    }

    public void setShopData(MapBean shopData) {
        this.shopData = shopData;
    }

    public MapBean getVenueData() {
        return venueData;
    }

    public void setVenueData(MapBean venueData) {
        this.venueData = venueData;
    }

    public MapBean getParkData() {
        return parkData;
    }

    public void setParkData(MapBean parkData) {
        this.parkData = parkData;
    }

    public MapBean getWashroomData() {
        return washroomData;
    }

    public void setWashroomData(MapBean washroomData) {
        this.washroomData = washroomData;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "busData=" + busData +
                ", shopData=" + shopData +
                ", venueData=" + venueData +
                ", parkData=" + parkData +
                ", washroomData=" + washroomData +
                '}';
    }
}
