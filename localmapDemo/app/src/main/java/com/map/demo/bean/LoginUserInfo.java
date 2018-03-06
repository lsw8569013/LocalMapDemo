package com.map.demo.bean;

/**
 * Created by zhaoqi on 2017/12/21.
 */

public class LoginUserInfo
{
    private int userId;
    private String userName;
    private String gender;
    private String nationality;
    private String faceId;
    private String faceUrl;
    private String favoriteSports;

    private long loginTime;
    private long lastRecognizeTime;
    private int recognizeCount;


    @Override
    public String toString() {
        return "LoginUserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", faceId='" + faceId + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                ", favoriteSports='" + favoriteSports + '\'' +
                ", loginTime=" + loginTime +
                ", lastRecognizeTime=" + lastRecognizeTime +
                ", recognizeCount=" + recognizeCount +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getLastRecognizeTime() {
        return lastRecognizeTime;
    }

    public void setLastRecognizeTime(long lastRecognizeTime) {
        this.lastRecognizeTime = lastRecognizeTime;
    }

    public int getRecognizeCount() {
        return recognizeCount;
    }

    public void setRecognizeCount(int recognizeCount) {
        this.recognizeCount = recognizeCount;
    }

    public String getFavoriteSports() {
        return favoriteSports;
    }

    public void setFavoriteSports(String favoriteSports) {
        this.favoriteSports = favoriteSports;
    }
}
