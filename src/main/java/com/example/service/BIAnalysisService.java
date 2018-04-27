package com.example.service;

/**
 * Created by E470 on 4/12/2018.
 */

import com.example.model.Address;

import java.util.List;

public interface BIAnalysisService {

    List<String> getCsvFiles();
    String readCsvfile(String fileName);

}
