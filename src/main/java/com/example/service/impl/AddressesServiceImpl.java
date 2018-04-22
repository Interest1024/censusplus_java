package com.example.service.impl;

/**
 * Created by E470 on 4/12/2018.
 * return detail address related data
 */


import java.util.List;
import java.util.ArrayList;

import com.example.model.Address;
import com.example.service.AddressesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.AddressDao;

@Service
public class AddressesServiceImpl implements AddressesService {

    @Autowired
    AddressDao addressesDao;

    @Override
    public List<Address> findAddressObjectByKeyWords(String keyWords){
        List<Address> listAdd = addressesDao.findNsw_addressByKeyWords(keyWords);
        return listAdd;
    }


    /*
    only return the address column in the Address
     */
    @Override
    public List<String> findAddressStringByKeyWords(String keyWords){
        List<Address> listAll = addressesDao.findNsw_addressByKeyWords(keyWords);

        List<String> listAdd = new ArrayList<String>();
        for(Address row : listAll){
            listAdd.add( row.getAddress() );
        }

        return listAdd;
    }

    /**
     * get the Address associated with the input address
     * @param address
     * @return
     */
    @Override
    public Address findAddressObjectByAddress(String address){
        Address nswAdd = addressesDao.findAddressStringByAddress(address);

        return nswAdd;
    }


}
