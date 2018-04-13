package com.example.model;

/**
 * Created by E470 on 4/12/2018.
 */

import java.io.Serializable;

public class Address implements Serializable{
    private String address_detail_pid;
    private String address;
    private String locality_name;
    private String mb_2016_code;

    public Address(){
    }

    public Address(String address_detail_pid, String address, String locality_name, String mb_2016_code) {
        this.address_detail_pid = address_detail_pid;
        this.address = address;
        this.locality_name = locality_name;
        this.mb_2016_code = mb_2016_code;
    }

    public String getAddress_detail_pid() {
        return address_detail_pid;
    }
    public void setAddress_detail_pid(String address_detail_pid) {
        this.address_detail_pid = address_detail_pid;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLocality_name() {
        return locality_name;
    }
    public void setLocality_name(String locality_name) {
        this.locality_name = locality_name;
    }
    public void setMb_2016_code(String mb_2016_code) {
        this.mb_2016_code = mb_2016_code;
    }
    public String getMb_2016_code() {
        return this.mb_2016_code;
    }

    @Override
    public String toString() {
        return "Customer [address_detail_pid=" + address_detail_pid
                +", address=" + address
                + ", locality_name=" + locality_name
                + ", mb_2016_code=" + mb_2016_code
                + "]";
    }
}
