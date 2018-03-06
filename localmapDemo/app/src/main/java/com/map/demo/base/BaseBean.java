package com.map.demo.base;

/**
 * Created by wb-yl349288 on 2017/11/29.
 */

public class BaseBean {

    /**
     * code : 200
     * message : success
     * result :
     */

    public int code;
    public String message;
    public String result;

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
