/**
 * Created by E470 on 4/21/2018.
 */
google.charts.load("current", {packages:['corechart']});
google.charts.setOnLoadCallback(getDataAndShowChart_g3_01);
google.charts.setOnLoadCallback(getDataAndShowChart_g2_01);
google.charts.setOnLoadCallback(getDataAndShowChart_g1_01);


function getDataAndShowChart_g3_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&stat='+'g3'+'&type='+'chart'+'&no='+1;
    $.getJSON(_biGetPatientList,
        function callback(result, status, xhr) {
            if ("success" === status) {
                success(result);
            } else {
                failure(result);
            }
        }
    );

    function success(result) {
        drawChart_g3_01(result);
    }

    function failure() {
    }
}


function drawChart_g3_01(result) {

    //var censusItem = 'g3';
    //console.log("googlechart.js::drawChart_g3_01 chartMap = ");
    //console.log(result);
    if (result == null ){
        return;
    }

    //change the second column from string to int
    for(var i = 1; i < result.length;i++){
        var value = result[i];
        result[i][1] = parseInt(result[i][1]);
    }

    var data = google.visualization.arrayToDataTable(result);

    //var view = new google.visualization.DataView(data);

    var options = {
        title: "People in different age groups",
        width: 400,
        height: 400,
        legend: { position: "none" }
    };
    var chart = new google.visualization.BarChart(document.getElementById("chart_g3_01"));
    chart.draw(data, options);
}

function getDataAndShowChart_g2_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&stat='+'g2'+'&type='+'chart'+'&no='+1;
    $.getJSON(_biGetPatientList,
        function callback(result, status, xhr) {
            if ("success" === status) {
                success(result);
            } else {
                failure(result);
            }
        }
    );

    function success(result) {
        drawChart_g2_01(result);
    }

    function failure() {
    }
}


function drawChart_g2_01(result) {

    //var censusItem = 'g3';
    //console.log("googlechart.js::drawChart_g2_01 chartMap = ");
    //console.log(result);
    if (result == null ){
        return;
    }

    //change the second column from string to int
    for(var i = 1; i < result.length;i++){
        var value = result[i];
        result[i][1] = parseInt(result[i][1]);
    }

    var data = google.visualization.arrayToDataTable(result);

    //var view = new google.visualization.DataView(data);

    var options = {
        title: "Females in different age groups",
        width: 400,
        height: 400,
        legend: { position: "none" }
    };
    var chart = new google.visualization.BarChart(document.getElementById("chart_g2_01"));
    chart.draw(data, options);
}

function getDataAndShowChart_g1_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&stat='+'g1'+'&type='+'chart'+'&no='+1;
    $.getJSON(_biGetPatientList,
        function callback(result, status, xhr) {
            if ("success" === status) {
                success(result);
            } else {
                failure(result);
            }
        }
    );

    function success(result) {
        drawChart_g1_01(result);
    }

    function failure() {
    }
}


function drawChart_g1_01(result) {

    //var censusItem = 'g3';
    //console.log("googlechart.js::drawChart_g1_01 chartMap = ");
    //console.log(result);
    if (result == null ){
        return;
    }

    //change the second column from string to int
    for(var i = 1; i < result.length;i++){
        var value = result[i];
        result[i][1] = parseInt(result[i][1]);
    }

    var data = google.visualization.arrayToDataTable(result);

    //var view = new google.visualization.DataView(data);

    var options = {
        title: "Males in different age groups",
        width: 400,
        height: 400,
        legend: { position: "none" }
    };
    var chart = new google.visualization.BarChart(document.getElementById("chart_g1_01"));
    chart.draw(data, options);
}