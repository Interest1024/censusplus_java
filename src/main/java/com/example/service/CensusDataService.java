package com.example.service;

import com.example.model.Suburb;

import java.util.List;
import java.util.Map;

/**
 * Created by E470 on 4/14/2018.
 */
public interface CensusDataService {

    List<Map<String, Object>> getCensusDataBySuburb(Suburb inputSuburb);
    Map<String, Object> getMetadataByStats(List<String> searchStats, int numClasses);
    Map<String,Object> getMapBoundary(String stat_id, int zoom_level, String boundary_name, String table_id,
                                      String map_left, String map_bottom, String map_right, String map_top);

}
