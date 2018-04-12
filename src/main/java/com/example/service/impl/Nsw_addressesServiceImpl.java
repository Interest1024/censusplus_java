package com.example.service.impl;

/**
 * Created by E470 on 4/12/2018.
 */


import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.Nsw_addressesDao;
import com.example.model.Nsw_addresses;
import com.example.service.Nsw_addressesService;

@Service
public class Nsw_addressesServiceImpl implements Nsw_addressesService {

    @Autowired
    Nsw_addressesDao nsw_addressesDao;

    @Override
    public List<Nsw_addresses> findNsw_addressByKeyWords(String keyWords){
        List<Nsw_addresses> listAdd = nsw_addressesDao.findNsw_addressByKeyWords(keyWords);
        return listAdd;
    }

    /*
    only return the address column in the Nsw_addresses
     */
    @Override
    public List<String> findAddressByKeyWords(String keyWords){
        List<Nsw_addresses> listAll = nsw_addressesDao.findNsw_addressByKeyWords(keyWords);

        List<String> listAdd = new ArrayList<String>();
        for(Nsw_addresses row : listAll){
            listAdd.add( row.getAddress() );
        }

        return listAdd;
    }

}
