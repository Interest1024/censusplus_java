package com.example.dao.impl;

import com.example.dao.CensusDataDao;
import com.example.model.Suburb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by E470 on 4/14/2018.
 */
@Repository
public class CensusDataDaoImpl extends JdbcDaoSupport implements CensusDataDao {

    @Autowired
    @Qualifier("dataSource")
    javax.sql.DataSource dataSource;


    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public List<Object> getCensusDataBySuburb(Suburb inputSuburb){
        System.out.println("CensusDataDaoImpl::getCensusDataBySuburb:Info Entrance");
        List<Object> result = new ArrayList<Object>(3);
        //hardcode 3
        result.add("g1");
        result.add("g2");
        result.add("g3");

        String sql = "SELECT sequential_id AS id, "
                + "lower(table_number) AS table, "
                + "replace(long_id, '_', ' ') AS description, "
                + "column_heading_description AS type, "
                + "'values' as maptype "
                + "FROM census_2016_data.metadata_stats "
                + "WHERE sequential_id IN ('G1','G2','G3') "
                + "ORDER BY sequential_id";
        //hardcode G1, G2, G3

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        if( rows.size() <= 0){
            return null;
        }

        //resort the census data item, give no for each item
        for(Map<String, Object> row: rows) {
            Map<String, Object> featureDict = new HashMap<String,Object>(row);

            featureDict.put("id", featureDict.get("id").toString().toLowerCase());
            featureDict.put("table", featureDict.get("table").toString().toLowerCase());
            if (featureDict.get("id").toString().toLowerCase().equals("g1")){
                featureDict.put("no","2");
                result.set(1,featureDict);
            }
            else if(featureDict.get("id").toString().toLowerCase().equals("g2") ){
                featureDict.put("no","3");
                result.set(2,featureDict);

            }
            else if(featureDict.get("id").toString().toLowerCase().equals("g3") ){
                featureDict.put("no","1");
                result.set(0,featureDict);
            }
        }

        sql = "SELECT tab.g1, tab.g2, tab.g3, tab.g4, tab.g5, tab.g6, tab.g7, tab.g8, tab.g9, "
                + "tab.g10, tab.g11, tab.g12, tab.g13, tab.g14, tab.g15, tab.g16, tab.g17, tab.g18, tab.g19, "
                + "tab.g20, tab.g21, tab.g22, tab.g23, tab.g24, tab.g25, tab.g26, tab.g27, tab.g28, tab.g29, "
                + "tab.g30, tab.g31, tab.g32, tab.g33, tab.g34, tab.g35, tab.g36 "
                + "FROM census_2016_data.ssc_g01 AS tab "
                + "WHERE tab.region_id = '"+inputSuburb.getSsc_code()+"'"
                + "LIMIT 1";

        List<Map<String, Object>> rows2 = getJdbcTemplate().queryForList(sql);

        if( rows2.size() <= 0){
            return null;
        }

        Map<String, Object> row2 = rows2.get(0);
        for( Object resultObject : result){
            Map<String, Object> item = (Map<String, Object>)resultObject;

            if (item.get("id").toString().toLowerCase().equals("g3")){
                List<List<String>> table1 = new ArrayList<List<String>>();
                table1.add(Arrays.asList("suburb", "year", "value"));
                table1.add(Arrays.asList(inputSuburb.getName(), "2016", row2.get("g3").toString()));
                //g3 has no 1, and is in result[0]
                Map<String,Object> resultItem = (Map<String,Object>)(result.get(0));
                resultItem.put("numoftable",Integer.parseInt("1"));
                resultItem.put("table",table1);
                resultItem.put("numofchart",Integer.parseInt("1"));
                resultItem.put("chart","chart_g3_01.png");
            }
            else if(item.get("id").toString().toLowerCase().equals("g2")) {
                List<List<String>> table1 = new ArrayList<List<String>>();
                table1.add(Arrays.asList("suburb", "year", "value"));
                table1.add(Arrays.asList(inputSuburb.getName(), "2016", row2.get("g2").toString()));
                //g3 has no 1, and is in result[2]
                Map<String,Object> resultItem = (Map<String,Object>)(result.get(2));
                resultItem.put("numoftable",Integer.parseInt("1"));
                resultItem.put("table",table1);
                resultItem.put("numofchart",Integer.parseInt("1"));
                resultItem.put("chart","chart_g2_01.png");
            }
            else if(item.get("id").toString().toLowerCase().equals("g1")) {
                List<List<String>> table1 = new ArrayList<List<String>>();
                table1.add(Arrays.asList("suburb", "year", "value"));
                table1.add(Arrays.asList(inputSuburb.getName(), "2016", row2.get("g1").toString()));
                //g3 has no 1, and is in result[1]
                Map<String,Object> resultItem = (Map<String,Object>)(result.get(1));
                resultItem.put("numoftable",Integer.parseInt("1")); //now only support one table
                resultItem.put("table",table1);
                resultItem.put("numofchart",Integer.parseInt("1"));
                resultItem.put("chart","chart_g1_01.png");
            }
        }

        return result;
    }



}
