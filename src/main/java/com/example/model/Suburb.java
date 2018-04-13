package com.example.model;

/**
 * Created by E470 on 4/13/2018.
 */

import java.io.Serializable;


public class Suburb implements Serializable{
    private String ssc_code;
    private String name;
    private Double centreLng;
    private Double centreLat;

    public Suburb(){

    }

    public String getSsc_code() {
        return ssc_code;
    }

    public Double getCentreLng() {
        return centreLng;
    }

    public Double getCentreLat() {
        return centreLat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSsc_code(String ssc_code) {
        this.ssc_code = ssc_code;
    }

    public void setCentreLng(Double centreLng) {
        this.centreLng = centreLng;
    }

    public void setCentreLat(Double centreLat) {
        this.centreLat = centreLat;
    }



}
