/**
 * Handle all google tables and charts in the result.html
 */
google.charts.load("current", {packages:['corechart','table']});
google.charts.setOnLoadCallback(getDataAndShowTable_g3_01);
google.charts.setOnLoadCallback(getDataAndShowTable_g2_01);
google.charts.setOnLoadCallback(getDataAndShowTable_g1_01);
google.charts.setOnLoadCallback(getDataAndShowChart_g3_01);
google.charts.setOnLoadCallback(getDataAndShowChart_g2_01);
google.charts.setOnLoadCallback(getDataAndShowChart_g1_01);

function getDataAndShowTable_g3_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&name='+inputSuburb+'&stat='+'g3'+'&type='+'table'+'&no='+1;
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
        drawTable_g3_01(result);
    }

    function failure() {
    }
}


function drawTable_g3_01(result) {

    //var censusItem = 'g3';
    //console.log("googlechart.js::drawChart_g3_01 chartMap = ");
    //console.log(result);
    if (result == null ){
        return;
    }

    var data = new google.visualization.arrayToDataTable(result,false);

    var table = new google.visualization.Table(document.getElementById('table_g3_01'));
    table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
}

function getDataAndShowTable_g2_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&name='+inputSuburb+'&stat='+'g2'+'&type='+'table'+'&no='+1;
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
        drawTable_g2_01(result);
    }

    function failure() {
    }
}


function drawTable_g2_01(result) {

    //var censusItem = 'g3';
    //console.log("googlechart.js::drawChart_g3_01 chartMap = ");
    //console.log(result);
    if (result == null ){
        return;
    }

    var data = new google.visualization.arrayToDataTable(result,false);

    var table = new google.visualization.Table(document.getElementById('table_g2_01'));
    table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
}

function getDataAndShowTable_g1_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&name='+inputSuburb+'&stat='+'g1'+'&type='+'table'+'&no='+1;
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
        drawTable_g1_01(result);
    }

    function failure() {
    }
}


function drawTable_g1_01(result) {

    if (result == null ){
        return;
    }

    var data = new google.visualization.arrayToDataTable(result,false);

    var table = new google.visualization.Table(document.getElementById('table_g1_01'));
    table.draw(data, {showRowNumber: true, width: '100%', height: '100%'});
}

function getDataAndShowChart_g3_01(){
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&name='+inputSuburb+'&stat='+'g3'+'&type='+'chart'+'&no='+1;
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
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&name='+inputSuburb+'&stat='+'g2'+'&type='+'chart'+'&no='+1;
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
    var _biGetPatientList = '/gettablechart?input_ssc='+input_ssc+'&name='+inputSuburb+'&stat='+'g1'+'&type='+'chart'+'&no='+1;
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