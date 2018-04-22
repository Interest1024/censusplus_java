package com.example.service.impl;

import com.example.dao.CensusDataDao;
import com.example.model.Suburb;
import com.example.service.CensusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by E470 on 4/14/2018.
 * return census data
 */
@Service
public class CensusDataServiceImpl implements CensusDataService {

    @Autowired
    CensusDataDao censusDataDao;

    /**
     * get detail census data by suburb for tables and charts data from
     * @param inputSuburb the input suburb
     * @return a list of map, including all data which will be shown in panels on the left side of the result.html
     */
    @Override
    public List<Map<String, Object>> getCensusDataBySuburb(Suburb inputSuburb){
        return censusDataDao.getCensusDataBySuburb(inputSuburb);
    }

    public List<List<String>> getCensusDataBySuburbStats(String input_ssc, String stat, String type, int no){
        return censusDataDao.getCensusDataBySuburbStats(input_ssc, stat, type, no);
    }

    @Override
    public Map<String, Object> getMetadataByStats(List<String> searchStats, int numClasses){
        return censusDataDao.getMetadataByStats(searchStats, numClasses);
    }

    @Override
    public Map<String,Object> getMapBoundary(String stat_id, int zoom_level, String boundary_name, String table_id,
                                                 String map_left, String map_bottom, String map_right, String map_top){
        return censusDataDao.getMapBoundary(stat_id, zoom_level, boundary_name, table_id,
                map_left, map_bottom, map_right, map_top);
    }

}
