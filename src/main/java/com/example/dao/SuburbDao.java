package com.example.dao;

import com.example.model.Suburb;

/**
 * Created by E470 on 4/13/2018.
 */
public interface SuburbDao {
    Suburb findSuburbByMb_2016_code(String inputMb_2016_code);
}
