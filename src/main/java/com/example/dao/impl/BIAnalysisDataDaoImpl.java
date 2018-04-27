package com.example.dao.impl;

import com.example.dao.BIAnalysisDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by E470 on 4/26/2018.
 */
@Repository
public class BIAnalysisDataDaoImpl implements BIAnalysisDataDao{

    @Value("${web.data-path}")
    private String dataPath;

    @Autowired
    private ResourceLoader resourceLoader;

    public List<String> getCsvFiles() {
        try {
            Resource csvPathResource = resourceLoader.getResource("classpath:"+dataPath);
            File folder = csvPathResource.getFile();

            //File folder = new File(csvPath);
            System.out.println("BIAnalysisDataDaoImpl::BIAnalysisDataDaoImpl:Info folder:");
            System.out.println(folder);
            File[] listOfFiles = folder.listFiles();
            //System.out.println(listOfFiles[0]);
            if(listOfFiles == null){
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
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public String readCsvFile(String fileName){
        Resource csvFileResource = resourceLoader.getResource("classpath:"+dataPath+ fileName + ".csv");

        //String filePathName = dataPath + fileName + ".csv";


        String result = "";
        try {
            //BufferedReader reader = new BufferedReader(new FileReader(filePathName));
            //reader.readLine(); //first line
            //String line = "";

            result = new String(Files.readAllBytes(Paths.get(csvFileResource.getURI())), StandardCharsets.UTF_8);

            //while((line=reader.readLine())!=null){
                //result += line;
                /*
                String item[] = line.split(",");

                String last = item[item.length-1];
                //int value = Integer.parseInt(last);
                System.out.println(last);
                */
            //}
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
