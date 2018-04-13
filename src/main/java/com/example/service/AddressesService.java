package com.example.service;

/**
 * Created by E470 on 4/12/2018.
 */

import java.util.List;

import com.example.model.Address;

public interface AddressesService {
    /*
    void insert(Customer cus);
    void insertBatch(List<Customer> customers);
    void loadAllCustomer();
    void getCustomerById(long cust_id);
    void getCustomerNameById(long cust_id);
    void getTotalNumerCustomer();
    */
    List<Address> findAddressObjectByKeyWords(String keyWords);
    List<String> findAddressStringByKeyWords(String keyWords);
    Address findAddressObjectByAddress(String address);

}
