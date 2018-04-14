package com.example.dao;

import com.example.model.Suburb;

import java.util.List;

/**
 * Created by E470 on 4/13/2018.
 */
public interface CensusDataDao {

    List<Object> getCensusDataBySuburb(Suburb inputSuburb);
}
