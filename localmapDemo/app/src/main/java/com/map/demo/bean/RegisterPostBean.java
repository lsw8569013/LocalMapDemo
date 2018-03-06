package com.map.demo.bean;

/**
 * Created by wb-yl349288 on 2017/12/5.
 */

public class RegisterPostBean {
    /**
     * code : 200
     * success : true
     * model : {"country":"Chinese","id":10,"userFullName":"123","gender":"male"}
     */

    private int code;
    private boolean success;
    private ModelBean model;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ModelBean getModel() {
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
    }

    public static class ModelBean {
        /**
         * country : Chinese
         * id : 10
         * userFullName : 123
         * gender : male
         */

        private String country;
        private int id;
        private String userFullName;
        private String gender;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(String userFullName) {
            this.userFullName = userFullName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "ModelBean{" +
                    "country='" + country + '\'' +
                    ", id=" + id +
                    ", userFullName='" + userFullName + '\'' +
                    ", gender='" + gender + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RegisterPostBean{" +
                "code=" + code +
                ", success=" + success +
                ", model=" + model +
                '}';
    }
}
