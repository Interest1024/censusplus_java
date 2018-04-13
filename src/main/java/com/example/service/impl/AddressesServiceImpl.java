package com.example.service.impl;

/**
 * Created by E470 on 4/12/2018.
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
    AddressDao nsw_addressesDao;

    @Override
    public List<Address> findAddressObjectByKeyWords(String keyWords){
        List<Address> listAdd = nsw_addressesDao.findNsw_addressByKeyWords(keyWords);
        return listAdd;
    }


    /*
    only return the address column in the Address
     */
    @Override
    public List<String> findAddressStringByKeyWords(String keyWords){
        List<Address> listAll = nsw_addressesDao.findNsw_addressByKeyWords(keyWords);

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
        Address nswAdd = nsw_addressesDao.findAddressStringByAddress(address);

        return nswAdd;
    }


}
