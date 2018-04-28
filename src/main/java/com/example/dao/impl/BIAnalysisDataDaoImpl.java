package com.example.dao.impl;

import com.example.dao.CensusDataDao;
import com.example.dao.BIAnalysisDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by E470 on 4/26/2018.
 */
@Repository
public class BIAnalysisDataDaoImpl implements BIAnalysisDataDao {


    @Value("${web.data-path}")
    private String dataPath;

    /*
    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }
    */

    protected final JdbcTemplate jdbc;

    @Autowired
    public BIAnalysisDataDaoImpl(JdbcTemplate jdbc) {
        this.jdbc=jdbc;
    }

    @Autowired
    private ResourceLoader resourceLoader;

    public List<String> getCsvFiles() {
        List<String> result = new ArrayList<>();
        result.add("AshfieldPopulation");
        //hardcode
        return result;
    }

    /*This code works in loacal machine, but does not work in heroku.
    public List<String> getCsvFiles() {
        try {
            Resource csvPathResource = resourceLoader.getResource("classpath:" + dataPath);
            File folder = csvPathResource.getFile();

            //File folder = new File(csvPath);
            System.out.println("BIAnalysisDataDaoImpl::BIAnalysisDataDaoImpl:Info folder:");
            System.out.println(folder);
            File[] listOfFiles = folder.listFiles();
            //System.out.println(listOfFiles[0]);
            if (listOfFiles == null) {
                return null;
            }

            List<String> result = new ArrayList<>();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String fileName = listOfFiles[i].getName();
                    //remove ".csv"
                    result.add(fileName.substring(0, fileName.length() - 4));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    */

    /* this is work in read csv book.
        however, this does not work in heroku
    public String readCsvFile(String fileName){
        Resource csvFileResource = resourceLoader.getResource("classpath:"+dataPath+ fileName + ".csv");
        //String filePathName = dataPath + fileName + ".csv";

        String result = "";
        try {
            result = new String(Files.readAllBytes(Paths.get(csvFileResource.getURI())), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    */

    /**
     * read data from db, and send to front end like a csv file.
     * @param fileName
     * @return
     */
    public String readCsvFile(String fileName) {
        String result = "";

        String sql = "SELECT tab.g1, tab.g2, tab.g3, tab.g4, tab.g5, tab.g6, tab.g7, tab.g8, tab.g9, "
                + "tab.g10, tab.g11, tab.g12, tab.g13, tab.g14, tab.g15, tab.g16, tab.g17, tab.g18, tab.g19, "
                + "tab.g20, tab.g21, tab.g22, tab.g23, tab.g24, tab.g25, tab.g26, tab.g27, tab.g28, tab.g29, "
                + "tab.g30, tab.g31, tab.g32, tab.g33, tab.g34, tab.g35, tab.g36 "
                + "FROM census_2016_data.ssc_g01 AS tab "
                + "WHERE tab.region_id = '" + "SSC10098" + "'"
                + "LIMIT 1";

        //jdbc.queryForObject(sql);
        List<Map<String, Object>> rows = jdbc.queryForList(sql);

        if (rows.size() <= 0) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        result+="Ages Group,Number of Males, Number of Females\r\n";
        result+="0-4,"+row.get("g4").toString()+","+row.get("g5").toString()+"\r\n";
        result+="5-14,"+row.get("g4").toString()+","+row.get("g8").toString()+"\r\n";
        result+="15-19,"+row.get("g10").toString()+","+row.get("g11").toString()+"\r\n";
        result+="20-24,"+row.get("g13").toString()+","+row.get("g14").toString()+"\r\n";
        result+="25-34,"+row.get("g16").toString()+","+row.get("g17").toString()+"\r\n";
        result+="35-44,"+row.get("g19").toString()+","+row.get("g20").toString()+"\r\n";
        result+="45-54,"+row.get("g22").toString()+","+row.get("g23").toString()+"\r\n";
        result+="55-64,"+row.get("g25").toString()+","+row.get("g26").toString()+"\r\n";
        result+="65-74,"+row.get("g28").toString()+","+row.get("g29").toString()+"\r\n";
        result+="75-84,"+row.get("g31").toString()+","+row.get("g32").toString()+"\r\n";
        result+=">85,"+row.get("g34").toString()+","+row.get("g35").toString()+"\r\n";

        /*
        for (Map<String, Object> resultObject : result) {
            Map<String, Object> item = resultObject;

        }
        */

        return result;
    }
}
