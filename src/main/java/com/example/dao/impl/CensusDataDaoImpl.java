package com.example.dao.impl;

import com.example.dao.CensusDataDao;
import com.example.model.Suburb;

import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.*;

//import org.json.*;
//import org.postgis.*;
import com.google.gson.Gson;

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

    /**
     * get all detail census data by suburb
     * @param inputSuburb
     * @return
     */
    @Override
    public List<Map<String, Object>> getCensusDataBySuburb(Suburb inputSuburb){
        //System.out.println("CensusDataDaoImpl::getCensusDataBySuburb:Info Entrance");
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(3);
        /*
        the format of the list is as follows:
        [
            {
            no=1,
            numoftable=1,
            maptype=values,
            description=Total Persons Persons,
            numofchart=1,
            id=g3,
            type=Persons,
            chart=chart_g3_01.png,
            table=[
                    [suburb, year, value],
                    [Summer Hill, 2016, 7311.0]
                    ]
             },
             {
             no=2,
              ...
             }
          ]

         */
        //hardcode 3
        Map<String, Object> mapOfG1 = new HashMap<String, Object>();
        mapOfG1.put("g1","");
        result.add(mapOfG1);
        Map<String, Object> mapOfG2 = new HashMap<String, Object>();
        mapOfG1.put("g2","");
        result.add(mapOfG2);
        Map<String, Object> mapOfG3 = new HashMap<String, Object>();
        mapOfG1.put("g3","");
        result.add(mapOfG3);

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
        for( Map<String, Object> resultObject : result){
            Map<String, Object> item = resultObject;

            if (item.get("id").toString().toLowerCase().equals("g3")){
                List<List<String>> table1 = new ArrayList<List<String>>();
                table1.add(Arrays.asList("suburb", "year", "value"));
                table1.add(Arrays.asList(inputSuburb.getName(), "2016", row2.get("g3").toString()));
                //g3 has no 1, and is in result[0]
                Map<String,Object> resultItem = (Map<String,Object>)(result.get(0));
                resultItem.put("numoftable",Integer.parseInt("1"));
                resultItem.put("table",table1);
                resultItem.put("numofchart",Integer.parseInt("1"));
                resultItem.put("chart","");
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
                resultItem.put("chart","");
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
                resultItem.put("chart","");
            }
        }

        System.out.println(result);
        return result;
    }

    /**
     * get detail census data by suburb for tables and charts data in one panel
     * @param
     * @return
     */
    public List<List<String>> getCensusDataBySuburbStats(String input_ssc, String stat, String type, int no) {
        //System.out.println("CensusDataDaoImpl::getCensusDataBySuburb:Info Entrance");
        if (stat.equals("g3") && type.equals("chart") && no == 1) {
            return getChartData_g3_01(input_ssc);
        }
        else if(stat.equals("g2") && type.equals("chart") && no == 1) {
            return getChartData_g2_01(input_ssc);
        }
        else if(stat.equals("g1") && type.equals("chart") && no == 1) {
            return getChartData_g1_01(input_ssc);
        }
        else {
            return null;
        }
    }

    private List<List<String>> getChartData_g3_01(String input_ssc){

        String sql = "SELECT tab.g1, tab.g2, tab.g3, tab.g4, tab.g5, tab.g6, tab.g7, tab.g8, tab.g9, "
                + "tab.g10, tab.g11, tab.g12, tab.g13, tab.g14, tab.g15, tab.g16, tab.g17, tab.g18, tab.g19, "
                + "tab.g20, tab.g21, tab.g22, tab.g23, tab.g24, tab.g25, tab.g26, tab.g27, tab.g28, tab.g29, "
                + "tab.g30, tab.g31, tab.g32, tab.g33, tab.g34, tab.g35, tab.g36 "
                + "FROM census_2016_data.ssc_g01 AS tab "
                + "WHERE tab.region_id = '"+input_ssc+"'"
                + "LIMIT 1";

        List<Map<String, Object>> rows2 = getJdbcTemplate().queryForList(sql);

        if( rows2.size() <= 0){
            return null;
        }

        List<List<String>> result = new ArrayList<>();

        Map<String, Object> row2 = rows2.get(0);
        result.add(Arrays.asList("Ages Group", "Number of people"));
        result.add(Arrays.asList("0-4",row2.get("g6").toString()));
        result.add(Arrays.asList("5-14",row2.get("g9").toString()));
        result.add(Arrays.asList("15-19",row2.get("g12").toString()));
        result.add(Arrays.asList("20-24",row2.get("g15").toString()));
        result.add(Arrays.asList("25-34",row2.get("g18").toString()));
        result.add(Arrays.asList("35-44",row2.get("g21").toString()));
        result.add(Arrays.asList("45-54",row2.get("g24").toString()));
        result.add(Arrays.asList("55-64",row2.get("g27").toString()));
        result.add(Arrays.asList("65-74",row2.get("g30").toString()));
        result.add(Arrays.asList("75-84",row2.get("g33").toString()));
        result.add(Arrays.asList(">85",row2.get("g36").toString()));

        return result;
    }

    private List<List<String>> getChartData_g2_01(String input_ssc){

        String sql = "SELECT tab.g1, tab.g2, tab.g3, tab.g4, tab.g5, tab.g6, tab.g7, tab.g8, tab.g9, "
                + "tab.g10, tab.g11, tab.g12, tab.g13, tab.g14, tab.g15, tab.g16, tab.g17, tab.g18, tab.g19, "
                + "tab.g20, tab.g21, tab.g22, tab.g23, tab.g24, tab.g25, tab.g26, tab.g27, tab.g28, tab.g29, "
                + "tab.g30, tab.g31, tab.g32, tab.g33, tab.g34, tab.g35, tab.g36 "
                + "FROM census_2016_data.ssc_g01 AS tab "
                + "WHERE tab.region_id = '"+input_ssc+"'"
                + "LIMIT 1";

        List<Map<String, Object>> rows2 = getJdbcTemplate().queryForList(sql);

        if( rows2.size() <= 0){
            return null;
        }

        List<List<String>> result = new ArrayList<>();

        Map<String, Object> row2 = rows2.get(0);
        result.add(Arrays.asList("Ages Group", "Number of people"));
        result.add(Arrays.asList("0-4",row2.get("g5").toString()));
        result.add(Arrays.asList("5-14",row2.get("g8").toString()));
        result.add(Arrays.asList("15-19",row2.get("g11").toString()));
        result.add(Arrays.asList("20-24",row2.get("g14").toString()));
        result.add(Arrays.asList("25-34",row2.get("g17").toString()));
        result.add(Arrays.asList("35-44",row2.get("g20").toString()));
        result.add(Arrays.asList("45-54",row2.get("g23").toString()));
        result.add(Arrays.asList("55-64",row2.get("g26").toString()));
        result.add(Arrays.asList("65-74",row2.get("g29").toString()));
        result.add(Arrays.asList("75-84",row2.get("g32").toString()));
        result.add(Arrays.asList(">85",row2.get("g35").toString()));

        return result;
    }

    private List<List<String>> getChartData_g1_01(String input_ssc){

        String sql = "SELECT tab.g1, tab.g2, tab.g3, tab.g4, tab.g5, tab.g6, tab.g7, tab.g8, tab.g9, "
                + "tab.g10, tab.g11, tab.g12, tab.g13, tab.g14, tab.g15, tab.g16, tab.g17, tab.g18, tab.g19, "
                + "tab.g20, tab.g21, tab.g22, tab.g23, tab.g24, tab.g25, tab.g26, tab.g27, tab.g28, tab.g29, "
                + "tab.g30, tab.g31, tab.g32, tab.g33, tab.g34, tab.g35, tab.g36 "
                + "FROM census_2016_data.ssc_g01 AS tab "
                + "WHERE tab.region_id = '"+input_ssc+"'"
                + "LIMIT 1";

        List<Map<String, Object>> rows2 = getJdbcTemplate().queryForList(sql);

        if( rows2.size() <= 0){
            return null;
        }

        List<List<String>> result = new ArrayList<>();

        Map<String, Object> row2 = rows2.get(0);
        result.add(Arrays.asList("Ages Group", "Number of people"));
        result.add(Arrays.asList("0-4",row2.get("g4").toString()));
        result.add(Arrays.asList("5-14",row2.get("g7").toString()));
        result.add(Arrays.asList("15-19",row2.get("g10").toString()));
        result.add(Arrays.asList("20-24",row2.get("g13").toString()));
        result.add(Arrays.asList("25-34",row2.get("g16").toString()));
        result.add(Arrays.asList("35-44",row2.get("g19").toString()));
        result.add(Arrays.asList("45-54",row2.get("g22").toString()));
        result.add(Arrays.asList("55-64",row2.get("g25").toString()));
        result.add(Arrays.asList("65-74",row2.get("g28").toString()));
        result.add(Arrays.asList("75-84",row2.get("g31").toString()));
        result.add(Arrays.asList(">85",row2.get("g34").toString()));

        return result;
    }





    /**
     * get the defination of each census item
     * @param searchStats
     * @param numClasses
     * @return
     */
    @Override
    public Map<String, Object> getMetadataByStats(List<String> searchStats, int numClasses){
        String statsStr = "";
        boolean isFirstItem = true;
        for(String stat: searchStats){
            if(isFirstItem){
                statsStr += "'" + stat + "'";
                isFirstItem = false;
            }
            else {
                statsStr += ",'" + stat + "'";
            }
        }

        String sql = "SELECT sequential_id AS id, "
                + "lower(table_number) AS \"table\", "
                + "replace(long_id, '_', ' ') AS description, "
                + "column_heading_description AS type, "
                + "'values' as maptype "
                + "FROM census_2016_data.metadata_stats "
                + "WHERE sequential_id IN ("+statsStr+") "
                + "ORDER BY sequential_id";

        //System.out.println("CensusDataDaoImpl::getMetadataByStats:Info sql"  +sql);
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        Map<String,Object> response_dict = new HashMap<String, Object>();



        response_dict.put("type","StatsCollection");
        response_dict.put("classes",numClasses);

        List<Object> feature_array = new ArrayList<Object>();


        for(Map<String,Object> row: rows) {
            Map<String,Object> feature_dict = new HashMap<String,Object>(row);
            feature_dict.put("id",feature_dict.get("id").toString().toLowerCase());
            feature_dict.put("table",feature_dict.get("table").toString().toLowerCase());
            feature_array.add(feature_dict);
        }

        response_dict.put("stats",feature_array);

        return response_dict;

    }

    /**
     * get ssc boundary for the map
     * @param stat_id
     * @param zoom_level
     * @param boundary_name
     * @param table_id
     * @param map_left
     * @param map_bottom
     * @param map_right
     * @param map_top
     * @return
     */
    @Override
    public Map<String,Object> getMapBoundary(String stat_id, int zoom_level, String boundary_name, String table_id,
                   String map_left, String map_bottom, String map_right, String map_top){

        String display_zoom = String.format("%02d", zoom_level);

        String sql = "SELECT bdy.id, bdy.name, bdy.population, tab."+stat_id+ "/ bdy.area AS density, "
                + "CASE WHEN bdy.population > 0 THEN tab."+stat_id+ " / bdy.population * 100.0 ELSE 0 END AS percent, "
                + "tab."+stat_id+ ", geojson_"+display_zoom+" AS geometry "
                + "FROM census_2016_web."+boundary_name+" AS bdy "
                + "INNER JOIN census_2016_data."+boundary_name+"_"+table_id+" AS tab ON bdy.id = tab.region_id "
                + "INNER JOIN census_2016_bdys."+boundary_name+"_2016_aust AS def on bdy.id = def.ssc_code16 "
                + "WHERE bdy.geom && ST_MakeEnvelope("+map_left+", "+map_bottom+", "+map_right+", "+map_top+", 4283) "
                + "AND def.ste_code16 = '1' ";
                //ste_code16 = '1', so only NSW map is shown

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        Map<String, Object> output_dict = new HashMap<String, Object>();
        output_dict.put("type","FeatureCollection");

        int i = 0;
        List<Object>feature_array = new ArrayList<Object>();

        for(Map<String, Object> row : rows) {
            Map<String, Object> feature_dict = new HashMap<String, Object>();
            feature_dict.put("type","Feature");

            Map<String, Object> properties_dict = new HashMap<String, Object>();

            for (String col : row.keySet()) {

                if(col.equals("geometry")) {
                    //JSONObject geoObj = new JSONObject(row.get(col).toString());
                    //PGgeometry geoObj = (PGgeometry)(row.get(col));
                    //feature_dict.put("geometry",geoObj);
                    Gson gson = new Gson();
                    Type geoObjMapType = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String,Object> geoObj = gson.fromJson(row.get(col).toString(),geoObjMapType);
                    //Map<String,Object> geoObj = gson.fromJson(row.get(col).toString(),Map.class);
                    feature_dict.put("geometry",geoObj);
                    //feature_dict.put("geometry",row.get(col));
                }
                else if (col.equals("id")){
                    feature_dict.put("id",row.get(col));
                    properties_dict.put("id",row.get(col));
                }
                else{
                    properties_dict.put(col,row.get(col));
                }
            }
            feature_dict.put("properties",properties_dict);

            feature_array.add(feature_dict);

            i += 1;
        }

        output_dict.put("features", feature_array);

        //System.out.println("CensusDataDaoImpl::get_data:info output_dict: ");
        //System.out.println(output_dict);

        return output_dict;

    }

}
