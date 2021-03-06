package com.example.controller;

import java.util.*;

import com.example.model.Suburb;
import com.example.service.BIAnalysisService;
import com.example.service.CensusDataService;
import com.example.service.SuburbService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.service.AddressesService;
import com.example.model.Address;

//import org.json.JSONObject;

@Controller
public class MainController {

    @Autowired
    AddressesService addressesService;

    @Autowired
    SuburbService suburbService;

    @Autowired
    CensusDataService censusDataService;

    @Autowired
    BIAnalysisService bIAnalysisService;

    // inject via application.properties
    //@Value("${welcome.message:test}")
    //private String message = "Hello World";

    /**
     * The search page
     * @param model
     * @return
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String search(Map<String, Object> model) {
        model.put("message","Enter your address");
        return "mainform";
    }

    /**
     * response to the summit of a address from the search page.
     * @param model
     * @param inputAddress
     * @return result.html with needed data
     */
    @RequestMapping(value="/", method=RequestMethod.POST)
    public String censusDataResult(Map<String, Object> model, @RequestParam("InputAddress") String inputAddress) {
        //List<Address> listAdd = nsw_addressesService.findNsw_addressByKeyWords("Summer Hill");

        //model.put("listAdd",listAdd);

        Address addr = addressesService.findAddressObjectByAddress(inputAddress);

        //if the system cannot find the address
        if (addr == null){
            model.put("message","Address not found. Please try again.");
            return "mainform";
        }

        String inputSuburbName = addr.getLocality_name();
        String inputMb_2016_code = addr.getMb_2016_code();

        Suburb inputSuburb = suburbService.findSuburbByMb_2016_code(inputMb_2016_code);

        //if the system cannot find the suburb
        if (inputSuburb == null){
            model.put("message","Address not found. Please try again.");
            return "mainform";
        }

        System.out.println("WelcomeController::result:Info "+inputAddress+","+inputSuburb.getName()+","
                +inputSuburb.getSsc_code()+","+inputSuburb.getCentreLng()+","+inputSuburb.getCentreLat());


        //Suburb inputSuburb = suburbService.findSuburbByMb_2016_code(inputMb_2016_code);

        List<Map<String, Object>> result_data = censusDataService.getCensusDataBySuburb(inputSuburb);

        //System.out.println("MainController::censusDataResult:Info "+result_data.size());
        System.out.println("MainController::censusDataResult:Info input_ssc = "+inputSuburb.getSsc_code());

        model.put("input_suburb",inputSuburb.getName());
        model.put("mb_2016_code",inputMb_2016_code);
        model.put("input_ssc",inputSuburb.getSsc_code());
        model.put("suburb_center_lng",inputSuburb.getCentreLng());
        model.put("suburb_center_lat",inputSuburb.getCentreLat());
        model.put("result_data",result_data);
        model.put("mapstats","g3");

        return "result";
    }


    /**
     * Service for the autocomplete function.
     * return complete address based on a user's key words
     */
    @RequestMapping(value="/autocomplete" , method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> autocomplete(@RequestParam String q){
        //System.out.println("WelcomController::autocomplete:Info Entrance");
        List<String> listAdd = addressesService.findAddressStringByKeyWords(q);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("matching_results",listAdd);
        return data;
    }

    /**
     * Return data for the about page
     * @param model
     * @return
     */
    @RequestMapping(value="/about" , method= RequestMethod.GET)
    public String about(Map<String, Object> model) {
        ArrayList<HashMap<String, String>> contributors = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mMap = new HashMap<>();
        mMap.put("name", "Bin Liu");
        mMap.put("link","https://github.com/Interest1024");
        contributors.add(mMap);

        model.put("contributors", contributors);
        return "about";
    }

    /**
     * Get current boundary level. Now alway return ssc.
     * used by map
     */
    @RequestMapping("/get-bdy-names")
    @ResponseBody
    public Map<String,Object> get_boundary_name(@RequestParam String min, @RequestParam String max) {
        int min_val = Integer.parseInt(min);
        int max_val = Integer.parseInt(max);
        Map<String,Object> boundary_zoom_dict = new HashMap<String,Object>();
        for (int zoom_level = min_val; zoom_level<=max_val;zoom_level++){
            Map<String,String> boundary_dict = new HashMap<String, String>();
            boundary_dict.put("name","ssc");
            boundary_dict.put("min","5");
            //hardcode: name, min
            boundary_zoom_dict.put( Integer.toString(zoom_level), boundary_dict);
        }
        return boundary_zoom_dict;
    }

    /**
     * Get the definition of census data items.
     * Used by map
     */
    @RequestMapping("/get-metadata")
    @ResponseBody
    public Map<String,Object> get_metadata(@RequestParam String stats, @RequestParam String n) {
        String raw_stats = stats.toUpperCase();
        List<String> search_stats = Arrays.asList(raw_stats.split(","));

        int num_classes=Integer.parseInt(n);

        return censusDataService.getMetadataByStats(search_stats, num_classes);
    }

    /**
     * Get the census data item of every suburb in the scope of the map.
     */
    @RequestMapping("/get-data")
    @ResponseBody
    public Map<String,Object> get_data(@RequestParam String ml, @RequestParam String mb,  @RequestParam String mr,
                                       @RequestParam String mt, @RequestParam String s, @RequestParam String t,
                                       @RequestParam String b, @RequestParam String z, @RequestParam String input_ssc) {

        String map_left = ml;
        String map_bottom = mb;
        String map_right = mr;
        String map_top = mt;
        String stat_id = s;
        String table_id = t;
        String boundary_name = b;
        int zoom_level = Integer.parseInt(z);

        if (boundary_name == null || boundary_name.equals("")) {
            boundary_name = "ssc";
            //min_val = 5;
        }
        //Hardcode

        Map<String, Object> mapInfo = censusDataService.getMapBoundary(stat_id, zoom_level, boundary_name, table_id,
                map_left, map_bottom, map_right, map_top);

        return mapInfo;
    }

    /**
     * Get the data for tables and charts in panels.
     */
    @RequestMapping("/gettablechart")
    @ResponseBody
    public List<List<String>> getCensusDataBySuburbStats(@RequestParam String input_ssc, @RequestParam String name,
                                                         @RequestParam String stat,
                                                         @RequestParam String type, @RequestParam int no){
        List<List<String>> result = censusDataService.getCensusDataBySuburbStats(input_ssc, name, stat, type, no);
        //System.out.println("MainController::getCensusDataBySuburbStats:Info result = "+result+","+input_ssc+","+stat);
        return result;
    }


    /**
     * return the bianalysis page, when click BI Analysis menu item.
     * @param model
     * @return
     */
    @RequestMapping(path = "/bianalysis")
    public String bianalysis(Map<String, Object> model) {
        model.put("message","bianalysis");
        return "bianalysis";
    }

    /**
     * get all .csv file in the data directory
     * @return
     */
    @RequestMapping(path = "/csvdata", method = RequestMethod.GET)
    @ResponseBody
    public  String getBIAnalysisCsvData() {
        List<String> resultList = bIAnalysisService.getCsvFiles();
        Gson gson = new Gson();
        String result = gson.toJson(resultList);
        return result;
    }

    /**
     * get the data in a csv files for the page BIAnalysis
     * @param fileName
     * @return
     */
    @RequestMapping(path = "/data/{fileName}")
    @ResponseBody
    public  Map<String, String> getBIAnalysisCsvData(@PathVariable("fileName") String fileName) {
        String fileStr = bIAnalysisService.readCsvfile(fileName);
        //System.out.println("MainController::getBIAnalysisCsvData: Info");
        //System.out.println(fileName);
        //System.out.println(fileStr);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("name",fileName);
        resultMap.put("csv",fileStr);
        //Gson gson = new Gson();
        //String result = gson.toJson(resultMap);
        //return result;
        return resultMap;
    }


    /*
    @RequestMapping(path = "/error")
    public Map<String, Object> handle(HttpServletRequest request) {
        /*Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", request.getAttribute("javax.servlet.error.status_code"));
        map.put("reason", request.getAttribute("javax.servlet.error.message"));
        return map;

    }
    */



}