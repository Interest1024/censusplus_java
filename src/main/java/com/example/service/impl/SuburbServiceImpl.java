package com.example.service.impl;

import com.example.dao.SuburbDao;
import com.example.model.Suburb;
import com.example.service.SuburbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by E470 on 4/13/2018.
 * return Suburb related data
 */

@Service
public class SuburbServiceImpl implements SuburbService {

    @Autowired
    SuburbDao suburbDao;

    @Override
    public Suburb findSuburbByMb_2016_code(String inputMb_2016_code){
        Suburb subu = suburbDao.findSuburbByMb_2016_code(inputMb_2016_code);
        return subu;
    }

}
