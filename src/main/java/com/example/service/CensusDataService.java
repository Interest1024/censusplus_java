package com.example.service;

import com.example.model.Suburb;

import java.util.List;

/**
 * Created by E470 on 4/14/2018.
 */
public interface CensusDataService {

    List<Object> getCensusDataBySuburb(Suburb inputSuburb);
}
