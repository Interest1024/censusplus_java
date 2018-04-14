/**
 * Created by E470 on 4/14/2018.
 */
"use strict";

var bdyNamesUrl = "/get-bdy-names";
var metadataUrl = "/get-metadata";
var dataUrl = "/get-data";

var map;
var info;
// var legend;
var themer;
var geojsonLayer;
var currLayer;


var numClasses = 7; // number of classes (i.e colours) in map theme
var minZoom = 4;
var maxZoom = 16;
var currentZoomLevel = 0;
var censusYear = "";
var curMapCenter = new L.LatLng(-33.85, 151.15);
//{lat: 151.12754999999999, lng: -33.88945}
var hasInitMapPanTo = 0;

// current searched ssc code
var input_ssc;
var statsArray = [];
var currentStat;
//for example: {'id': 'g2', 'table': 'g01', 'description': 'Total Persons Females', 'type': 'Females', 'maptype': 'values'}]
var currentStatId = "";
//for example: g2. currentStatId is lower case. parameter mapstats of init is capital.
//because in the return data from /get-data, the key is lowcase. for example: in props[currentStatId].
var currMapMin = 0;
var currMapMax = 0;
var boundaryZooms;
var currentStats;
var boundaryOverride = "";

var currentBoundary = "";
var currentBoundaryMin = 7;

var lowPopColour = "#422";
var colourRamp;
//var colourRange = ["#1f1f1f", "#e45427"]; // dark grey > orange/red
//var colourRange = ["#1a1a1a", "#DD4132"]; // dark grey > red
//var colourRange = ["#1a1a1a", "#92B558"]; // dark grey > green
var colourRange = ["#ffffff", "#00ff00"]; // dark grey > green

//the suburb name from seach box
var inputSuburb="";

// get querystring values
// code from http://forum.jquery.com/topic/getting-value-from-a-querystring
// get querystring as an array split on "&"
var querystring = location.search.replace("?", "").split("&");

// declare object
var queryObj = {};

// loop through each name-value pair and populate object
var i;
for (i = 0; i < querystring.length; i+=1) {
    // get name and value
    queryObj[querystring[i].split("=")[0]] = querystring[i].split("=")[1];
}

// get/set values from querystring
if (!queryObj.census) {
    censusYear = "2016";
} else {
    censusYear = queryObj.census.toString();
    // TODO: CHECK CENSUS YEAR VALUE IS VALID (2011 OR 2016 ONLY)
}

// get/set values from querystring

// auto-boundary override (for screenshots only! will create performance issues. e.g showing SA1"s nationally!)
if (queryObj.b !== undefined) {
    boundaryOverride = queryObj.b.toLowerCase();
}

// start zoom level
if (!queryObj.z) {
    currentZoomLevel = 13;
} else {
    currentZoomLevel = queryObj.z;
}

//// number of classes to theme the map - TODO: ADD SUPPORT FOR CUSTOM NUMBER OF MAP CLASSES
//if (!queryObj["n"]) {
//    numClasses = 7;
//} else {
//    numClasses = queryObj["n"];
//}

/*
 // get the stat(s) - can include basic equations using + - * / and ()  e.g. B23 * (B45 + B678)
 if (!queryObj.stats) {
 if (censusYear === "2016") {
 statsArray = ["g3", "g1", "g2"]; // total_persons
 } else {  // 2011
 statsArray = ["b3", "b1", "b2"]; // total_persons
 }
 } else {
 statsArray = encodeURIComponent(queryObj.stats.toLowerCase()).split("%2C");
 // TODO: handle maths operators as well as plain stats
 }
 */

function init(searchSuburb,mb_2016_code,p_input_ssc,suburb_center_lng,suburb_center_lat,mapstats) {
    console.log("loadmap.js::init: enter, paras="+searchSuburb+","+mb_2016_code+","+input_ssc+","
        +suburb_center_lng+","+suburb_center_lat+","+mapstats);

    input_ssc = p_input_ssc;
    // initial stat is the first one in the querystring
    //currentStatId = statsArray[0];
    currentStatId = mapstats.toLowerCase();
    inputSuburb = searchSuburb;
    curMapCenter = new L.LatLng(suburb_center_lat, suburb_center_lng);

    // create colour ramp
    colourRamp = new Rainbow();
    colourRamp.setSpectrum(colourRange[0], colourRange[1]);

    //Initialize the map on the "map" div - only use canvas if supported (can be slow on Safari)
    var elem = document.createElement("canvas");

    //if (!$.isEmptyObject(map)){

    if(typeof map !== 'undefined'){
        map.remove();
    }


    if (elem.getContext && elem.getContext("2d")) {
        map = new L.Map("datamap", { preferCanvas: true });
    }
    else {
        map = new L.Map("datamap", { preferCanvas: false });
    }

    //disable mouse wheel
    map.scrollWheelZoom.disable();

    //map = new L.Map("datamap", { preferCanvas: false }); // canvas slows Safari down versus Chrome (IE & edge are untested)


    // acknowledge the data provider
    map.attributionControl.addAttribution("Census data &copy; <a href='http://www.abs.gov.au/websitedbs/d3310114.nsf/Home/Attributing+ABS+Material'>ABS</a>");

    // create non-interactive pane (i.e. no mouse events) for basemap tiles
    map.createPane("basemap");
    map.getPane("basemap").style.zIndex = 650;
    map.getPane("basemap").style.pointerEvents = "none";

    // load CartoDB basemap
    //L.tileLayer("http://cartodb-basemaps-{s}.global.ssl.fastly.net/dark_all/{z}/{x}/{y}.png", {
    //L.tileLayer("https://a.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    L.tileLayer("https://a.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution : "&copy; <a href='http://www.openstreetmap.org/copyright'>OpenStreetMap</a> &copy; <a href='http://cartodb.com/attributions'>CartoDB</a>",
        subdomains : "abcd",
        minZoom : minZoom,
        maxZoom : maxZoom,
        pane: "tilePane", //"basemap",
        opacity: 1
    }).addTo(map);

    // set the view to a given center and zoom
    //map.setView(new L.LatLng(-33.85, 151.15), currentZoomLevel);
    map.setView(curMapCenter, currentZoomLevel);

    /*
     // get bookmarks
     var bmStorage = {
     getAllItems : function (callback) {
     $.getJSON("/main/static/bookmarks.json",
     function (json) {
     callback(json);
     });
     }
     };
     // add bookmark control to map
     var bm = new L.Control.Bookmarks({
     position : "topleft",
     localStorage : false,
     storage : bmStorage
     }).addTo(map);
     */


    // add control that shows info on mouseover
    info = L.control();
    info.onAdd = function () {
        this._div = L.DomUtil.create("div", "info");
        L.DomEvent.disableScrollPropagation(this._div);
        L.DomEvent.disableClickPropagation(this._div);
        this.update();
        return this._div;
    };
    info.update = function (props, colour) {
        var infoStr;
        console.log("loadmap.js::info.update: props=");
        console.log(props);
        if (props) {
            // improve the formatting of multi-name bdys
            //var re = new RegExp(" - ", "g");
            //var name = props.name.replace(re, "<br/>");
            var propsname = props.name.replace(/ *\([^)]*\) */g, "");

            //print hightlight suburb's name
            infoStr = "<span style='font-weight: bold; font-size:1em; background:#fff;'>" + propsname + "</span><br/>";

            // if no pop, nothing to display
            //if (props.population === 0) {
            //    infoStr += "<span class='highlight' style='background:#fff;'>no population</span>";
            //}
            //else {
            // // special case if value is total pop - convert to pop density
            // var stat = 0;
            // var type = "";
            // if (currentStat.description === "Total Persons Persons") {
            //     stat = props.density;
            //     type = "Persons / km<sup>2</sup>"
            // } else {
            //     stat = props[currentStatId]
            //     type = currentStat.type;
            // }

            var type = currentStat.type;
            var valStr = stringNumber(props[currentStatId], "values", type);
            var popStr = stringNumber(props.population, "values", "dummy") + " persons";
            //console.log("maptype"+currentStat.maptype);

            if (currentStat.maptype === "values") {
                var colour = getColor(props[currentStatId], 99999999); //dummy second value get's the right colour
                infoStr += "<span class='highlight' style='background:#fff;'>" + type + ": " + valStr + "</span>"; //<br/>" + popStr;
            }
            else { // "percent"
                var colour = getColor(props.percent, 99999999); //dummy second value get's the right colour
                var percentStr = stringNumber(props.percent, "percent", type);
                infoStr += "<span class='highlight' style='background:#fff;'>" + currentStat.description + ": " + percentStr + "</span>"; //<br/>" + valStr + " of " + popStr;
            }

            // highlight low population bdys
            //if (props.population <= currentBoundaryMin) {
            //    infoStr += "<br/><span class='highlight' style='background:" + lowPopColour + "'>low population</span>";
            //}
            //}
        }
        else {
            infoStr ="<span style='font-weight: bold; font-size:1em; background:#fff;'>"+"Pick a boundary"
                + "</span>";
        }

        this._div.innerHTML = infoStr;
    };


    /*
     // add radio buttons to choose stat to theme the map
     themer = L.control({
     position : "bottomright"
     });

     themer.onAdd = function () {
     this._div = L.DomUtil.create("div", "info themer");
     L.DomEvent.disableScrollPropagation(this._div);
     L.DomEvent.disableClickPropagation(this._div);
     this.update();
     return this._div;
     };

     themer.update = function (radioButtons) {
     this._div.innerHTML = radioButtons;

     // event to trigger the map theme change
     $("input:radio[name=stat]").click(function () {
     currentStatId = $(this).val();

     // update stat metadata and map data
     getCurrentStatMetadata();
     getData(input_ssc);
     });
     };
     themer.addTo(map);
     themer.update("<b>L O A D I N G . . .</b>");
     */



    // get list of boundaries and the zoom levels they display at
    // and get stats metadata, including map theme classes
    $.when(
        $.getJSON(bdyNamesUrl + "?min=" + minZoom.toString() + "&max=" + maxZoom.toString()),
        $.getJSON(metadataUrl + "?c="  + censusYear + "&n=" + numClasses.toString() + "&stats=" + mapstats)
    ).done(function(bdysResponse, metadataResponse) {
        if (!boundaryOverride){
            boundaryZooms = bdysResponse[0];
        } else {
            // create array of zoom levels with the override boundary id
            boundaryZooms = {};
            var j;
            for (j = minZoom; j <= maxZoom; j+=1) {
                boundaryZooms[j] = {
                    name: boundaryOverride,
                    min: currentBoundaryMin
                };
            }
        }

        // get the initial stat"s metadata
        currentStat = metadataResponse[0].stats[0];
        console.log("loadmap.js::init: currentStats=");
        console.log(currentStat.id);
        console.log(currentStat);
        //getCurrentStatMetadata(currentStats);

        // show legend and info controls
        // legend.addTo(map);
        info.addTo(map);

        // get the first lot of data
        getData(input_ssc);
    });

    // get a new set of data when map panned or zoomed
    map.on("moveend", function () {
        //getCurrentStatMetadata(currentStats);
        getData(input_ssc);
    });

}

//put the input subsurb in the center of map when init the map
function initMapPanTo(curMapCenter){
    if(hasInitMapPanTo == 0){
        console.log("loadmap.js::initMapPanTo");
        console.log(curMapCenter);
        //map.panTo(curMapCenter);
        map.setView(curMapCenter, currentZoomLevel);
        hasInitMapPanTo=1;
    }
    else{
    }
}
/*
 function setRadioButtons() {
 // var radioButtons = "<h4>Active stat</h4>";
 var radioButtons = "";

 for (var i = 0; i < currentStats.length; i++){
 var value = currentStats[i].id;
 var description = currentStats[i].description;
 var type = currentStats[i].type;

 if (value === currentStatId) {
 //format values
 var mapType = currentStats[i].maptype;
 var minStr = stringNumber(currMapMin, mapType, type);
 var maxStr = stringNumber(currMapMax, mapType, type);

 radioButtons += "<div><input id='r" + i.toString() + "' type='radio' name='stat' value='" + value +
 "' checked='checked'><label for='r" + i.toString() + "'><span><span></span></span><b>" + description + "</b></label>" +
 "<div style='padding: 0.2em 0em 0.6em 1.8em'><table class='colours' ><tr><td>" + minStr + "</td><td style='width: 10em'></td><td>" + maxStr + "</td></tr></table></div></div>";
 } else {
 radioButtons += "<div><input id='r" + i.toString() + "' type='radio' name='stat' value='" + value +
 "'><label for='r" + i.toString() + "'><span><span></span></span>" + description + "</label></div>";
 }
 }

 themer.update(radioButtons);
 }
 */
function getCurrentStatMetadata(currentStats) {
    // loop through the stats to get the current one
    for (var i = 0; i < currentStats.length; i++) {
        if (currentStats[i].id === currentStatId) {
            currentStat = currentStats[i];
        }
    }
}

function stringNumber(val, mapType, type) {
    var numString = "";

    if (mapType === "values") {
        //add dollar sign
        if (type.indexOf("$/") !== -1) {
            numString = "$" + val.toLocaleString(["en-AU"]);
        } else {
            numString = val.toLocaleString(["en-AU"]);
        }
    } else { // i.e. "percent"
        //round percentages
        numString = val.toFixed(1).toLocaleString(["en-AU"]) + "%";
    }

    return numString;
}

/***********************************************************
 *called when init or moving map
 *call main/views.py::@main.route("/get-data") to get boundaries data
 ***********************************************************/
function getData(input_ssc) {
    console.log(currentStat);
    console.time("got boundaries");

    // get new zoom level and boundary
    currentZoomLevel = map.getZoom();
    currentBoundary = boundaryZooms[currentZoomLevel.toString()].name;
    currentBoundaryMin = boundaryZooms[currentZoomLevel.toString()].min;

    //restrict to the zoom levels that have data
    if (currentZoomLevel < minZoom) {
        currentZoomLevel = minZoom;
    }
    if (currentZoomLevel > maxZoom) {
        currentZoomLevel = maxZoom;
    }

    // get map extents
    var bb = map.getBounds();
    var sw = bb.getSouthWest();
    var ne = bb.getNorthEast();

    // build URL
    var ua = [];
    ua.push(dataUrl);
    ua.push("?ml=");
    ua.push(sw.lng.toString());
    ua.push("&mb=");
    ua.push(sw.lat.toString());
    ua.push("&mr=");
    ua.push(ne.lng.toString());
    ua.push("&mt=");
    ua.push(ne.lat.toString());
    ua.push("&s=");
    ua.push(currentStat.id);
    ua.push("&t=");
    ua.push(currentStat.table);
    ua.push("&b=");
    ua.push(currentBoundary);
    ua.push("&m=");
    ua.push(currentStat.maptype);
//    ua.push("&c=");
//    ua.push(censusYear);
    ua.push("&z=");
    ua.push((currentZoomLevel).toString())
    ua.push("&input_ssc=");
    ua.push(input_ssc)
    ;

    var requestString = ua.join("");

//    console.log(requestString);

    //Fire off AJAX request
    $.getJSON(requestString, gotData);
}

/***********************************************************
 *create geo shape from retuned json boundaries data using L.geoJson
 ************************************************************/
function gotData(json) {
    console.timeEnd("loadmap.js::gotData: got boundaries");
    console.time("loadmap.js::gotData: parsed GeoJSON");
    console.log("loadmap.js::gotData: json = "+json);

    if (json !== null) {
        if(geojsonLayer !== undefined) {
            geojsonLayer.clearLayers();
        }

        /*
         // get min and max values
         currMapMin = 999999999;
         currMapMax = -999999999;
         */
        var features = json.features;

        for (var i = 0; i < features.length; i++){
            var props = features[i].properties;

            // only include if pop is significant
            //if (props.population > currentBoundaryMin){
            var val = 0;

            if (currentStat.maptype === "values") {
                val = props[currentStatId];
            } else { // "percent"
                val = props.percent;
            }

            //if (val < currMapMin) { currMapMin = val }
            //if (val > currMapMax) { currMapMax = val }
            //}
        }

        // correct max percents over 100% (where pop is less than stat, for whatever reason)
        //if (currentStat.maptype === "percent" && currMapMax > 100.0) { currMapMax = 100.0 }

        // set the number range for the colour gradient (allow for decimals, convert to ints)
        //var minInt = parseInt(currMapMin.toFixed(1).toString().replace(".",""));
        //var maxInt = parseInt(currMapMax.toFixed(1).toString().replace(".",""));

        //colourRamp.setNumberRange(minInt, maxInt);


        //update the legend with the new min and max
        // legend.update();

        // reset info box
        info.update();

        // create the radio buttons
        //setRadioButtons();

        // console.log(currMapMin);
        // console.log(currMapMax);

        // add data to map layer
        geojsonLayer = L.geoJson(json, {
            style : style,
            onEachFeature : onEachFeature
        }).addTo(map);

        //calculate the center of the suburb

        //json.forEach(function(curElement){
        //console.log("loadmap.js::gotData: json:"+json);
        //});

        /*
         //center current suburb
         //console.log("loadmap.js::gotData: curCenter = ");
         var curPolygon = L.polygon(json.features[0].geometry.coordinates);
         var mapCenter = curPolygon.getBounds().getCenter();
         //console.log(mapCenter.lat);
         curMapCenter = new L.LatLng(mapCenter.lng,mapCenter.lat);
         //console.log(curMapCenter);
         //put the input subsurb in the center of map
         initMapPanTo(curMapCenter);
         */
    }
    else {
        alert("No data returned!")
    }

    console.timeEnd("loadmap.js::gotData: parsed GeoJSON");


}

//Set the style of suburbs.
function style(feature) {

    var renderVal;
    var props = feature.properties;

    if (currentStat.maptype === "values") {
        renderVal = props[currentStatId];
    }
    else {
        renderVal = props.percent;
    }

    //hardcode
    var max = 60000;
    var min = 0;
    if(currentStatId === 'g3'){
        max = 45000; //50474;
        //min = 1000;
    }
    else if(currentStatId === 'g2'){
        max = 22000; //25734;
        //min = 500;
    }
    else if(currentStatId === 'g1'){
        max = 22000; //25296;
        //min = 500;
    }
    //console.log("loadmap.js::style"+input_ssc+", "+max.toString());

    var colorDegree = Math.min(renderVal/max,1.0);
    var col = heatMapColorforValue(colorDegree);
    //var col = getColor(renderVal, props.population);
    //var col = "#FFF056";

    var returnStyle = {
        weight : 0,
        opacity : 0.8,
        color : "#fff",
        fillOpacity : 0.6,
        fillColor : col //"#FFF056"
    };
    if(props['id'] === input_ssc){
        returnStyle = {
            weight : 2,
            opacity : 0.8,
            color : "#b30000",
            fillOpacity : 0.6,
            fillColor : col //"#FFF056"
        };
    }
    return returnStyle;
}

// get color depending on ratio of count versus max value
function getColor(val, pop) {
    var colour = "";

    // show dark red/gray if low population (these can dominate the map)
    if (pop <= currentBoundaryMin){
        colour =  lowPopColour;
    } else {
        //convert value to int to get the right colour
        var valInt = parseInt(val.toFixed(1).toString().replace(".",""));
        colour = "#" + colourRamp.colourAt(valInt);
    }

    return colour;
}

function onEachFeature(feature, layer) {
    layer.on({
        click : highlightFeature,
        mouseover : highlightFeature,
        mouseout : resetHighlight
        // click : zoomToFeature
    });
}

//Set the style of the highlight suburb.
function highlightFeature(e) {
    if (currLayer !== undefined) {
        geojsonLayer.resetStyle(currLayer);
    }

    currLayer = e.target;
    var props = currLayer.feature.properties;

    if (currLayer !== undefined) {

        //var highlightColour = "#fff";
        var hightlightstyle = {
            weight: 0,
            color: "#fff",
            opacity: 0.8,
            fillOpacity : 0.1,
        }
        if(props['id'] === input_ssc){
            hightlightstyle = {
                weight: 2,
                color: "#b30000",
                opacity: 0.8,
                fillOpacity : 0.1,
            }
        }
        currLayer.setStyle(hightlightstyle);

        //    if (!L.Browser.ie && !L.Browser.edge && !L.Browser.opera) {
        //currLayer.bringToFront();
        //    }
        //console.log("loadmap.js::highlightFeature:");
        //console.log(currentStatId);
        //console.log(currLayer.feature.properties[currentStatId]);
        info.update(currLayer.feature.properties);
    }
}

function resetHighlight(e) {
    geojsonLayer.resetStyle(e.target);
    info.update();
}

// function zoomToFeature(e) {
//    map.fitBounds(e.target.getBounds());
// }


//the value is in [0,1]
function heatMapColorforValue(value){
    var h = (1.0 - value) * 240
    return hslToHex(h,100,50)
    //return "hsl(" + h + ", 100%, 50%)";
}

//convert hsl to rgb
function hslToHex(h, s, l) {
    h /= 360;
    s /= 100;
    l /= 100;
    let r, g, b;
    if (s === 0) {
        r = g = b = l; // achromatic
    } else {
        const hue2rgb = (p, q, t) => {
            if (t < 0) t += 1;
            if (t > 1) t -= 1;
            if (t < 1 / 6) return p + (q - p) * 6 * t;
            if (t < 1 / 2) return q;
            if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
            return p;
        };
        const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
        const p = 2 * l - q;
        r = hue2rgb(p, q, h + 1 / 3);
        g = hue2rgb(p, q, h);
        b = hue2rgb(p, q, h - 1 / 3);
    }
    const toHex = x => {
        const hex = Math.round(x * 255).toString(16);
        return hex.length === 1 ? '0' + hex : hex;
    };
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}