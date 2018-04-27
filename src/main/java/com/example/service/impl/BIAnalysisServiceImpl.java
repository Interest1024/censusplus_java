package com.example.service.impl;

/**
 * Created by E470 on 4/12/2018.
 * return detail address related data
 */


import com.example.dao.AddressDao;
import com.example.dao.BIAnalysisDataDao;
import com.example.model.Address;
import com.example.service.BIAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BIAnalysisServiceImpl implements BIAnalysisService {

    @Autowired
    BIAnalysisDataDao bIAnalysisDataDao;

    public List<String> getCsvFiles(){
        return bIAnalysisDataDao.getCsvFiles();
    }

    @Override
    public String readCsvfile(String fileName){
        String result = bIAnalysisDataDao.readCsvFile(fileName);
        return result;
    }
}
