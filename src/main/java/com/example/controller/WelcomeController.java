package com.example.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import com.example.model.Suburb;
import com.example.service.SuburbService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.service.AddressesService;
import com.example.model.Address;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {

    @Autowired
    AddressesService nsw_addressesService;

    @Autowired
    SuburbService suburbService;

    // inject via application.properties
    //@Value("${welcome.message:test}")
    //private String message = "Hello World";

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String search(Map<String, Object> model) {
        model.put("message","Enter your address");
        return "mainform";
    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    public String result(Map<String, Object> model, @RequestParam("InputAddress") String inputAddress) {
        //List<Address> listAdd = nsw_addressesService.findNsw_addressByKeyWords("Summer Hill");

        //model.put("listAdd",listAdd);

        Address addr = nsw_addressesService.findAddressObjectByAddress(inputAddress);

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

        List<Object> result_data = new ArrayList<Object>();

        model.put("input_suburb",inputSuburb.getName());
        model.put("mb_2016_code",inputMb_2016_code);
        model.put("input_ssc",inputSuburb.getSsc_code());
        model.put("suburb_center_lng",inputSuburb.getCentreLng());
        model.put("suburb_center_lat",inputSuburb.getCentreLat());
        model.put("result_data",result_data);
        model.put("mapstats","g3");

        return "mainform";
    }


/*
            for row in rows:
    input_ssc = row['ssc_code']
    suburb_center_lat = row['lat']
    suburb_center_lng = row['lng']

    print("view.py::index: input_ssc = ",input_ssc,"lat=",suburb_center_lat,"lng=",suburb_center_lng)

        # get datailed population data.
    result_data = get_population_data(input_ssc, input_suburb);

        return render_template('result.html',
                               input_suburb=input_suburb,
                               mb_2016_code=mb_2016_code,
                               input_ssc=input_ssc,
                               suburb_center_lng=suburb_center_lng,
                               suburb_center_lat=suburb_center_lat,
                               result_data=result_data,
                               mapstats="g3") #g3: population

    #elif resultform.validate_on_submit() and resultform.Submit2.data:
            #return redirect(url_for('main.index'))
            # render the serach page
    else:
            return render_template('mainform.html',
                                   form=form)

*/

    @RequestMapping(value="/autocomplete" , method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> autocomplete(@RequestParam String q){
        System.out.println("WelcomController::autocomplete:Info Entrance");
        List<String> listAdd = nsw_addressesService.findAddressStringByKeyWords(q);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("matching_results",listAdd);
        return data;
    }

    @RequestMapping("/about")
    public String about(Map<String, Object> model) {
        ArrayList contributors = new ArrayList();
        HashMap mMap = new HashMap();
        mMap.put("name", "Bin Liu");
        mMap.put("link","https://github.com/Interest1024");
        contributors.add(mMap);

        model.put("contributors", contributors);
        return "about";
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