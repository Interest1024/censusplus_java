package com.example.service;

/**
 * Created by E470 on 4/12/2018.
 */

import java.util.List;

import com.example.model.Nsw_addresses;

public interface Nsw_addressesService {
    /*
    void insert(Customer cus);
    void insertBatch(List<Customer> customers);
    void loadAllCustomer();
    void getCustomerById(long cust_id);
    void getCustomerNameById(long cust_id);
    void getTotalNumerCustomer();
    */
    List<Nsw_addresses> findNsw_addressByKeyWords(String keyWords);
    List<String> findAddressByKeyWords(String keyWords);
}
