package com.example.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.service.Nsw_addressesService;
import com.example.model.Nsw_addresses;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {

    @Autowired
    Nsw_addressesService nsw_addressesService;

    // inject via application.properties
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        //model.put("message", this.message);
        List<Nsw_addresses> listAdd = nsw_addressesService.findNsw_addressByKeyWords("Summer Hill");

        model.put("listAdd",listAdd);

        return "mainform";
    }

    @RequestMapping(value="/autocomplete" , method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> autocomplete(@RequestParam String q){
        System.out.println("WelcomController::autocomplete:Info Entrance");
        List<String> listAdd = nsw_addressesService.findAddressByKeyWords(q);
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