var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setTimeZoneAck
function execute(input, logger) {

    //时区
    var list = new List();
    var report = new Map();
    report.type = "report";
    report.command = "deviceReport";
    report.data = new Map();
    event.data.timeZone = input.data.timeZone;
    list.add(report);

    return list;

}