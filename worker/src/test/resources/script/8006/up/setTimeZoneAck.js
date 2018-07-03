var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setTimeZoneAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 0) {
        logger.info( "setUpgradeInfo succeeded");
    } else {
        logger.error("setUpgradeInfo failed");
    }
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