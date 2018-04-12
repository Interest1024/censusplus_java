package com.example.dao;

/**
 * Created by E470 on 4/12/2018.
 */

import java.util.List;
import com.example.model.Nsw_addresses;

public interface Nsw_addressesDao {
    //void insert(Customer cus);
    //void inserBatch(List<Customer> customers);
    //List<Customer> loadAllCustomer();
    //Customer findCustomerById(long cust_id);
    //String findNameById(long cust_id);
    //int getTotalNumberCustomer();
    List<Nsw_addresses> findNsw_addressByKeyWords(String keyWords);
}
