package com.example.dao;

/**
 * Created by E470 on 4/12/2018.
 */

import java.util.List;

import com.example.model.Address;

public interface AddressDao {
    //void insert(Customer cus);
    //void inserBatch(List<Customer> customers);
    //List<Customer> loadAllCustomer();
    //Customer findCustomerById(long cust_id);
    //String findNameById(long cust_id);
    //int getTotalNumberCustomer();
    List<Address> findNsw_addressByKeyWords(String keyWords);
    Address findAddressStringByAddress(String address);
}
