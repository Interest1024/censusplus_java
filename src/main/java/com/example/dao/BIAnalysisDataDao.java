package com.example.dao;

/**
 * Created by E470 on 4/12/2018.
 */

import com.example.model.Address;

import java.util.List;

public interface BIAnalysisDataDao {

    List<String> getCsvFiles();
    String readCsvFile(String fileName);
}
