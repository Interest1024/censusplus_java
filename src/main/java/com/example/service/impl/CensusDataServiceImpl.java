package com.example.service.impl;

import com.example.dao.CensusDataDao;
import com.example.model.Suburb;
import com.example.service.CensusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by E470 on 4/14/2018.
 */
@Service
public class CensusDataServiceImpl implements CensusDataService {

    @Autowired
    CensusDataDao censusDataDao;

    @Override
    public List<Object> getCensusDataBySuburb(Suburb inputSuburb){
        return censusDataDao.getCensusDataBySuburb(inputSuburb);
    }
}
