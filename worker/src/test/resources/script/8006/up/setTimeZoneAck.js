var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up setTimeZoneAck
function execute(input, logger) {

    //时区
    var list = new List();
    var report = new Map();
    report.type = "report";
    report.command = "deviceReport";
    report.data = new Map();
    report.data.timeZone = new Integer(input.data.result - 12);
    list.add(report);

    return list;

}